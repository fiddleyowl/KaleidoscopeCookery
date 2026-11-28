package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.List;
import java.util.Optional;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.DEFAULT_SOUP_BASE;

public record StockpotRecipe(NonNullList<Ingredient> ingredients,
                             Identifier soupBase, ItemStack result, int time,
                             Optional<Ingredient> carrier, StockpotVisuals visuals,
                             boolean syrup, boolean fillEmptySlots) implements BaseRecipe<StockpotInput> {

    public StockpotRecipe(NonNullList<Ingredient> ingredients,
                          Identifier soupBase, ItemStack result,
                          int time, Optional<Ingredient> carrier, StockpotVisuals visuals,
                          boolean syrup) {
        this(ingredients, soupBase, result, time, carrier, visuals, syrup, false);
    }

    public StockpotRecipe(List<Ingredient> ingredients,
                          Identifier soupBase, ItemStack result,
                          int time, Optional<Ingredient> carrier, StockpotVisuals visuals,
                          boolean syrup
    ) {
        this(NonNullList.copyOf(ingredients.subList(0, Math.min(ingredients.size(), RECIPES_SIZE))),
                soupBase, result, time, carrier, visuals, syrup, true);
    }

    public StockpotRecipe(NonNullList<Ingredient> ingredients,
                          ItemStack result, int time, ItemStack container) {
        this(ingredients, DEFAULT_SOUP_BASE, result, time,
                container.isEmpty()
                        ? Optional.empty()
                        : Optional.of(Ingredient.of(container.getItem())),
                StockpotVisuals.DEFAULT, false
        );
    }

    public Identifier cookingTexture() {
        return this.visuals.cookingTexture();
    }

    public Identifier finishedTexture() {
        return this.visuals.finishedTexture();
    }

    public int cookingBubbleColor() {
        return this.visuals.cookingBubbleColor();
    }

    public int finishedBubbleColor() {
        return this.visuals.finishedBubbleColor();
    }

    @Override
    public boolean matches(StockpotInput container, Level level) {
        return container.getSoupBase().equals(this.soupBase)
               && RecipeMatcher.findMatches(container.getInputs(), BaseRecipe.fillInputs(ingredients, fillEmptySlots)) != null;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public RecipeSerializer<StockpotRecipe> getSerializer() {
        return ModRecipes.STOCKPOT_SERIALIZER.get();
    }

    @Override
    public RecipeType<StockpotRecipe> getType() {
        return ModRecipes.STOCKPOT_RECIPE;
    }
}
