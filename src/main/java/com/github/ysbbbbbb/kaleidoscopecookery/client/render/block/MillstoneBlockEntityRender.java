package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.MillstoneModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class MillstoneBlockEntityRender implements BlockEntityRenderer<MillstoneBlockEntity, MillstoneBlockEntityRender.State> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/block/millstone.png");

    private final ItemModelResolver itemModelResolver;
    private final MillstoneModel bodyModel;

    public MillstoneBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.bodyModel = new MillstoneModel(context.bakeLayer(MillstoneModel.LAYER_LOCATION));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(MillstoneBlockEntity millstone, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(millstone, state, partialTick, cameraPosition, breakProgress);
        Level level = millstone.getLevel();
        if (level == null) {
            return;
        }
        Direction facing = millstone.getBlockState().getValue(MillstoneBlock.FACING);
        state.facingDeg = facing.get2DDataValue() * 90;

        if (millstone.hasEntity()) {
            float rot = state.facingDeg + millstone.getRotation(level, partialTick);
            state.wheelRotation = -rot * Mth.DEG_TO_RAD;
            state.rollRotation = rot * Mth.DEG_TO_RAD;
            state.stickRotation = -millstone.getLiftAngle() * Mth.DEG_TO_RAD;
        } else {
            float rot = state.facingDeg + millstone.getCacheRot();
            state.wheelRotation = -rot * Mth.DEG_TO_RAD;
        }

        state.visible = true;
        ItemStack renderItem = millstone.getOutputs().getStackInSlot(0);
        if (renderItem.isEmpty()) {
            renderItem = millstone.getInput();
        }
        state.itemCount = Math.min(renderItem.getCount(), MillstoneBlockEntity.MAX_INPUT_COUNT);
        if (!renderItem.isEmpty()) {
            state.seed = millstone.hashCode();
            ItemStack display = ItemRenderReplacer.display(renderItem, ItemRenderReplacerReloadListener.INSTANCE.millstone());
            this.itemModelResolver.updateForTopItem(state.item, display, ItemDisplayContext.FIXED, level, null, 0);
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (!state.visible) {
            return;
        }
        this.submitBody(state, poseStack, collector);
        if (state.itemCount > 0) {
            this.submitItems(state, poseStack, collector);
        }
    }

    private void submitBody(State state, PoseStack poseStack, SubmitNodeCollector collector) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - state.facingDeg));
        collector.submitModel(this.bodyModel, state, poseStack, TEXTURE,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0, null);
        poseStack.popPose();
    }

    private void submitItems(State state, PoseStack poseStack, SubmitNodeCollector collector) {
        RandomSource source = RandomSource.create(state.seed);
        for (int i = 0; i < state.itemCount; i++) {
            poseStack.pushPose();
            poseStack.translate(0, 0.875, 0);
            poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45 + source.nextInt(15)), 0.5f, 0, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(source.nextInt(20)));
            poseStack.mulPose(Axis.XN.rotationDegrees(80 + source.nextInt(20)));
            poseStack.scale(0.65F, 0.65F, 0.65F);
            state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public AABB getRenderBoundingBox(MillstoneBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return getAABB(pos.offset(-3, 0, -3), pos.offset(3, 1, 3));
    }

    private static AABB getAABB(BlockPos pStart, BlockPos pEnd) {
        return new AABB(pStart.getX(), pStart.getY(), pStart.getZ(), pEnd.getX(), pEnd.getY(), pEnd.getZ());
    }

    public static final class State extends BlockEntityRenderState {
        public float wheelRotation;
        public float rollRotation;
        public float stickRotation;
        private final ItemStackRenderState item = new ItemStackRenderState();
        private boolean visible;
        private int facingDeg;
        private int itemCount;
        private long seed;
    }
}
