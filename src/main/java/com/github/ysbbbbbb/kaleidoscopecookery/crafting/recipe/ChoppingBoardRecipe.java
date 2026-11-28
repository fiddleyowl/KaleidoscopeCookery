package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;

public class ChoppingBoardRecipe implements BaseRecipe<SingleRecipeInput> {
    private final Ingredient ingredient;
    private final int cutCount;
    private final Identifier modelId;
    private final List<ItemStack> results;

    public ChoppingBoardRecipe(Ingredient ingredient, List<ItemStack> results, int cutCount, Identifier modelId) {
        this.ingredient = ingredient;
        this.cutCount = Math.max(cutCount, 1);
        this.modelId = modelId;
        this.results = results.stream().map(ItemStack::copy).toList();
    }

    @Override
    public boolean matches(SingleRecipeInput inv, Level level) {
        return this.ingredient.test(inv.getItem(0));
    }

    @Override
    public RecipeSerializer<ChoppingBoardRecipe> getSerializer() {
        return ModRecipes.CHOPPING_BOARD_SERIALIZER.get();
    }

    @Override
    public RecipeType<ChoppingBoardRecipe> getType() {
        return ModRecipes.CHOPPING_BOARD_RECIPE;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient);
        return ingredients;
    }

    public ItemStack getResult() {
        return this.result();
    }

    @Override
    public ItemStack result() {
        return this.results.isEmpty() ? ItemStack.EMPTY : this.results.getFirst();
    }

    public List<ItemStack> getResults() {
        return this.results;
    }

    public int getCutCount() {
        return cutCount;
    }

    public Identifier getModelId() {
        return modelId;
    }
}
