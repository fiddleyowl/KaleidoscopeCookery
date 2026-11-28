package com.github.ysbbbbbb.kaleidoscopecookery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class WithTooltipsBlockItem extends BlockItem {
    protected final String key;

    public WithTooltipsBlockItem(Block block, Properties properties, String name) {
        super(block, properties);
        this.key = "tooltip.kaleidoscope_cookery." + name;
    }

    public WithTooltipsBlockItem(Block block, String name) {
        this(block, new Properties(), name);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable(key).withStyle(ChatFormatting.GRAY));
    }
}
