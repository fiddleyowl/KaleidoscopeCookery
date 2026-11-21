package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class MillstoneRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "millstone";

    private @Nullable Ingredient ingredient;
    private ItemStack result = ItemStack.EMPTY;

    public static MillstoneRecipeBuilder builder() {
        return new MillstoneRecipeBuilder();
    }

    public MillstoneRecipeBuilder setIngredient(ItemLike itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setIngredient(TagKey<Item> itemLike, HolderGetter<Item> items) {
        this.ingredient = Ingredient.of(items.getOrThrow(itemLike));
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemStack stack) {
        this.result = stack;
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemLike itemLike) {
        this.result = new ItemStack(itemLike);
        return this;
    }

    public MillstoneRecipeBuilder setResult(ItemLike itemLike, int count) {
        this.result = new ItemStack(itemLike, count);
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public ResourceKey<Recipe<?>> defaultId() {
        String path = BuiltInRegistries.ITEM.getKey(this.getResult()).getPath();
        Identifier filePath = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, NAME + "/" + path);
        return ResourceKey.create(Registries.RECIPE, filePath);
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        Identifier filePath = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, NAME + "/" + recipeId);
        this.save(output, ResourceKey.create(Registries.RECIPE, filePath));
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
        RandomOutput output = new RandomOutput(this.result, 1.0F);
        MillstoneRecipe recipe = new MillstoneRecipe(Objects.requireNonNull(this.ingredient), List.of(output));
        recipeOutput.accept(id, recipe, null);
    }
}
