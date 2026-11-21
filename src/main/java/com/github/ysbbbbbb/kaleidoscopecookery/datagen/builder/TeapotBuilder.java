package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.TeapotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.TeapotRecipeSerializer;
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
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class TeapotBuilder implements RecipeBuilder {
    private static final String NAME = "teapot";

    private final HolderGetter<Item> items;

    private Identifier teaFluid = TeapotRecipeSerializer.EMPTY_TEA_FLUID;
    private @Nullable Ingredient ingredient;
    private int ingredientCount = TeapotRecipeSerializer.DEFAULT_INGREDIENT_COUNT;
    private int time = TeapotRecipeSerializer.DEFAULT_TIME;
    private ItemStack result = ItemStack.EMPTY;

    public TeapotBuilder(HolderGetter<Item> items) {
        this.items = items;
    }

    public static TeapotBuilder builder(HolderGetter<Item> items) {
        return new TeapotBuilder(items);
    }

    public TeapotBuilder setTeaFluid(Identifier baseTeaFluid) {
        this.teaFluid = baseTeaFluid;
        return this;
    }

    public TeapotBuilder setTeaFluid(Fluid fluid) {
        this.teaFluid = BuiltInRegistries.FLUID.getKey((fluid));
        return this;
    }

    @SuppressWarnings("all")
    public TeapotBuilder setIngredient(Object ingredient) {
        if (ingredient instanceof ItemLike itemLike) {
            this.ingredient = Ingredient.of(itemLike);
        } else if (ingredient instanceof ItemStack stack) {
            this.ingredient = Ingredient.of(stack.getItem());
        } else if (ingredient instanceof TagKey tagKey) {
            this.ingredient = Ingredient.of(this.items.getOrThrow(tagKey));
        } else if (ingredient instanceof Ingredient ingredientObj) {
            this.ingredient = ingredientObj;
        } else if (ingredient instanceof DeferredItem<?>) {
            this.ingredient = Ingredient.of(((DeferredItem<?>) ingredient).get());
        } else {
            throw new IllegalArgumentException("Unsupported ingredient type: " + ingredient.getClass().getName());
        }
        return this;
    }

    public TeapotBuilder setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
        return this;
    }

    public TeapotBuilder setTime(int time) {
        this.time = time;
        return this;
    }

    public TeapotBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public TeapotBuilder setResult(ItemLike result) {
        this.result = new ItemStack(result);
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
        recipeOutput.accept(id, new TeapotRecipe(this.teaFluid, Objects.requireNonNull(this.ingredient), this.ingredientCount, this.time, this.result), null);
    }
}