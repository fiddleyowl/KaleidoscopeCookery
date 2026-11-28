package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ShawarmaSpitBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ShawarmaSpitBlockEntity;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ShawarmaSpitBlockEntityRender implements BlockEntityRenderer<ShawarmaSpitBlockEntity, ShawarmaSpitBlockEntityRender.State> {
    private final ItemModelResolver itemModelResolver;

    public ShawarmaSpitBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ShawarmaSpitBlockEntity shawarmaSpit, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(shawarmaSpit, state, partialTick, cameraPosition, breakProgress);
        ItemStack cookedItem = shawarmaSpit.cookedItem;
        state.itemCount = 0;
        if (cookedItem.isEmpty()) {
            state.item.clear();
            return;
        }
        ItemStack renderItem;
        if (!shawarmaSpit.cookingItem.isEmpty()) {
            renderItem = shawarmaSpit.cookingItem;
        } else {
            renderItem = cookedItem;
        }
        BlockState blockState = shawarmaSpit.getBlockState();
        state.powered = blockState.getValue(BlockStateProperties.POWERED);
        state.half = blockState.getValue(ShawarmaSpitBlock.HALF);
        state.itemCount = renderItem.getCount();
        this.itemModelResolver.updateForTopItem(state.item, renderItem, ItemDisplayContext.FIXED, shawarmaSpit.getLevel(), null, 0);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.itemCount == 0) {
            return;
        }

        // 如果是充能状态，那么一直旋转
        if (state.powered) {
            long time = System.currentTimeMillis() % 360_0;
            poseStack.rotateAround(Axis.YP.rotationDegrees(time / 10f), 0.5f, 0, 0.5f);
        }

        // 如果是上半部分
        if (state.half == DoubleBlockHalf.UPPER) {
            poseStack.translate(0.25, 0.5, 0.25);
            this.submitItems(state, poseStack, collector);
        }

        // 如果是下半部分
        else if (state.half == DoubleBlockHalf.LOWER) {
            poseStack.translate(0.25, 0.875, 0.25);
            this.submitItems(state, poseStack, collector);
        }
    }

    private void submitItems(State state, PoseStack poseStack, SubmitNodeCollector collector) {
        for (int i = 0; i < state.itemCount; i++) {
            poseStack.pushPose();
            poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45), 0.25f, 0, 0.25f);
            poseStack.scale(0.65F, 0.65F, 0.65F);
            state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    public static final class State extends BlockEntityRenderState {
        private final ItemStackRenderState item = new ItemStackRenderState();
        private DoubleBlockHalf half = DoubleBlockHalf.LOWER;
        private boolean powered;
        private int itemCount;
    }
}
