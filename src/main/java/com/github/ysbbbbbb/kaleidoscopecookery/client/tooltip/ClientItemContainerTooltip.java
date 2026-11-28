package com.github.ysbbbbbb.kaleidoscopecookery.client.tooltip;


import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nullable;

public class ClientItemContainerTooltip implements ClientTooltipComponent {
    private final NonNullList<ItemStack> items = NonNullList.create();
    private @Nullable MutableComponent emptyTip = null;

    public ClientItemContainerTooltip(ItemContainerTooltip containerTooltip) {
        IItemHandler handler = containerTooltip.handler();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                this.items.add(stack);
            }
        }
        if (items.isEmpty()) {
            this.emptyTip = Component.translatable("tooltip.kaleidoscope_cookery.item_container.empty");
        }
    }

    @Override
    public int getHeight(Font font) {
        if (emptyTip != null) {
            return 10;
        }
        int row = (items.size() - 1) / 8 + 1;
        return 20 * row;
    }

    @Override
    public int getWidth(Font font) {
        if (emptyTip != null) {
            return font.width(emptyTip);
        }
        int maxInRow = Math.min(items.size(), 8);
        return maxInRow * 20;
    }

    @Override
    public void extractImage(Font font, int pX, int pY, int width, int height, GuiGraphicsExtractor guiGraphics) {
        if (emptyTip != null) {
            guiGraphics.text(font, emptyTip, pX, pY, ARGB.opaque(ChatFormatting.GRAY.getColor()));
        } else {
            int i = 0;
            for (ItemStack stack : this.items) {
                int xOffset = pX + (i % 8) * 20;
                int yOffset = pY + (i / 8) * 20;
                guiGraphics.fakeItem(stack, xOffset, yOffset);
                guiGraphics.itemDecorations(font, stack, xOffset, yOffset);
                i++;
            }
        }
    }
}
