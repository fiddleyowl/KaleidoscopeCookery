package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.BambooTrayRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.BambooTrayRecipeSerializer;
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

import java.util.Objects;

public class BambooTrayRecipeBuilder implements RecipeBuilder {
    private static final String NAME = "bamboo_tray";

    private final BambooTrayRecipe.Subtype subtype;

    private @Nullable Ingredient ingredient;
    private ItemStack result = ItemStack.EMPTY;
    private int duration = BambooTrayRecipeSerializer.DEFAULT_DURATION;

    private BambooTrayRecipeBuilder(BambooTrayRecipe.Subtype subtype) {
        this.subtype = subtype;
    }

    public static BambooTrayRecipeBuilder wetting() {
        return new BambooTrayRecipeBuilder(BambooTrayRecipe.Subtype.WETTING);
    }

    public static BambooTrayRecipeBuilder drying() {
        return new BambooTrayRecipeBuilder(BambooTrayRecipe.Subtype.DRYING);
    }

    public BambooTrayRecipeBuilder setIngredient(ItemLike itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public BambooTrayRecipeBuilder setIngredient(TagKey<Item> tag, HolderGetter<Item> items) {
        this.ingredient = Ingredient.of(items.getOrThrow(tag));
        return this;
    }

    public BambooTrayRecipeBuilder setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    public BambooTrayRecipeBuilder setResult(ItemLike itemLike) {
        this.result = new ItemStack(itemLike);
        return this;
    }

    public BambooTrayRecipeBuilder setResult(ItemLike itemLike, int count) {
        this.result = new ItemStack(itemLike, count);
        return this;
    }

    public BambooTrayRecipeBuilder setResult(ItemStack result) {
        this.result = result;
        return this;
    }

    public BambooTrayRecipeBuilder setDuration(int duration) {
        this.duration = Math.max(duration, 1);
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
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
        Identifier id = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID,
                NAME + "/" + this.subtype.getSerializedName() + "/" + path);
        return ResourceKey.create(Registries.RECIPE, id);
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        Identifier id = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID,
                NAME + "/" + this.subtype.getSerializedName() + "/" + recipeId);
        this.save(output, ResourceKey.create(Registries.RECIPE, id));
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        output.accept(id, new BambooTrayRecipe(Objects.requireNonNull(this.ingredient), this.result, this.subtype, this.duration), null);
    }
}
