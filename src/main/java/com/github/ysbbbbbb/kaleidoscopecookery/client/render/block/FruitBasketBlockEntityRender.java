package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jspecify.annotations.Nullable;

public class FruitBasketBlockEntityRender implements BlockEntityRenderer<FruitBasketBlockEntity, FruitBasketBlockEntityRender.State> {
    private final ItemModelResolver itemModelResolver;

    public FruitBasketBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(FruitBasketBlockEntity basket, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(basket, state, partialTick, cameraPosition, breakProgress);
        ItemStackHandler items = basket.getItems();
        state.rotation = basket.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() * 90;
        for (int index = 0; index < state.items.length; index++) {
            ItemStack stack = items.getStackInSlot(index);
            if (stack.isEmpty()) {
                state.items[index] = null;
                continue;
            }
            ItemStackRenderState item = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(item, stack, ItemDisplayContext.FIXED, basket.getLevel(), null, 0);
            state.items[index] = item;
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(state.rotation));
        poseStack.translate(-0.5, 0, -0.5);
        poseStack.translate(0.1, 0.3, 0.35);
        for (int i = 0; i < 2; i++) {
            poseStack.pushPose();
            for (int j = 0; j < 4; j++) {
                int index = i * 4 + j;
                ItemStackRenderState item = state.items[index];
                if (item != null) {
                    poseStack.translate(0.15, 0, 0);
                    poseStack.pushPose();
                    poseStack.translate(0, 0, index % 2 == 0 ? -0.01f : 0.01f);
                    poseStack.mulPose(Axis.YN.rotationDegrees(90));
                    poseStack.mulPose(Axis.XN.rotationDegrees(-30));
                    poseStack.scale(0.375f, 0.375f, 0.375f);
                    item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
                    poseStack.popPose();
                }
            }
            poseStack.popPose();
            poseStack.translate(0, 0, 0.32);
        }
        poseStack.popPose();
    }

    public static final class State extends BlockEntityRenderState {
        private final @Nullable ItemStackRenderState[] items = new ItemStackRenderState[8];
        private int rotation;
    }
}
