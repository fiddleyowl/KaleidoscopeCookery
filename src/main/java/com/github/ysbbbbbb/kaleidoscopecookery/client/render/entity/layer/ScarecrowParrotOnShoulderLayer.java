package com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.layer;

import com.github.ysbbbbbb.kaleidoscopecookery.client.model.ScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.ScarecrowRender;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.animal.parrot.ParrotModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ParrotRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ParrotRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class ScarecrowParrotOnShoulderLayer extends RenderLayer<ScarecrowRender.State, ScarecrowModel> {
    private final ParrotModel model;

    public ScarecrowParrotOnShoulderLayer(RenderLayerParent<ScarecrowRender.State, ScarecrowModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new ParrotModel(modelSet.bakeLayer(ModelLayers.PARROT));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight,
                       ScarecrowRender.State state, float netHeadYaw, float headPitch) {
        ParrotRenderState parrot = state.shoulderParrot;
        if (parrot == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.625F, -1.675F, 0.0625F);
        collector.submitModel(
                this.model, parrot, poseStack, ParrotRenderer.getVariantTexture(parrot.variant),
                packedLight, OverlayTexture.NO_OVERLAY, state.outlineColor, null
        );
        poseStack.popPose();
    }
}
