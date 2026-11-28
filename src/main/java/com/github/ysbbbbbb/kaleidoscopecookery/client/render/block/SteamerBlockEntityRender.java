package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.ModModelEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class SteamerBlockEntityRender implements BlockEntityRenderer<SteamerBlockEntity, SteamerBlockEntityRender.State> {
    private final ItemModelResolver itemModelResolver;

    public SteamerBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(SteamerBlockEntity steamer, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(steamer, state, partialTick, cameraPosition, breakProgress);
        ModModelEvent.copyBreakProgress(state);
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        // 盖住了就不渲染
        if (steamer.getBlockState().getValue(SteamerBlock.HAS_LID)) {
            return;
        }

        NonNullList<ItemStack> items = steamer.getItems();
        Map<Identifier, Identifier> map = ItemRenderReplacerReloadListener.INSTANCE.steamer();
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            Identifier key = BuiltInRegistries.ITEM.getKey(stack.getItem());
            state.hasReplacementModel[i] = map.containsKey(key);
            ItemStackRenderState itemState = new ItemStackRenderState();
            ItemStack display = ItemRenderReplacer.display(stack, map);
            this.itemModelResolver.updateForTopItem(itemState, display, ItemDisplayContext.FIXED, steamer.getLevel(), null, 0);
            state.items[i] = itemState;
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        for (int i = 0; i < state.items.length; i++) {
            ItemStackRenderState item = state.items[i];
            if (item == null) {
                continue;
            }

            double x = (i % 2) * 0.3 + 0.35;
            double y = (i / 4) * 0.5 + 0.25 + (i % 4) * 0.01;
            double z = ((i / 2) % 2) * 0.3 + 0.35;

            poseStack.pushPose();
            poseStack.translate(x, y, z);
            if (!state.hasReplacementModel[i]) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(-35));
                poseStack.mulPose(Axis.XN.rotationDegrees(90));
            } else {
                poseStack.translate(0, 0.4375, 0.4375);
                poseStack.mulPose(Axis.ZP.rotationDegrees(-35));
            }
            poseStack.scale(0.5F, 0.5F, 0.5F);
            ModModelEvent.submitItem(item, state, poseStack, collector);
            poseStack.popPose();
        }
    }

    public static final class State extends BlockEntityRenderState {
        private final @Nullable ItemStackRenderState[] items = new ItemStackRenderState[8];
        private final boolean[] hasReplacementModel = new boolean[items.length];
    }
}
