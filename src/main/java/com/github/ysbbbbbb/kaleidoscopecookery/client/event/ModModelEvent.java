package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID, value = Dist.CLIENT)
public class ModModelEvent {
    private static final String MODELS = "models/";
    private static final String MODELS_CHOPPING_BOARD = MODELS + "chopping_board";
    private static final String JSON = ".json";
    private static final Map<Identifier, StandaloneModelKey<BlockStateModelPart>> MODEL_KEYS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterStandalone event) {
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        resourceManager.listResources(MODELS_CHOPPING_BOARD, id -> id.getPath().endsWith(JSON))
                .keySet().stream().map(ModModelEvent::handleModelId).forEach(id -> register(event, id));

        // 颜色模型为地毯模板提供纹理
        for (DyeColor color : DyeColor.values()) {
            register(event, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block/carpet/chair/" + color.getName()));
            for (String position : new String[]{"single", "left", "middle", "right"}) {
                register(event, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID,
                        "block/carpet/table/" + color.getName() + "_" + position));
            }
        }
    }

    private static void register(ModelEvent.RegisterStandalone event, Identifier id) {
        StandaloneModelKey<BlockStateModelPart> key = MODEL_KEYS.computeIfAbsent(id, model -> new StandaloneModelKey<>(model::toString));
        event.register(key, SimpleUnbakedStandaloneModel.simpleModelWrapper(id));
    }

    public static @Nullable BlockStateModelPart getModel(Identifier id) {
        StandaloneModelKey<BlockStateModelPart> key = MODEL_KEYS.get(id);
        if (key == null) {
            return null;
        }
        return Minecraft.getInstance().getModelManager().getStandaloneModel(key);
    }

    public static void copyBreakProgress(BlockEntityRenderState state) {
        ModelFeatureRenderer.CrumblingOverlay breakProgress = state.breakProgress;
        if (breakProgress != null) {
            // 方块实体共用提取阶段的姿态栈，提交时要用各自的破坏投影坐标。
            state.breakProgress = new ModelFeatureRenderer.CrumblingOverlay(breakProgress.progress(), breakProgress.cameraPose().copy());
        }
    }

    public static void submitModel(BlockStateModelPart model, BlockEntityRenderState state, PoseStack poseStack,
                                   SubmitNodeCollector collector, RenderType renderType) {
        collector.submitBlockModel(poseStack, renderType, List.of(model), BlockModelRenderState.EMPTY_TINTS,
                state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        ModelFeatureRenderer.CrumblingOverlay breakProgress = state.breakProgress;
        if (breakProgress == null) {
            return;
        }
        collector.submitCustomGeometry(poseStack, ModelBakery.DESTROY_TYPES.get(breakProgress.progress()), (pose, buffer) -> {
            VertexConsumer decal = new SheetedDecalTextureGenerator(buffer, breakProgress.cameraPose(), 1.0F);
            QuadInstance quadInstance = new QuadInstance();
            for (Direction direction : Direction.values()) {
                for (BakedQuad quad : model.getQuads(direction)) {
                    decal.putBakedQuad(pose, quad, quadInstance);
                }
            }
            for (BakedQuad quad : model.getQuads(null)) {
                decal.putBakedQuad(pose, quad, quadInstance);
            }
        });
    }

    public static void submitItem(ItemStackRenderState item, BlockEntityRenderState state, PoseStack poseStack,
                                  SubmitNodeCollector collector) {
        ModelFeatureRenderer.CrumblingOverlay breakProgress = state.breakProgress;
        if (breakProgress == null || !(collector instanceof SubmitNodeStorage storage)) {
            item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            return;
        }

        // 用物品提交后的各层姿态叠加裂纹，保留物品模型自己的显示变换。
        List<SubmitNodeStorage.ItemSubmit> submissions = storage.order(0).getItemSubmits();
        int first = submissions.size();
        item.submit(poseStack, collector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        for (int i = first; i < submissions.size(); i++) {
            SubmitNodeStorage.ItemSubmit submission = submissions.get(i);
            // 裂纹的提交批次排在物品和汤面之后。
            RenderType crackRenderType = ModelBakery.DESTROY_TYPES.get(breakProgress.progress());
            collector.order(1).submitCustomGeometry(poseStack, crackRenderType, (pose, buffer) -> {
                VertexConsumer decal = new SheetedDecalTextureGenerator(buffer, breakProgress.cameraPose(), 1.0F);
                QuadInstance quadInstance = new QuadInstance();
                for (BakedQuad quad : submission.quads()) {
                    if (quad.materialInfo().itemRenderType().affectsCrumbling()) {
                        decal.putBakedQuad(submission.pose(), quad, quadInstance);
                    }
                }
            });
        }
    }

    private static Identifier handleModelId(Identifier input) {
        String namespace = input.getNamespace();
        String path = input.getPath();
        String substring = path.substring(MODELS.length(), path.length() - JSON.length());
        return Identifier.fromNamespaceAndPath(namespace, substring);
    }
}
