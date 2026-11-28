package com.github.ysbbbbbb.kaleidoscopecookery.client.resources;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public record ItemRenderReplacer(Map<Identifier, Identifier> pot,
                                 Map<Identifier, Identifier> stockpotCooking,
                                 Map<Identifier, Identifier> stockpotFinished,
                                 Map<Identifier, Identifier> millstone,
                                 Map<Identifier, Identifier> steamer) {
    public static final Codec<ItemRenderReplacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Identifier.CODEC, Identifier.CODEC).fieldOf("pot").forGetter(ItemRenderReplacer::pot),
            Codec.unboundedMap(Identifier.CODEC, Identifier.CODEC).fieldOf("stockpot_cooking").forGetter(ItemRenderReplacer::stockpotCooking),
            Codec.unboundedMap(Identifier.CODEC, Identifier.CODEC).fieldOf("stockpot_finished").forGetter(ItemRenderReplacer::stockpotFinished),
            Codec.unboundedMap(Identifier.CODEC, Identifier.CODEC).fieldOf("millstone").forGetter(ItemRenderReplacer::millstone),
            Codec.unboundedMap(Identifier.CODEC, Identifier.CODEC).fieldOf("steamer").forGetter(ItemRenderReplacer::steamer)
    ).apply(instance, ItemRenderReplacer::new));

    public ItemRenderReplacer() {
        this(Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap());
    }

    public static ItemStack display(ItemStack stack, Map<Identifier, Identifier> models) {
        if (stack.isEmpty()) {
            return stack;
        }
        Identifier key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        Identifier model = models.get(key);
        if (model == null) {
            return stack;
        }
        // 只在渲染用的副本上替换物品模型。
        ItemStack display = stack.copy();
        display.set(DataComponents.ITEM_MODEL, model);
        return display;
    }

    public void addAll(ItemRenderReplacer other) {
        this.pot.putAll(other.pot);
        this.stockpotCooking.putAll(other.stockpotCooking);
        this.stockpotFinished.putAll(other.stockpotFinished);
        this.millstone.putAll(other.millstone);
        this.steamer.putAll(other.steamer);
    }
}
