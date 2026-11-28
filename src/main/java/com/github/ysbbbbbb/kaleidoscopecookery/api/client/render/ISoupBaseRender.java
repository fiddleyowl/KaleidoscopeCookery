package com.github.ysbbbbbb.kaleidoscopecookery.api.client.render;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public interface ISoupBaseRender {
    RenderType SOUP_SURFACE = RenderType.create("kaleidoscope_cookery:soup_surface",
            RenderSetup.builder(RenderPipelines.ENTITY_TRANSLUCENT_CULL)
                    .withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
                    .useLightmap()
                    .useOverlay()
                    .affectsCrumbling()
                    .setOutline(RenderSetup.OutlineProperty.AFFECTS_OUTLINE)
                    .sortOnUpload()
                    .createRenderSetup());

    /**
     * 从汤锅提取绘制状态。未开始烹饪时使用汤底自身的贴图，烹饪中及完成后使用配方指定的贴图。
     */
    SoupBaseRenderState extractRenderState(StockpotBlockEntity stockpot, float partialTick, SpriteGetter sprites,
                             BlockEntityRenderState blockEntityState, @Nullable Identifier texture, float soupHeight);

    /**
     * 工具方法，用于渲染流体贴图
     */
    static void renderSurface(SoupBaseRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {
        collector.submitCustomGeometry(poseStack, SOUP_SURFACE, (pose, buffer) -> drawSurface(state, pose.pose(), buffer));
        ModelFeatureRenderer.CrumblingOverlay breakProgress = state.breakProgress;
        if (breakProgress != null) {
            // 裂纹在汤面绘制后叠加。
            RenderType crackRenderType = ModelBakery.DESTROY_TYPES.get(breakProgress.progress());
            collector.order(1).submitCustomGeometry(poseStack, crackRenderType, (pose, buffer) -> {
                VertexConsumer decal = new SheetedDecalTextureGenerator(buffer, breakProgress.cameraPose(), 1.0F);
                drawSurface(state, pose.pose(), decal);
            });
        }
    }

    private static void drawSurface(SoupBaseRenderState state, Matrix4f matrix, VertexConsumer vertexConsumer) {
        TextureAtlasSprite sprite = state.sprite;
        int color = state.color;
        int packedLight = state.packedLight;
        float y = state.soupHeight;

        // 锅内水面的位置和大小（根据实际锅模型调整）
        float min = 3 / 16f, max = 1 - 3 / 16f;

        // 渲染一个平面
        vertexConsumer.addVertex(matrix, min, y, min)
                .setColor(color)
                .setUv(sprite.getU0(), sprite.getV0())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0, 1, 0);
        vertexConsumer.addVertex(matrix, min, y, max)
                .setColor(color)
                .setUv(sprite.getU0(), sprite.getV(10 / 16f))
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0, 1, 0);
        vertexConsumer.addVertex(matrix, max, y, max)
                .setColor(color)
                .setUv(sprite.getU(10 / 16f), sprite.getV(10 / 16f))
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0, 1, 0);
        vertexConsumer.addVertex(matrix, max, y, min)
                .setColor(color)
                .setUv(sprite.getU(10 / 16f), sprite.getV0())
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0, 1, 0);
    }

    /**
     * 还没有放入原料时的汤底的渲染
     */
    default void renderWhenPutIngredient(SoupBaseRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        renderSurface(state, poseStack, collector);
    }

    /**
     * 烹饪中的汤底渲染
     */
    default void renderWhenCooking(SoupBaseRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        renderSurface(state, poseStack, collector);
    }

    /**
     * 烹饪完成后的汤底渲染
     */
    default void renderWhenFinished(SoupBaseRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        renderSurface(state, poseStack, collector);
    }

    final class SoupBaseRenderState {
        private final TextureAtlasSprite sprite;
        private final int color; // 流体颜色
        private final int packedLight;
        private final float soupHeight; // 汤底的高度
        private final ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress;
        public @Nullable EntityRenderState mobRenderState;
        public int mobAnimationSeed;

        public SoupBaseRenderState(TextureAtlasSprite sprite, int color, float soupHeight, BlockEntityRenderState blockEntityState) {
            this.sprite = sprite;
            this.color = color;
            this.packedLight = blockEntityState.lightCoords;
            this.soupHeight = soupHeight;
            this.breakProgress = blockEntityState.breakProgress;
        }
    }
}
