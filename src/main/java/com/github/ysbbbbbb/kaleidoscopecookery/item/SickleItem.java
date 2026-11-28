package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.event.SickleHarvestEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.TeaTreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEnchantments;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;
import java.util.function.Consumer;

public class SickleItem extends Item {
    public SickleItem(ToolMaterial material, Properties properties) {
        this(material, properties, createToolProperties());
    }

    public SickleItem(ToolMaterial material, Properties properties, Tool toolComponentData) {
        this(properties.durability(material.durability())
                .enchantable(material.enchantmentValue())
                .repairable(material.repairItems()), toolComponentData);
    }

    public SickleItem(Properties properties) {
        this(properties.durability(2000) // 耐久度
                .enchantable(5) // 附魔值
                .repairable(Items.FLINT), createToolProperties());
    }

    private SickleItem(Properties properties, Tool toolComponentData) {
        super(properties.component(DataComponents.TOOL, toolComponentData)
                .component(DataComponents.WEAPON, new Weapon(1)));
    }

    // 镰刀使用剑的挖掘规则，攻击属性由物品属性决定。
    private static Tool createToolProperties() {
        var blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return new Tool(List.of(
                Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 15.0F),
                Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE),
                Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5F)
        ), 1.0F, 2, true);
    }

    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
        return true;
    }

    @Override
    public boolean canPerformAction(ItemInstance stack, ItemAbility itemAbility) {
        return itemAbility == ItemAbilities.SWORD_SWEEP;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        // 生成挥动音效和粒子
        Player player = context.getPlayer();
        if (player == null) {
            return super.useOn(context);
        }
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        int sweepLevel = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .get(ModEnchantments.SWEEP)
                .map(stack::getEnchantmentLevel)
                .orElse(0);
        int radius = 2 + sweepLevel;
        // 根据清扫等级搜索 5x5、7x7、9x9 或 11x11 范围内的植物。
        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -radius; z <= radius; z++) {
                    harvest(pos, x, y, z, level, player, stack);
                }
            }
        }

        serverLevel.playSound(null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(),
                1.0F, 1.0F);
        double dx = -Mth.sin(player.getYRot() * (float) (Math.PI / 180.0));
        double dz = Mth.cos(player.getYRot() * (float) (Math.PI / 180.0));
        serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, player.getX() + dx, player.getY(0.5), player.getZ() + dz,
                0, dx, 0.0, dz, 0.0);
        int durabilityCost = switch (sweepLevel) {
            case 1 -> 2;
            case 2 -> 4;
            case 3 -> 8;
            default -> 1;
        };
        stack.hurtAndBreak(durabilityCost, player, EquipmentSlot.MAINHAND);
        player.getCooldowns().addCooldown(BuiltInRegistries.ITEM.getKey(this), 10);
        return InteractionResult.SUCCESS;
    }

    private boolean harvest(BlockPos pos, int x, int y, int z, Level level, Player player, ItemStack stack) {
        BlockPos newPos = pos.offset(x, y, z);
        if (!level.mayInteract(player, newPos)) {
            return false;
        }
        BlockState blockState = level.getBlockState(newPos);
        if (blockState.isAir()) {
            return false;
        }
        // 黑名单
        if (blockState.is(TagMod.SICKLE_HARVEST_BLACKLIST)) {
            return false;
        }

        Block block = blockState.getBlock();
        // 触发事件
        SickleHarvestEvent event = new SickleHarvestEvent(player, stack, newPos, blockState);
        if (NeoForge.EVENT_BUS.post(event).isCanceled()) {
            return event.isCostDurability();
        }

        // 如果是茶树
        if (block instanceof TeaTreeBlock teaTreeBlock) {
            if (teaTreeBlock.isMaxAge(blockState)) {
                teaTreeBlock.playerDestroy(level, player, newPos, blockState, null, ItemStack.EMPTY);
                level.setBlock(newPos, teaTreeBlock.getStateForAge(0), Block.UPDATE_ALL);
                level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(blockState));
                return true;
            }
            return false;
        }

        // 如果是作物，那么检查是否成熟
        if (block instanceof CropBlock cropBlock) {
            // 水稻特判
            if (block instanceof RiceCropBlock) {
                int position = blockState.getValue(RiceCropBlock.LOCATION);
                newPos = newPos.below(position);
                blockState = level.getBlockState(newPos);
            }
            if (cropBlock.isMaxAge(blockState)) {
                // 成熟则收割
                cropBlock.playerDestroy(level, player, newPos, blockState, null, ItemStack.EMPTY);
                BlockState stateForAge = cropBlock.getStateForAge(0);
                // 同步水属性状态
                BooleanProperty waterlogged = BlockStateProperties.WATERLOGGED;
                if (stateForAge.hasProperty(waterlogged)) {
                    stateForAge = stateForAge.setValue(waterlogged, blockState.getValue(waterlogged));
                }
                level.setBlock(newPos, stateForAge, Block.UPDATE_ALL);
                level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(blockState));
                return true;
            }
            return false;
        }

        // 如果是灌木，直接破坏
        if (block instanceof BushBlock) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.gameMode.destroyBlock(newPos);
                level.levelEvent(null, LevelEvent.PARTICLES_DESTROY_BLOCK, newPos, Block.getId(blockState));
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.sickle").withStyle(ChatFormatting.GRAY));
    }
}
