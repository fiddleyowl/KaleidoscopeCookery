package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.ModModelEvent;
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
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class KitchenShelfBlockEntityRender implements BlockEntityRenderer<KitchenShelfBlockEntity, KitchenShelfBlockEntityRender.State> {
    private final ItemModelResolver itemModelResolver;

    public KitchenShelfBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(KitchenShelfBlockEntity shelf, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(shelf, state, partialTick, cameraPosition, breakProgress);
        ModModelEvent.copyBreakProgress(state);
        ItemStack stored = shelf.getStoredStack();
        state.displayCount = Math.min(16, stored.getCount());
        if (stored.isEmpty()) {
            return;
        }

        state.rotation = shelf.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180;
        this.itemModelResolver.updateForTopItem(state.item, stored.copyWithCount(1), ItemDisplayContext.FIXED, shelf.getLevel(), null, 0);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.displayCount == 0) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(state.rotation));

        // 以朝北的厨架为基准，每层从后往前放四排，每排两个。
        var tilt = Axis.XP.rotationDegrees(45);
        for (int index = 0; index < state.displayCount; index++) {
            int layer = index / 8;
            int slot = index % 8;
            int odd = slot / 2;

            poseStack.pushPose();
            poseStack.translate(
                    slot % 2 == 0 ? -0.25 : 0.25,
                    0.7 + layer * -0.5,
                    0.3 - odd * 0.20
            );
            poseStack.mulPose(tilt);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            ModModelEvent.submitItem(state.item, state, poseStack, collector);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    public static final class State extends BlockEntityRenderState {
        private final ItemStackRenderState item = new ItemStackRenderState();
        private int displayCount;
        private float rotation;
    }
}
