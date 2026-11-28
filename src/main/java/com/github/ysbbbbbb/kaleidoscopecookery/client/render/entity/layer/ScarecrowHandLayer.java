package com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.layer;

import com.github.ysbbbbbb.kaleidoscopecookery.client.model.ScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.ScarecrowRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class ScarecrowHandLayer extends ItemInHandLayer<ScarecrowRender.State, ScarecrowModel> {
    public ScarecrowHandLayer(ScarecrowRender entityRenderer) {
        super(entityRenderer);
    }

    @Override
    protected void submitArmWithItem(ScarecrowRender.State state, ItemStackRenderState item, ItemStack stack, HumanoidArm arm,
                                     PoseStack poseStack, SubmitNodeCollector collector, int packedLight) {
        if (!item.isEmpty()) {
            boolean isLeft = arm == HumanoidArm.LEFT;

            poseStack.pushPose();
            this.getParentModel().translateToHand(state, arm, poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            if (isLeft) {
                poseStack.translate(-0.125, 0, -1.375);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            } else {
                poseStack.translate(0.125, 0, -1.375);
                poseStack.mulPose(Axis.ZP.rotationDegrees(-90));
            }
            poseStack.mulPose(Axis.XP.rotationDegrees(85));
            poseStack.scale(0.75F, 0.75F, 0.75F);
            item.submit(poseStack, collector, packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
        }
    }
}
