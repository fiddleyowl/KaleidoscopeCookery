package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class KitchenwareRacksBlockEntityRender implements BlockEntityRenderer<KitchenwareRacksBlockEntity, KitchenwareRacksBlockEntityRender.State> {
    private final ItemModelResolver itemModelResolver;

    public KitchenwareRacksBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(KitchenwareRacksBlockEntity blockEntity, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPosition, breakProgress);
        state.rotation = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() * 90;
        this.itemModelResolver.updateForTopItem(state.itemLeft, blockEntity.getItemLeft(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        this.itemModelResolver.updateForTopItem(state.itemRight, blockEntity.getItemRight(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(state.rotation));

        if (!state.itemLeft.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.2, 0.4375, -0.3);
            this.submitItem(state.itemLeft, poseStack, collector, state.lightCoords);
            poseStack.popPose();
        }

        if (!state.itemRight.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.2, 0.4375, -0.3);
            this.submitItem(state.itemRight, poseStack, collector, state.lightCoords);
            poseStack.popPose();
        }
    }

    private void submitItem(ItemStackRenderState item, PoseStack poseStack, SubmitNodeCollector collector, int packedLight) {
        poseStack.scale(0.75f, 0.75f, 0.75f);
        poseStack.mulPose(Axis.XN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(-25));
        poseStack.mulPose(Axis.ZN.rotationDegrees(45));
        item.submit(poseStack, collector, packedLight, OverlayTexture.NO_OVERLAY, 0);
    }

    public static final class State extends BlockEntityRenderState {
        private final ItemStackRenderState itemLeft = new ItemStackRenderState();
        private final ItemStackRenderState itemRight = new ItemStackRenderState();
        private int rotation;
    }
}
