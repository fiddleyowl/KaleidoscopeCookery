package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.output.RandomOutput;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.MillstoneRecipe;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MillstoneRecipeSerializer {
    private static final MapCodec<List<RandomOutput>> RESULTS_MAP_CODEC = new MapCodec<>() {
        @Override
        public <T> DataResult<List<RandomOutput>> decode(DynamicOps<T> ops, MapLike<T> input) {
            T resultsNode = input.get("results");
            if (resultsNode != null) {
                return RandomOutput.CODEC.listOf().parse(ops, resultsNode);
            }
            T resultNode = input.get("result");
            if (resultNode != null) {
                return RandomOutput.CODEC.parse(ops, resultNode).map(List::of);
            }
            return DataResult.error(() -> "Missing both 'results' and 'result' fields!");
        }

        @Override
        public <T> RecordBuilder<T> encode(List<RandomOutput> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
            if (input.size() == 1) {
                return prefix.add("result", RandomOutput.CODEC.encodeStart(ops, input.getFirst()));
            } else {
                return prefix.add("results", RandomOutput.CODEC.listOf().encodeStart(ops, input));
            }
        }

        @Override
        public <T> Stream<T> keys(DynamicOps<T> ops) {
            return Stream.of(ops.createString("results"), ops.createString("result"));
        }
    };

    public static final MapCodec<MillstoneRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient().orElseThrow()),
                    RESULTS_MAP_CODEC.forGetter(MillstoneRecipe::results)
            ).apply(instance, MillstoneRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MillstoneRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient().orElseThrow(),
            RandomOutput.STREAM_CODEC.apply(ByteBufCodecs.list(4)), MillstoneRecipe::results,
            MillstoneRecipe::new
    );

    public static final Identifier EMPTY_ID = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "millstone/empty");

    public static RecipeHolder<MillstoneRecipe> getEmptyRecipe() {
        MillstoneRecipe recipe = new MillstoneRecipe(Optional.empty(), NonNullList.withSize(4, RandomOutput.EMPTY));
        return new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, EMPTY_ID), recipe);
    }
}
