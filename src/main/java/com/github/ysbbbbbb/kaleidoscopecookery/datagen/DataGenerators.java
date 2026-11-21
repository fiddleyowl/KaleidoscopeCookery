package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.model.ItemModelGenerator;
import com.github.ysbbbbbb.kaleidoscopecookery.datagen.tag.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.Util;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.stream.Stream;

@EventBusSubscriber
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        bindDefaultComponents(event);

        event.createProvider(TagBlock::new);
        event.createProvider(TagItem::new);
        event.createProvider(TagPoiType::new);
        event.createProvider(TagEntityType::new);
        event.createProvider(TagDamage::new);

        event.createProvider(DataMapGenerator::new);
        event.createProvider(AdvancementGenerator::new);
        event.createProvider(LootTableGenerator::new);
        event.createProvider(ModRecipeGenerator.Runner::new);
        event.createProvider(GlobalLootModifier::new);
        event.createProvider(ParticleDescriptionGenerator::new);
        event.createProvider(ItemModelGenerator::new);
        event.createProvider(SoundDefinitionsGenerator::new);
    }

    private static void bindDefaultComponents(GatherDataEvent.Client event) {
        // 数据生成阶段独立于世界加载，需手动加载注册表标签来初始化默认组件
        var resources = event.getResourceManager(PackType.SERVER_DATA);
        RegistryAccess.Frozen builtIn = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        var tags = TagLoader.loadTagsForExistingRegistries(resources, builtIn);
        var staticRegistries = TagLoader.buildUpdatedLookups(builtIn, tags);
        var dynamicRegistries = RegistryDataLoader.load(resources, staticRegistries,
                RegistryDataLoader.WORLDGEN_REGISTRIES, Util.backgroundExecutor(), tags).join();
        var registries = HolderLookup.Provider.create(Stream.concat(staticRegistries.stream(), dynamicRegistries.listRegistries()));
        BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.build(registries)
                .forEach(DataComponentInitializers.PendingComponents::apply);
    }
}
