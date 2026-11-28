package com.github.ysbbbbbb.kaleidoscopecookery.compat.kubejs.recipe;

import com.mojang.datafixers.util.Either;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.IntBounds;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;

import java.util.List;
import java.util.Optional;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.PotRecipeSerializer.CARRIER_CODEC;

public interface PotRecipeSchema {
    RecipeKey<ItemStack> OUTPUT = ItemStackComponent.ITEM_STACK.outputKey("result");
    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.asListOrSelf()
            .inputKey("ingredients");
    RecipeComponent<Either<Ingredient, List<Ingredient>>> CARRIER_COMPONENT = IngredientComponent.OPTIONAL_INGREDIENT
            .or(IngredientComponent.INGREDIENT.asList().withBounds(IntBounds.OPTIONAL))
            .withCodec(CARRIER_CODEC.xmap(
                    carrier -> {
                        if (carrier.isPresent()) {
                            return Either.left(carrier.get());
                        }
                        return Either.right(List.of());
                    },
                    carrier -> {
                        Optional<Ingredient> ingredient = carrier.left();
                        if (ingredient.isPresent()) {
                            return ingredient;
                        }

                        List<Ingredient> alternatives = carrier.right().orElseThrow();
                        if (alternatives.isEmpty()) {
                            return Optional.empty();
                        }
                        return Optional.of(CompoundIngredient.of(alternatives.toArray(Ingredient[]::new)));
                    }));
    RecipeKey<Either<Ingredient, List<Ingredient>>> CARRIER = CARRIER_COMPONENT.inputKey("carrier").defaultOptional();
    RecipeKey<Integer> TIME = NumberComponent.INT.otherKey("time").optional(200);
    RecipeKey<Integer> STIR_FRY_COUNT = NumberComponent.INT.otherKey("stir_fry_count").optional(3);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INGREDIENTS, CARRIER, TIME, STIR_FRY_COUNT);
}
