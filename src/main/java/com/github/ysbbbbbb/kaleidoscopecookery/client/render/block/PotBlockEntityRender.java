package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.ModModelEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class PotBlockEntityRender implements BlockEntityRenderer<PotBlockEntity, PotBlockEntityRender.State> {
    private final ItemModelResolver itemModelResolver;

    public PotBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(PotBlockEntity pot, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(pot, state, partialTick, cameraPosition, breakProgress);
        ModModelEvent.copyBreakProgress(state);
        RandomSource source = RandomSource.create(pot.getSeed());
        PotBlockEntity.StirFryAnimationData data = pot.animationData;
        long elapsedMillis = System.currentTimeMillis() - data.timestamp;

        if (data.preSeed == -1L) {
            data.preSeed = pot.getSeed();
        }
        if (data.preSeed != pot.getSeed()) {
            data.preSeed = pot.getSeed();
            if (elapsedMillis > 1000) {
                data.timestamp = System.currentTimeMillis();
                data.randomHeights = new float[9];
                for (int i = 0; i < 9; i++) {
                    data.randomHeights[i] = 0.25f + source.nextFloat() * 1;
                }
            }
        }

        state.rotation = pot.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() * 90;
        state.elapsedMillis = elapsedMillis;
        // 焦糊程度，菜变黑
        if (pot.getStatus() == PotBlockEntity.BURNT) {
            int tick = pot.getCurrentTick();
            int burntLevel = Mth.clamp(tick / 25, 0, 16);
            state.lightCoords = OverlayTexture.u(burntLevel);
        }

        // 炒菜阶段，或者炒完，但是需要碗才能装的菜，只渲染原材料
        boolean showInputs = pot.getStatus() != PotBlockEntity.FINISHED && pot.getStatus() != PotBlockEntity.BURNT;
        if (showInputs || pot.hasCarrier()) {
            List<ItemStack> items = pot.getInputs();
            for (int i = 0; i < items.size(); i++) {
                ItemStack item = items.get(i);
                if (!item.isEmpty()) {
                    extractItem(pot, state, source, i, data, item);
                }
            }
        } else {
            // 结束阶段，并且不需要碗的菜，直接渲染结果
            extractItem(pot, state, source, 0, data, pot.getResult());
        }
    }

    private void extractItem(PotBlockEntity pot, State state, RandomSource source, int index,
                             PotBlockEntity.StirFryAnimationData data, ItemStack item) {
        int count = 90 + source.nextInt(90);
        state.itemRotations[index] = index * count;
        if (state.elapsedMillis < 1000) {
            state.itemHeights[index] = data.randomHeights[index] * Mth.sin(Mth.PI * state.elapsedMillis / 1000f);
        }
        ItemStackRenderState itemState = new ItemStackRenderState();
        ItemStack display = ItemRenderReplacer.display(item, ItemRenderReplacerReloadListener.INSTANCE.pot());
        this.itemModelResolver.updateForTopItem(itemState, display, ItemDisplayContext.FIXED, pot.getLevel(), null, 0);
        state.items[index] = itemState;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.1, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(state.rotation));
        poseStack.mulPose(Axis.XN.rotationDegrees(90));
        poseStack.scale(0.5f, 0.5f, 0.5f);

        for (int i = 0; i < state.items.length; i++) {
            ItemStackRenderState item = state.items[i];
            if (item == null) {
                continue;
            }
            submitItem(state, poseStack, collector, i, item);
            poseStack.translate(0, 0, 0.025);
        }
        poseStack.popPose();
    }

    private void submitItem(State state, PoseStack poseStack, SubmitNodeCollector collector, int index, ItemStackRenderState item) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.ZN.rotationDegrees(state.itemRotations[index]));
        if (state.elapsedMillis < 1000) {
            poseStack.translate(0, 0, state.itemHeights[index]);
            poseStack.mulPose(Axis.XN.rotationDegrees(720f / 1000 * state.elapsedMillis));
        }
        ModModelEvent.submitItem(item, state, poseStack, collector);

        poseStack.popPose();
    }

    public static final class State extends BlockEntityRenderState {
        private final @Nullable ItemStackRenderState[] items = new ItemStackRenderState[PotRecipe.RECIPES_SIZE];
        private final int[] itemRotations = new int[items.length];
        private final float[] itemHeights = new float[items.length];
        private int rotation;
        private long elapsedMillis;
    }
}
