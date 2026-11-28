package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.RecipeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class RecipeBlockEntityRender implements BlockEntityRenderer<RecipeBlockEntity, RecipeBlockEntityRender.State> {
    private final ItemModelResolver itemModelResolver;

    public RecipeBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(RecipeBlockEntity recipeBlock, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(recipeBlock, state, partialTick, cameraPosition, breakProgress);
        state.item.clear();
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        ItemStack stack = recipeBlock.getItems().getStackInSlot(0);
        if (stack.isEmpty()) {
            return;
        }
        RecipeItem.RecipeRecord record = stack.get(ModDataComponents.RECIPE_RECORD);
        if (record == null) {
            return;
        }

        ItemStack output = record.output();
        Direction facing = recipeBlock.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        AttachFace attachFace = recipeBlock.getBlockState().getValue(BlockStateProperties.ATTACH_FACE);

        state.rotationX = attachFace.ordinal();
        state.rotationY = facing.get2DDataValue() + (attachFace == AttachFace.CEILING ? 2 : 0);
        state.attachFace = attachFace;
        this.itemModelResolver.updateForTopItem(state.item, output, ItemDisplayContext.FIXED, recipeBlock.getLevel(), null, 0);
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.item.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.rotationY * 90));
        poseStack.mulPose(Axis.XP.rotationDegrees(90 - state.rotationX * 90));
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.scale(0.5f, 0.5f, 0.5f);

        if (state.attachFace == AttachFace.WALL) {
            poseStack.translate(1, 1.25, 0);
        } else {
            poseStack.translate(1, 0.75, 2);
        }

        state.item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    public static final class State extends BlockEntityRenderState {
        private final ItemStackRenderState item = new ItemStackRenderState();
        private AttachFace attachFace = AttachFace.FLOOR;
        private int rotationX;
        private int rotationY;
    }
}
