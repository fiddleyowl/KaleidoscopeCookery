package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.List;

public class RiceBowlRecipe extends CustomRecipe {
    private final CraftingBookCategory category;
    private final Ingredient ingredient;
    private final ItemStack result;

    public RiceBowlRecipe(CraftingBookCategory category, Ingredient ingredient, ItemStack result) {
        this.category = category;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        List<ItemStack> items = container.items();

        // 有且只能有一个物品匹配 ingredient
        if (items.stream().filter(ingredient).count() != 1) {
            return false;
        }

        // 有且只能有一个物品匹配 COOKED_RICE
        return items.stream().filter(stack -> stack.is(TagCommon.COOKED_RICE)).count() == 1;
    }

    @Override
    public ItemStack assemble(CraftingInput container) {
        ItemStack assembled = result.copy();
        copyBestQuality(container, assembled);
        return assembled;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    public List<Ingredient> getIngredients(HolderLookup.Provider registries) {
        Ingredient cookedRice = registries.get(TagCommon.COOKED_RICE)
                .filter(tag -> tag.size() > 0)
                .map(Ingredient::of)
                .orElseGet(() -> DataComponentIngredient.of(DataComponents.CUSTOM_NAME,
                        Component.literal("Empty Tag: " + TagCommon.COOKED_RICE.location()), Items.BARRIER));
        return List.of(this.ingredient, cookedRice);
    }

    public ItemStack getResult() {
        return result;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public RecipeSerializer<RiceBowlRecipe> getSerializer() {
        return ModRecipes.RICE_BOWL_SERIALIZER.get();
    }

    private static void copyBestQuality(CraftingInput container, ItemStack result) {
        boolean hasQuality = false;
        Quality bestQuality = Quality.POOR;

        for (int i = 0; i < container.size(); i++) {
            ItemStack ingredient = container.getItem(i);
            if (!QualityUtils.hasQuality(ingredient)) {
                continue;
            }

            Quality quality = QualityUtils.getQuality(ingredient);
            if (!hasQuality || quality.getScore() > bestQuality.getScore()) {
                bestQuality = quality;
                hasQuality = true;
            }
        }

        if (hasQuality) {
            QualityUtils.setQuality(result, bestQuality);
        }
    }
}
