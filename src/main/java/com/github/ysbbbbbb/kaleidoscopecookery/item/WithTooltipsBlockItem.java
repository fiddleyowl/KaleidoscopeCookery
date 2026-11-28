package com.github.ysbbbbbb.kaleidoscopecookery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Consumer;

public class WithTooltipsBlockItem extends BlockItem {
    private static final String TOOLTIP_PREFIX = "tooltip.kaleidoscope_cookery.";

    protected final String key;
    private final List<String> additionalNames;

    public WithTooltipsBlockItem(Block block, Properties properties, String name) {
        this(block, properties, name, new String[0]);
    }

    public WithTooltipsBlockItem(Block block, Properties properties, String name, String... additionalNames) {
        super(block, properties);
        this.key = TOOLTIP_PREFIX + name;
        this.additionalNames = List.of(additionalNames);
    }

    public WithTooltipsBlockItem(Block block, String name) {
        this(block, new Properties(), name);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable(key).withStyle(ChatFormatting.GRAY));
        for (String name : additionalNames) {
            tooltip.accept(Component.translatable(TOOLTIP_PREFIX + name).withStyle(ChatFormatting.GRAY));
        }
    }
}
