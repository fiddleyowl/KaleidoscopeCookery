package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TeaBannerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner.LeftBannerModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner.NormalBannerModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner.PatternModel;
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
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import static com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery.MOD_ID;

public class TeaBannerBlockEntityRenderer implements BlockEntityRenderer<TeaBannerBlockEntity, TeaBannerBlockEntityRenderer.State> {
    private final NormalBannerModel normalBanner;
    private final LeftBannerModel wallBanner;
    private final PatternModel patternModel;

    public TeaBannerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.normalBanner = new NormalBannerModel(context.bakeLayer(NormalBannerModel.LAYER_LOCATION));
        this.wallBanner = new LeftBannerModel(context.bakeLayer(LeftBannerModel.LAYER_LOCATION));
        this.patternModel = new PatternModel(context.bakeLayer(PatternModel.LAYER_LOCATION));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(TeaBannerBlockEntity teaBanner, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(teaBanner, state, partialTick, cameraPosition, breakProgress);
        ModModelEvent.copyBreakProgress(state);
        Level level = teaBanner.getLevel();
        if (level == null) {
            return;
        }
        Direction facing = teaBanner.getBlockState().getValue(TeaBannerBlock.FACING);
        state.wallMounted = teaBanner.getBlockState().getValue(TeaBannerBlock.FACE) == AttachFace.WALL;
        state.facingDegrees = facing.get2DDataValue() * 90.0F;

        long positionPhase = teaBanner.getBlockPos().getX() * 7L
                             + teaBanner.getBlockPos().getY() * 9L
                             + teaBanner.getBlockPos().getZ() * 13L;
        float animationTime = positionPhase + level.getGameTime() + partialTick;

        state.waveAngle = (-0.0125F + 0.01F * Mth.cos(Mth.PI * 2.0F * animationTime / 100.0F))
                          * Mth.HALF_PI;

        state.color = teaBanner.getColor();
        String patternName = TeaBannerBlockEntity.getPatternTexture(teaBanner.getPatternItem());
        if (patternName == null) {
            patternName = "tea";
        }
        state.patternName = patternName;
        state.visible = true;
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.visible) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
        poseStack.mulPose(Axis.YN.rotationDegrees(180.0F - state.facingDegrees));
        if (state.wallMounted) {
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        }

        Identifier baseTexture = Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/banner/" + state.color.getName() + ".png");
        if (state.wallMounted) {
            collector.submitModel(
                    this.wallBanner, state.waveAngle, poseStack, baseTexture,
                    state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress
            );
        } else {
            collector.submitModel(
                    this.normalBanner, state.waveAngle, poseStack, baseTexture,
                    state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress
            );
        }

        if (state.wallMounted) {
            poseStack.translate(-1, 3, 0);
        } else {
            poseStack.translate(0, 0, 0);
        }

        Identifier patternTexture = Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/pattern/" + state.patternName + ".png");
        collector.submitModel(
                this.patternModel, state.waveAngle, poseStack, patternTexture,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress
        );
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(TeaBannerBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(3);
    }

    public static final class State extends BlockEntityRenderState {
        private boolean visible;
        private boolean wallMounted;
        private float facingDegrees;
        private float waveAngle;
        private DyeColor color = DyeColor.RED;
        private String patternName = "tea";
    }
}
