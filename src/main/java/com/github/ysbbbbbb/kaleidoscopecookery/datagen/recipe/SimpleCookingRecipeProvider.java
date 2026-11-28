package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.ItemLike;

public class SimpleCookingRecipeProvider extends ModRecipeProvider {
    public SimpleCookingRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        vanillaCookingRecipe(ModItems.RAW_LAMB_CHOPS.get(), ModItems.COOKED_LAMB_CHOPS.get(), 0.35F, consumer);
        vanillaCookingRecipe(ModItems.RAW_COW_OFFAL.get(), ModItems.COOKED_COW_OFFAL.get(), 0.35F, consumer);
        vanillaCookingRecipe(ModItems.RAW_PORK_BELLY.get(), ModItems.COOKED_PORK_BELLY.get(), 0.35F, consumer);
        vanillaCookingRecipe(ModItems.RAW_CUT_SMALL_MEATS.get(), ModItems.COOKED_CUT_SMALL_MEATS.get(), 0.35F, consumer);
        vanillaCookingRecipe(ModItems.RAW_MEATBALL.get(), ModItems.COOKED_MEATBALL.get(), 0.35F, consumer);
        vanillaCookingRecipe(ModItems.STUFFED_DOUGH_FOOD.get(), ModItems.SAMSA.get(), 0.35F, consumer);

        withRecipeOutput(consumer).simpleCookingRecipe("campfire_cooking", CampfireCookingRecipe::new,
                600, ModItems.FRESH_TEA_LEAVES.get(), ModItems.DRIED_TEA_LEAVES.get(), 0.1F);
        Item freshTeaLeaves = ModItems.FRESH_TEA_LEAVES.get();
        Item driedTeaLeaves = ModItems.DRIED_TEA_LEAVES.get();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(freshTeaLeaves), RecipeCategory.FOOD,
                        CookingBookCategory.MISC, driedTeaLeaves, 0.1F, 200)
                .unlockedBy(getHasName(freshTeaLeaves), this.has(freshTeaLeaves))
                .save(consumer, getItemName(driedTeaLeaves) + "_from_smelting");
    }

    public void vanillaCookingRecipe(ItemLike input, ItemLike output, float experience) {
        simpleCookingRecipe("smoking", SmokingRecipe::new, 100, input, output, experience);
        simpleCookingRecipe("campfire_cooking", CampfireCookingRecipe::new, 600, input, output, experience);
        simpleCookingRecipe("smelting", SmeltingRecipe::new, 200, input, output, experience);
    }

    public void vanillaCookingRecipe(ItemLike input, ItemLike output, float experience, RecipeOutput consumer) {
        this.withRecipeOutput(consumer).vanillaCookingRecipe(input, output, experience);
    }

    private SimpleCookingRecipeProvider withRecipeOutput(RecipeOutput consumer) {
        return consumer == this.output
                ? this : new SimpleCookingRecipeProvider(this.registries, consumer);
    }
}
