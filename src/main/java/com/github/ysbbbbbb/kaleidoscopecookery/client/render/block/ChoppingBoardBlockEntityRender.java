package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.ChoppingBoardBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.ChoppingBoardBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.ModModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ChoppingBoardBlockEntityRender implements BlockEntityRenderer<ChoppingBoardBlockEntity, ChoppingBoardBlockEntityRender.State> {
    public ChoppingBoardBlockEntityRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ChoppingBoardBlockEntity choppingBoard, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(choppingBoard, state, partialTick, cameraPosition, breakProgress);
        Identifier modelId = choppingBoard.getModelId();
        if (modelId == null) {
            return;
        }
        if (choppingBoard.previousModel == null || !modelId.equals(choppingBoard.previousModel)) {
            choppingBoard.previousModel = modelId;
            choppingBoard.cacheModels = new Identifier[choppingBoard.getMaxCutCount() + 1];
            for (int i = 0; i <= choppingBoard.getMaxCutCount(); i++) {
                Identifier location = Identifier.fromNamespaceAndPath(modelId.getNamespace(), "chopping_board/" + modelId.getPath() + "/" + i);
                choppingBoard.cacheModels[i] = location;
            }
        }
        if (choppingBoard.cacheModels == null) {
            return;
        }
        int index = Math.min(choppingBoard.getCurrentCutCount(), choppingBoard.cacheModels.length - 1);
        Identifier cacheModel = choppingBoard.cacheModels[index];
        state.model = ModModelEvent.getModel(cacheModel);
        ModModelEvent.copyBreakProgress(state);
        state.rotationSteps = choppingBoard.getBlockState().getValue(ChoppingBoardBlock.FACING).get2DDataValue();
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.model == null) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5D, 0, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotationSteps * 90));
        poseStack.translate(-0.5D, 0.125, -0.5D);
        ModModelEvent.submitModel(state.model, state, poseStack, collector, Sheets.cutoutBlockSheet());
        poseStack.popPose();
    }

    public static final class State extends BlockEntityRenderState {
        private @Nullable BlockStateModelPart model;
        private int rotationSteps;
    }
}
