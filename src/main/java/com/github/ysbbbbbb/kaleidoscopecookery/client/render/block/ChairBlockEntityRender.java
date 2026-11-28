package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.ChairBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.ModModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Util;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public class ChairBlockEntityRender implements BlockEntityRenderer<ChairBlockEntity, ChairBlockEntityRender.State> {
    private static final Function<DyeColor, Identifier> CACHE_MODEL = Util.memoize(color ->
            Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block/carpet/chair/" + color.getName()));

    public ChairBlockEntityRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ChairBlockEntity chair, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(chair, state, partialTick, cameraPosition, breakProgress);
        state.rotation = chair.getBlockState().getValue(HorizontalDirectionalBlock.FACING).getOpposite().get2DDataValue();
        ModModelEvent.updateModel(state.carpet, CACHE_MODEL.apply(chair.getColor()));
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.rotation * 90));
        poseStack.translate(-0.5, 0, -0.5);
        state.carpet.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    public static final class State extends BlockEntityRenderState {
        private final BlockModelRenderState carpet = new BlockModelRenderState();
        private int rotation;
    }
}
