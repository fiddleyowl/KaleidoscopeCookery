package com.github.ysbbbbbb.kaleidoscopecookery.client.tooltip;


import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.RecipeItemTooltip;
import com.github.ysbbbbbb.kaleidoscopecookery.item.RecipeItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;

public class ClientRecipeItemTooltip implements ClientTooltipComponent {
    private final RecipeItem.RecipeRecord recipeRecord;
    private final MutableComponent recipeTypeText;
    private final MutableComponent ingredientsText;
    private final MutableComponent outputText;

    public ClientRecipeItemTooltip(RecipeItemTooltip containerTooltip) {
        this.recipeRecord = containerTooltip.record();

        this.recipeTypeText = Component.translatable(recipeRecord.flexRecipe()
                ? "jei.kaleidoscope_cookery.flex_recipe"
                : "jei.kaleidoscope_cookery.strict_recipe");

        Quality quality = containerTooltip.quality();
        if (quality != null) {
            this.recipeTypeText.append(CommonComponents.SPACE).append(quality.getTooltip());
        }

        this.ingredientsText = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.ingredient");
        this.outputText = Component.translatable("tooltip.kaleidoscope_cookery.recipe_item.output");
    }

    @Override
    public int getHeight(Font font) {
        return 40;
    }

    @Override
    public int getWidth(Font font) {
        int ingredientsSize = recipeRecord.input().size() * 12 + font.width(ingredientsText) + 2;
        int outputSize = font.width(outputText) + 20;
        return Math.max(font.width(recipeTypeText), Math.max(ingredientsSize, outputSize));
    }

    @Override
    public void extractImage(Font font, int pX, int pY, int width, int height, GuiGraphicsExtractor guiGraphics) {
        int ingredientsWidth = font.width(ingredientsText);
        int outputWidth = font.width(outputText);
        int color = ARGB.opaque(ChatFormatting.GRAY.getColor());

        guiGraphics.text(font, recipeTypeText, pX, pY + 4, color);

        int ingredientsYOffset = pY + 12;
        guiGraphics.text(font, ingredientsText, pX, ingredientsYOffset + 4, color);

        int i = 0;
        for (ItemStack stack : recipeRecord.input()) {
            int xOffset = pX + ingredientsWidth + i * 12;
            guiGraphics.fakeItem(stack, xOffset, ingredientsYOffset);
            i++;
        }

        int xOffset = pX + outputWidth;
        int yOffset = pY + 24;
        guiGraphics.text(font, outputText, pX, yOffset + 4, color);
        ItemStack stack = recipeRecord.output();
        guiGraphics.fakeItem(stack, xOffset, yOffset);
        guiGraphics.itemDecorations(font, stack, xOffset, yOffset);
    }
}
