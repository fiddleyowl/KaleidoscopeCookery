package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Sets;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public record FlexPotRecipe(int time, int stirFryCount, Optional<Ingredient> carrier,
                            NonNullList<Ingredient> ingredients, ItemStack result, boolean fillEmptySlots) implements BaseRecipe<SimpleInput> {
    public FlexPotRecipe(int time, int stirFryCount, Optional<Ingredient> carrier,
                         NonNullList<Ingredient> ingredients, ItemStack result) {
        this(time, stirFryCount, carrier, ingredients, result, false);
    }

    public FlexPotRecipe(int time, int stirFryCount, Optional<Ingredient> carrier,
                         List<Ingredient> ingredients, ItemStack result) {
        this(time, stirFryCount, carrier,
                NonNullList.copyOf(ingredients.subList(0, Math.min(ingredients.size(), RECIPES_SIZE))), result, true);
    }

    @Override
    public boolean matches(SimpleInput simpleInput, Level level) {
        NonNullList<ItemStack> merged = NonNullList.withSize(FlexPotRecipe.RECIPES_SIZE, ItemStack.EMPTY);
        Set<Item> record = Sets.newHashSet();
        int index = 0;
        for (int i = 0; i < simpleInput.size(); i++) {
            ItemStack stack = simpleInput.getItem(i);
            Item item = stack.getItem();
            if (!stack.isEmpty() && !record.contains(item)) {
                merged.set(index, stack);
                record.add(item);
                index++;
                if (index >= merged.size()) {
                    break;
                }
            }
        }
        return RecipeMatcher.findMatches(merged, BaseRecipe.fillInputs(ingredients, fillEmptySlots)) != null;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public RecipeSerializer<FlexPotRecipe> getSerializer() {
        return ModRecipes.FLEX_POT_SERIALIZER.get();
    }

    @Override
    public RecipeType<FlexPotRecipe> getType() {
        return ModRecipes.FLEX_POT_RECIPE;
    }
}
