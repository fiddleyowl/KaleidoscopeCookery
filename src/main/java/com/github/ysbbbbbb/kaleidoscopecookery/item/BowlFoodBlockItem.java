package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.config.ClientConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods.Food;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.CompatRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import org.jspecify.annotations.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class BowlFoodBlockItem extends BlockItem implements IHasContainer {
    private final List<MobEffectInstance> effectInstances;
    private final Function<Quality, List<MobEffectInstance>> effectCache;
    private final Optional<Item> usingConvertsTo;

    public BowlFoodBlockItem(Item.Properties itemProperties, Block block, Food properties, @Nullable ItemLike usingConvertsTo) {
        itemProperties = properties.applyTo(itemProperties.stacksTo(16));
        if (usingConvertsTo != null) {
            itemProperties = itemProperties.usingConvertsTo(usingConvertsTo.asItem());
        }
        super(block, itemProperties);
        this.usingConvertsTo = Optional.ofNullable(usingConvertsTo).map(ItemLike::asItem);
        this.effectInstances = properties.guaranteedEffects();
        this.effectCache = Util.memoize(quality -> QualityUtils.modifyEffects(this.effectInstances, quality));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (level instanceof ServerLevel serverLevel && this.getBlock() instanceof FoodBiteBlock foodBiteBlock) {
            LootParams.Builder builder = (new LootParams.Builder(serverLevel))
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(entity.blockPosition()))
                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, null);
            BlockState state = foodBiteBlock.defaultBlockState().setValue(foodBiteBlock.getBites(), foodBiteBlock.getMaxBites());
            List<ItemStack> drops = getDrops(state, builder);
            for (ItemStack itemStack : drops) {
                if (itemStack.isEmpty()) {
                    continue;
                }
                // 需要剔除 usingConvertsTo，因为已经给过了
                if (this.usingConvertsTo.isPresent()) {
                    if (itemStack.getItem() == this.usingConvertsTo.get()) {
                        continue;
                    }
                }
                if (entity instanceof Player player) {
                    ItemHandlerHelper.giveItemToPlayer(player, itemStack);
                } else {
                    ItemEntity itemEntity = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), itemStack);
                    level.addFreshEntity(itemEntity);
                }
            }
        }
        return QualityUtils.finishUsingFood(stack, level, entity);
    }

    private List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        Optional<ResourceKey<LootTable>> resourcekey = state.getBlock().getLootTable();
        if (resourcekey.isEmpty()) {
            return Collections.emptyList();
        } else {
            LootParams lootParams = params.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
            ServerLevel serverLevel = lootParams.getLevel();
            LootTable lootTable = serverLevel.getServer().reloadableRegistries().getLootTable(resourcekey.orElseThrow());
            return lootTable.getRandomItems(lootParams);
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        // 描述
        Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id != null) {
            String key = "tooltip.%s.%s.maxim".formatted(id.getNamespace(), id.getPath());
            MutableComponent full = Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
            // 先拿到纯文本，再按 \n 切
            String text = full.getString();
            for (String line : text.split("\n")) {
                if (!line.isEmpty()) {
                    tooltip.accept(Component.literal(line).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                } else {
                    tooltip.accept(CommonComponents.EMPTY);
                }
            }
        }

        boolean showEffect = !this.effectInstances.isEmpty()
                             && CompatRegistry.SHOW_POTION_EFFECT_TOOLTIPS
                             && ClientConfig.SHOW_FOOD_EFFECT_TOOLTIPS.get();

        // 品质
        if (QualityUtils.hasQuality(stack)) {
            Quality quality = QualityUtils.getQuality(stack);
            tooltip.accept(quality.getTooltip());
            if (showEffect) {
                tooltip.accept(CommonComponents.space());
                PotionContents.addPotionTooltip(this.effectCache.apply(quality), tooltip, 1.0F, context.tickRate());
            }
        } else {
            tooltip.accept(CommonComponents.space());
            PotionContents.addPotionTooltip(this.effectInstances, tooltip, 1.0F, context.tickRate());
        }
    }

    @Override
    public Item getContainerItem() {
        return Items.BOWL;
    }
}
