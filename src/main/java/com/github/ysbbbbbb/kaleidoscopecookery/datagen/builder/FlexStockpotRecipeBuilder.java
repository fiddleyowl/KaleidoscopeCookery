package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.FlexStockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer;
import com.google.common.collect.Lists;
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
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FlexStockpotRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "flex_stockpot";

    private final HolderGetter<Item> items;
    private List<Ingredient> ingredients = Lists.newArrayList();
    private ItemStack result = ItemStack.EMPTY;
    private int time = StockpotRecipeSerializer.DEFAULT_TIME;
    private Optional<Ingredient> carrier = Optional.of(StockpotRecipeSerializer.DEFAULT_CARRIER);
    private Identifier soupBase = StockpotRecipeSerializer.DEFAULT_SOUP_BASE;
    private StockpotVisuals visuals = StockpotVisuals.DEFAULT;

    public FlexStockpotRecipeBuilder(HolderGetter<Item> items) {
        this.items = items;
    }

    public static FlexStockpotRecipeBuilder builder(HolderGetter<Item> items) {
        return new FlexStockpotRecipeBuilder(items);
    }

    @SuppressWarnings("all")
    public FlexStockpotRecipeBuilder addInput(Object... ingredients) {
        for (Object ingredient : ingredients) {
            if (ingredient instanceof ItemLike itemLike) {
                this.ingredients.add(Ingredient.of(itemLike));
            } else if (ingredient instanceof ItemStack stack) {
                this.ingredients.add(Ingredient.of(stack.getItem()));
            } else if (ingredient instanceof TagKey tagKey) {
                this.ingredients.add(Ingredient.of(this.items.getOrThrow(tagKey)));
            } else if (ingredient instanceof Ingredient ingredientObj) {
                this.ingredients.add(ingredientObj);
            } else if (ingredient instanceof DeferredItem) {
                this.ingredients.add(Ingredient.of(((DeferredItem<Item>) ingredient).get()));
            }
        }
        return this;
    }

    public FlexStockpotRecipeBuilder setSoupBase(Identifier soupBase) {
        this.soupBase = soupBase;
        return this;
    }

    public FlexStockpotRecipeBuilder setResult(Item result) {
        this.result = new ItemStack(result, 3);
        return this;
    }

    public FlexStockpotRecipeBuilder setResult(Item result, int count) {
        return this.setResult(new ItemStack(result, count));
    }

    public FlexStockpotRecipeBuilder setResult(Identifier result) {
        this.result = new ItemStack(BuiltInRegistries.ITEM.getValue(result));
        return this;
    }

    public FlexStockpotRecipeBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public FlexStockpotRecipeBuilder setTime(int time) {
        this.time = time;
        return this;
    }

    public FlexStockpotRecipeBuilder setCarrier(ItemLike carrier) {
        this.carrier = Optional.of(Ingredient.of(carrier));
        return this;
    }

    public FlexStockpotRecipeBuilder setCookingTexture(Identifier cookingTexture) {
        this.visuals = new StockpotVisuals(cookingTexture, this.visuals.finishedTexture(),
                this.visuals.cookingBubbleColor(), this.visuals.finishedBubbleColor());
        return this;
    }

    public FlexStockpotRecipeBuilder setFinishedTexture(Identifier finishedTexture) {
        this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), finishedTexture,
                this.visuals.cookingBubbleColor(), this.visuals.finishedBubbleColor());
        return this;
    }

    public FlexStockpotRecipeBuilder setCookingBubbleColor(int cookingBubbleColor) {
        this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(),
                cookingBubbleColor, this.visuals.finishedBubbleColor());
        return this;
    }

    public FlexStockpotRecipeBuilder setFinishedBubbleColor(int finishedBubbleColor) {
        this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(),
                this.visuals.cookingBubbleColor(), finishedBubbleColor);
        return this;
    }

    public FlexStockpotRecipeBuilder setBubbleColors(int cookingBubbleColor, int finishedBubbleColor) {
        this.visuals = new StockpotVisuals(this.visuals.cookingTexture(), this.visuals.finishedTexture(),
                cookingBubbleColor, finishedBubbleColor);
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
        Identifier filePath = Identifier.fromNamespaceAndPath(
                KaleidoscopeCookery.MOD_ID, NAME + "/" + path
        );
        return ResourceKey.create(Registries.RECIPE, filePath);
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        Identifier filePath = Identifier.fromNamespaceAndPath(
                KaleidoscopeCookery.MOD_ID, NAME + "/" + recipeId
        );
        this.save(output, ResourceKey.create(Registries.RECIPE, filePath));
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
        FlexStockpotRecipe recipe = new FlexStockpotRecipe(
                this.ingredients, this.soupBase, this.result,
                this.time, this.carrier, this.visuals
        );
        recipeOutput.accept(id, recipe, null);
    }
}
