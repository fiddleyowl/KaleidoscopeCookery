package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends ModRecipeProvider {
    private final List<ModRecipeProvider> providers = Lists.newArrayList();

    public ModRecipeGenerator(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        providers.add(new ChoppingBoardRecipeProvider(registries, output));
        providers.add(new DecorationRecipeProvider(registries, output));
        providers.add(new FoodBiteRecipeProvider(registries, output));
        providers.add(new PotRecipeProvider(registries, output));
        providers.add(new ShapedRecipeProvider(registries, output));
        providers.add(new ShapelessRecipeProvider(registries, output));
        providers.add(new SimpleCookingRecipeProvider(registries, output));
        providers.add(new SimplePotRecipeProvider(registries, output));
        providers.add(new StockpotRecipeProvider(registries, output));
        providers.add(new MillstoneRecipeProvider(registries, output));
        providers.add(new SteamerRecipeProvider(registries, output));
        providers.add(new TeapotRecipeProvider(registries, output));
        providers.add(new BambooTrayRecipeProvider(registries, output));
    }

    @Override
    public void buildRecipes(RecipeOutput consumer) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(ModItems.DIAMOND_KITCHEN_KNIFE.get()), Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.TOOLS, ModItems.NETHERITE_KITCHEN_KNIFE.get())
                .unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT))
                .save(consumer, "netherite_kitchen_knife_smithing");
        for (ModRecipeProvider provider : providers) {
            provider.buildRecipes(consumer);
        }
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeGenerator(registries, output);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
