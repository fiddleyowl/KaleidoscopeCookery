package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.BambooTrayBlockEntity;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class BambooTrayBlockEntityRender implements BlockEntityRenderer<BambooTrayBlockEntity, BambooTrayBlockEntityRender.State> {
    private static final double[][] SLOT_POSITIONS = {
            {0.25, 0.25}, {0.75, 0.25}, {0.25, 0.75}, {0.75, 0.75}
    };
    private final ItemModelResolver itemModelResolver;

    public BambooTrayBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(BambooTrayBlockEntity tray, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tray, state, partialTick, cameraPosition, breakProgress);
        ModModelEvent.copyBreakProgress(state);
        for (int slot = 0; slot < tray.getItems().size(); slot++) {
            ItemStack stack = tray.getItems().get(slot);
            if (stack.isEmpty()) {
                continue;
            }
            int copies = stack.getCount() == 1 ? 1 : Math.min(5, (stack.getCount() - 1) / 16 + 2);
            ItemStackRenderState[] items = new ItemStackRenderState[copies];
            for (int copy = 0; copy < copies; copy++) {
                items[copy] = new ItemStackRenderState();
                this.itemModelResolver.updateForTopItem(items[copy], stack, ItemDisplayContext.FIXED,
                        tray.getLevel(), null, slot * 7 + copy);
            }
            state.items[slot] = items;
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        for (int slot = 0; slot < state.items.length; slot++) {
            ItemStackRenderState[] items = state.items[slot];
            if (items == null) {
                continue;
            }
            RandomSource random = RandomSource.create(state.blockPos.asLong() + slot * 31L);
            for (int copy = 0; copy < items.length; copy++) {
                poseStack.pushPose();
                double xOffset = (random.nextDouble() - 0.5) * 0.1;
                double zOffset = (random.nextDouble() - 0.5) * 0.1;
                poseStack.translate(
                        SLOT_POSITIONS[slot][0] + xOffset,
                        0.14 + copy * 0.01 + slot * 0.005,
                        SLOT_POSITIONS[slot][1] + zOffset
                );
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.mulPose(Axis.ZP.rotationDegrees(random.nextFloat() * 360));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                ModModelEvent.submitItem(items[copy], state, poseStack, collector);
                poseStack.popPose();
            }
        }
    }

    public static final class State extends BlockEntityRenderState {
        private final ItemStackRenderState[] @Nullable [] items = new ItemStackRenderState[SLOT_POSITIONS.length][];
    }
}
