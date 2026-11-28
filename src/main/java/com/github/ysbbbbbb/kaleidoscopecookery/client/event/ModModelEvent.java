package com.github.ysbbbbbb.kaleidoscopecookery.client.event;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.joml.Matrix4f;

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

        // 额外添加的模型注册
        // 蜂蜜瓶，用来替换锅内渲染
        register(event, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/honey"));
        // 鸡蛋，用来替换锅内渲染
        register(event, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/egg"));
        // 油
        register(event, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/oil_in_millstone"));

        // 重置缓存
        ItemRenderReplacer.resetCache();
    }

    private static void register(ModelEvent.RegisterStandalone event, Identifier id) {
        StandaloneModelKey<BlockStateModelPart> key = MODEL_KEYS.computeIfAbsent(id, model -> new StandaloneModelKey<>(model::toString));
        event.register(key, SimpleUnbakedStandaloneModel.simpleModelWrapper(id));
    }

    public static void updateModel(BlockModelRenderState state, Identifier id) {
        state.clear();
        StandaloneModelKey<BlockStateModelPart> key = MODEL_KEYS.get(id);
        if (key == null) {
            return;
        }
        BlockStateModelPart model = Minecraft.getInstance().getModelManager().getStandaloneModel(key);
        if (model != null) {
            state.setupModel(new Matrix4f(), false).add(model);
        }
    }

    private static Identifier handleModelId(Identifier input) {
        String namespace = input.getNamespace();
        String path = input.getPath();
        String substring = path.substring(MODELS.length(), path.length() - JSON.length());
        return Identifier.fromNamespaceAndPath(namespace, substring);
    }
}
