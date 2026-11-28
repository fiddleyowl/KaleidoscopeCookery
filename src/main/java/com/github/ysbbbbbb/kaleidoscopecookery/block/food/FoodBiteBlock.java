package com.github.ysbbbbbb.kaleidoscopecookery.block.food;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods.Food;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteAnimateTicks;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public class FoodBiteBlock extends FoodBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    /**
     * 数量比 Quality 的数量多 1，超出范围的表示默认（即旧版数据）
     */
    public static final IntegerProperty QUALITY = IntegerProperty.create("quality", 0, Quality.values().length);
    public static final int DEFAULT_QUALITY = Quality.values().length;

    protected final Food foodProperties;
    protected final IntegerProperty bites;
    protected final int maxBites;
    protected final FoodBiteAnimateTicks.@Nullable AnimateTick animateTick;

    protected VoxelShape aabb = FoodBlock.AABB;

    public FoodBiteBlock(Properties properties, Food foodProperties, int maxBites, FoodBiteAnimateTicks.@Nullable AnimateTick animateTick) {
        super(properties);
        this.maxBites = maxBites;
        this.foodProperties = foodProperties;
        this.bites = IntegerProperty.create("bites", 0, maxBites);
        this.animateTick = animateTick;

        // 重置一遍 BlockState，因为在父类 FoodBlock 中已经创建了一个默认的 BlockStateDefinition
        StateDefinition.Builder<Block, BlockState> builder = new StateDefinition.Builder<>(this);
        this.createBitesBlockStateDefinition(builder);
        this.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(bites, 0)
                .setValue(FACING, Direction.SOUTH)
                .setValue(QUALITY, DEFAULT_QUALITY)
        );
    }

    public FoodBiteBlock(Properties properties, Food foodProperties) {
        this(properties, foodProperties, 3, null);
    }

    public FoodBiteBlock setAABB(VoxelShape aabb) {
        this.aabb = aabb;
        return this;
    }

    public IntegerProperty getBites() {
        return bites;
    }

    public int getMaxBites() {
        return maxBites;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (animateTick != null) {
            animateTick.animateTick(state, level, pos, random);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        int bites = state.getValue(this.bites);
        if (bites >= getMaxBites()) {
            level.destroyBlock(pos, true, player);
            return InteractionResult.SUCCESS;
        }
        if (level.isClientSide()) {
            if (eat(level, pos, state, player).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
        }
        return eat(level, pos, state, player);
    }

    protected InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        FoodProperties food = this.foodProperties.properties();
        if (!player.canEat(food.canAlwaysEat())) {
            return InteractionResult.PASS;
        }

        double radio = 1.0;
        int qualityNum = state.getValue(QUALITY);
        if (qualityNum != DEFAULT_QUALITY) {
            radio = Quality.BY_ID.apply(qualityNum).getRatio();
        }

        player.getFoodData().eat(
                (int) Math.round(food.nutrition() * radio),
                (float) (food.saturation() * radio)
        );

        for (ConsumeEffect consumeEffect : this.foodProperties.consumable().onConsumeEffects()) {
            // 摆盘食用只处理状态效果。
            if (!(consumeEffect instanceof ApplyStatusEffectsConsumeEffect applyStatusEffects)) {
                continue;
            }
            List<MobEffectInstance> statusEffects = applyStatusEffects.effects();
            float probability = applyStatusEffects.probability();
            if (!level.isClientSide() && level.getRandom().nextFloat() < probability) {
                // Consumable 的 apply_effects 接收效果列表，厨房仍为每个效果单独建组
                for (MobEffectInstance instance : statusEffects) {
                    MobEffectInstance newInstance = new MobEffectInstance(
                            instance.getEffect(),
                            (int) Math.round(instance.getDuration() * radio),
                            instance.getAmplifier()
                    );
                    player.addEffect(newInstance);
                }
            }
        }

        level.playSound(null, pos, SoundEvents.GENERIC_EAT.value(), SoundSource.PLAYERS,
                0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        level.gameEvent(player, GameEvent.EAT, pos);

        int bites = state.getValue(this.bites);
        if (bites < getMaxBites()) {
            level.setBlock(pos, state.setValue(this.bites, bites + 1), Block.UPDATE_ALL);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.aabb;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, QUALITY);
    }

    protected void createBitesBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(bites, FACING, QUALITY);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        int value = state.getValue(bites);
        return (3 - value) * 5;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction opposite = context.getHorizontalDirection().getOpposite();

        int quality = DEFAULT_QUALITY;
        ItemStack itemInHand = context.getItemInHand();
        if (QualityUtils.hasQuality(itemInHand)) {
            quality = QualityUtils.getQuality(itemInHand).getId();
        }

        return this.defaultBlockState()
                .setValue(FACING, opposite)
                .setValue(QUALITY, quality);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        int value = state.getValue(QUALITY);

        // 没有等级系统，默认掉落
        if (value == DEFAULT_QUALITY) {
            return drops;
        }
        // 吃过一口的，不会掉落原材料，忽略
        if (state.getValue(bites) != 0) {
            return drops;
        }
        // 查找原材料，然后附加等级标签
        drops.forEach(stack -> {
            if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof FoodBiteBlock) {
                Quality quality = Quality.BY_ID.apply(value);
                QualityUtils.setQuality(stack, quality);
            }
        });
        return drops;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
