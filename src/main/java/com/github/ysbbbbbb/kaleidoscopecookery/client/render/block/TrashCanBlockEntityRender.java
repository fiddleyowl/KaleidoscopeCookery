package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.TrashCanBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.TrashCanModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.ModModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class TrashCanBlockEntityRender implements BlockEntityRenderer<TrashCanBlockEntity, TrashCanBlockEntityRender.State> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/block/trash_can.png");

    private final TrashCanModel model;

    public TrashCanBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.model = new TrashCanModel(context.bakeLayer(TrashCanModel.LAYER_LOCATION));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(TrashCanBlockEntity trashCan, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(trashCan, state, partialTick, cameraPosition, breakProgress);
        ModModelEvent.copyBreakProgress(state);
        Level level = trashCan.getLevel();
        if (level == null) {
            return;
        }

        Direction facing = trashCan.getBlockState().getValue(TrashCanBlock.FACING);
        state.facingDeg = facing.get2DDataValue() * 90;
        state.ageInTicks = level.getGameTime() + partialTick;
        state.putState.copyFrom(trashCan.putState);
        state.withdrawState.copyFrom(trashCan.withdrawState);
        state.player1State.copyFrom(trashCan.player1State);
        state.player2State.copyFrom(trashCan.player2State);
        state.enterState.copyFrom(trashCan.enterState);
        state.visible = true;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.visible) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - state.facingDeg));
        collector.submitModel(
                this.model, state, poseStack, TEXTURE,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress
        );
        poseStack.popPose();
    }

    public static final class State extends BlockEntityRenderState {
        public final AnimationState putState = new AnimationState();
        public final AnimationState withdrawState = new AnimationState();
        public final AnimationState player1State = new AnimationState();
        public final AnimationState player2State = new AnimationState();
        public final AnimationState enterState = new AnimationState();
        public float ageInTicks;
        private boolean visible;
        private int facingDeg;
    }
}
