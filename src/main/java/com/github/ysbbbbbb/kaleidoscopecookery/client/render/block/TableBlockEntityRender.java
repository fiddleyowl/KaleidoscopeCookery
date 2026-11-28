package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.TableBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TableBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.ModModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jspecify.annotations.Nullable;

import java.util.function.BiFunction;

public class TableBlockEntityRender implements BlockEntityRenderer<TableBlockEntity, TableBlockEntityRender.State> {
    // 用原版掉落物的厚度判断代替 isGui3d()，厚度超过 1/16 的模型视为立体
    private static final float FLAT_ITEM_DEPTH_THRESHOLD = 1.0F / 16.0F;

    private static final BiFunction<DyeColor, Integer, Identifier> CACHE_MODEL = Util.memoize((color, position) -> {
        String name = color.getName();
        if (position == TableBlock.SINGLE) {
            return Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block/carpet/table/" + name + "_single");
        }
        if (position == TableBlock.MIDDLE) {
            return Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block/carpet/table/" + name + "_middle");
        }
        if (position == TableBlock.LEFT) {
            return Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block/carpet/table/" + name + "_left");
        }
        return Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block/carpet/table/" + name + "_right");
    });

    private final ItemModelResolver itemModelResolver;

    public TableBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(TableBlockEntity table, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(table, state, partialTick, cameraPosition, breakProgress);
        BlockState blockState = table.getBlockState();
        state.axis = blockState.getValue(TableBlock.AXIS);

        if (blockState.getValue(TableBlock.HAS_CARPET)) {
            int position = blockState.getValue(TableBlock.POSITION);
            ModModelEvent.updateModel(state.carpet, CACHE_MODEL.apply(table.getColor(), position));
        } else {
            state.carpet.clear();
        }

        ItemStackHandler items = table.getItems();
        int count = 0;
        for (int i = 0; i < items.getSlots(); i++) {
            if (items.getStackInSlot(i).isEmpty()) {
                continue;
            }
            count++;
        }

        state.itemCount = Math.min(count, state.items.length);
        for (int slot = 0; slot < state.itemCount; slot++) {
            ItemStack stack = items.getStackInSlot(slot);
            this.itemModelResolver.updateForTopItem(
                    state.items[slot], stack, ItemDisplayContext.FIXED, table.getLevel(), null, 0
            );
            state.blockItems[slot] = stack.getItem() instanceof BlockItem;
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Direction.Axis axis = state.axis;
        if (!state.carpet.isEmpty()) {
            int rotation = axis == Direction.Axis.X ? 180 : 270;
            poseStack.pushPose();
            poseStack.translate(0.5, 0, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(-rotation));
            poseStack.translate(-0.5, 0, -0.5);
            state.carpet.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        int count = state.itemCount;
        if (count == 0) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.3125, 0.5);
        poseStack.scale(0.65F, 0.65F, 0.65F);

        if (count == 1) {
            this.rotation(poseStack, axis);
            this.submitItem(state, 0, poseStack, collector);
        } else if (count == 2) {
            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(-0.25, 0, 0.1);
            this.submitItem(state, 0, poseStack, collector);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.25, 0.01, -0.1);
            this.submitItem(state, 1, poseStack, collector);
            poseStack.popPose();
        } else if (count == 3) {
            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.25, 0, -0.2);
            this.submitItem(state, 0, poseStack, collector);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(-0.25, 0.01, 0);
            this.submitItem(state, 1, poseStack, collector);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.24, 0.02, 0.2);
            this.submitItem(state, 2, poseStack, collector);
            poseStack.popPose();
        } else {
            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.25, 0, -0.3);
            this.submitItem(state, 0, poseStack, collector);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(-0.24, 0.01, -0.1);
            this.submitItem(state, 1, poseStack, collector);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(0.24, 0.02, 0.1);
            this.submitItem(state, 2, poseStack, collector);
            poseStack.popPose();

            poseStack.pushPose();
            this.rotation(poseStack, axis);
            poseStack.translate(-0.25, 0.03, 0.3);
            this.submitItem(state, 3, poseStack, collector);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private void rotation(PoseStack poseStack, Direction.Axis axis) {
        if (axis == Direction.Axis.X) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
        } else {
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
        }
    }

    private void submitItem(State state, int slot, PoseStack poseStack, SubmitNodeCollector collector) {
        ItemStackRenderState item = state.items[slot];
        // 仅 3D 立体模型才会有 0.25 偏移
        if (state.blockItems[slot] && item.getModelBoundingBox().getZsize() > FLAT_ITEM_DEPTH_THRESHOLD) {
            poseStack.translate(0, -0.25, 0);
        }
        item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
    }

    public static final class State extends BlockEntityRenderState {
        private final BlockModelRenderState carpet = new BlockModelRenderState();
        private final ItemStackRenderState[] items = {
                new ItemStackRenderState(), new ItemStackRenderState(),
                new ItemStackRenderState(), new ItemStackRenderState()
        };
        private final boolean[] blockItems = new boolean[items.length];
        private Direction.Axis axis = Direction.Axis.Z;
        private int itemCount;
    }
}
