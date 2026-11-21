package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;

public abstract class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.buildRecipes(this.output);
    }

    public void buildRecipes(RecipeOutput consumer) {
    }

    public Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, path);
    }

    protected ResourceKey<Recipe<?>> modRecipeKey(String path) {
        return ResourceKey.create(Registries.RECIPE, this.modLoc(path));
    }

    public String getRecipeIdWithCount(ItemLike itemLike, int count) {
        return getItemName(itemLike) + "_" + count;
    }

    public ItemLike[] getItemsWithCount(ItemLike itemLike, int count) {
        ItemLike[] items = new ItemLike[count];
        Arrays.fill(items, itemLike);
        return items;
    }

    public TagKey<Item>[] getItemsWithCount(TagKey<Item> itemLike, int count) {
        TagKey<Item>[] items = new TagKey[count];
        Arrays.fill(items, itemLike);
        return items;
    }
}
