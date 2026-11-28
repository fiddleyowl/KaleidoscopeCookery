package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
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

public record PotRecipe(int time, int stirFryCount, Optional<Ingredient> carrier,
                        NonNullList<Ingredient> ingredients, ItemStack result, boolean fillEmptySlots) implements BaseRecipe<SimpleInput> {
    public PotRecipe(int time, int stirFryCount, Optional<Ingredient> carrier,
                     NonNullList<Ingredient> ingredients, ItemStack result) {
        this(time, stirFryCount, carrier, ingredients, result, false);
    }

    public PotRecipe(int time, int stirFryCount, Optional<Ingredient> carrier,
                     List<Ingredient> ingredients, ItemStack result) {
        this(time, stirFryCount, carrier,
                NonNullList.copyOf(ingredients.subList(0, Math.min(ingredients.size(), RECIPES_SIZE))), result, true);
    }

    @Override
    public boolean matches(SimpleInput simpleInput, Level level) {
        return RecipeMatcher.findMatches(simpleInput.getInputs(), BaseRecipe.fillInputs(ingredients, fillEmptySlots)) != null;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public RecipeSerializer<PotRecipe> getSerializer() {
        return ModRecipes.POT_SERIALIZER.get();
    }

    @Override
    public RecipeType<PotRecipe> getType() {
        return ModRecipes.POT_RECIPE;
    }
}
