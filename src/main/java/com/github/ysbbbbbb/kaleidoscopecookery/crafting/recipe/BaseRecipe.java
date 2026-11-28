package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface BaseRecipe<C extends RecipeInput> extends Recipe<C> {
    int RECIPES_SIZE = 9;

    static List<? extends Predicate<ItemStack>> fillInputs(List<Ingredient> inputs, boolean fillEmptySlots) {
        if (!fillEmptySlots) {
            return inputs;
        }
        List<Predicate<ItemStack>> filled = new ArrayList<>(Math.max(RECIPES_SIZE, inputs.size()));
        filled.addAll(inputs);
        // List 构造器补齐九槽，直接传入 NonNullList 时按原料数匹配。
        for (int i = inputs.size(); i < RECIPES_SIZE; i++) {
            filled.add(ItemStack::isEmpty);
        }
        return filled;
    }

    ItemStack result();

    @Override
    default ItemStack assemble(C container) {
        return result().copy();
    }

    @Override
    default boolean isSpecial() {
        return true;
    }

    @Override
    default boolean showNotification() {
        return true;
    }

    @Override
    default String group() {
        return "";
    }

    @Override
    default PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    default RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
