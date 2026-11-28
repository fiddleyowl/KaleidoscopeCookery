package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.apache.commons.lang3.StringUtils;

public class SteamerRecipe extends SingleItemRecipe {
    private final int cookTick;

    public SteamerRecipe(Ingredient ingredient, ItemStack result, int cookTick) {
        super(new Recipe.CommonInfo(true), ingredient, ItemStackTemplate.fromNonEmptyStack(result));
        this.cookTick = Math.max(cookTick, 1);
    }

    @Override
    public String group() {
        return StringUtils.EMPTY;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<SteamerRecipe> getSerializer() {
        return ModRecipes.STEAMER_SERIALIZER.get();
    }

    @Override
    public RecipeType<SteamerRecipe> getType() {
        return ModRecipes.STEAMER_RECIPE;
    }

    public Ingredient getIngredient() {
        return this.input();
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.input());
        return ingredients;
    }

    public ItemStack getResult() {
        ItemStackTemplate template = this.result();
        // 蒸笼产物沿用配方 Codec 读取的数量。
        return new ItemStack(template.item(), template.count(), template.components());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input) {
        return this.getResult();
    }

    public int getCookTick() {
        return cookTick;
    }
}
