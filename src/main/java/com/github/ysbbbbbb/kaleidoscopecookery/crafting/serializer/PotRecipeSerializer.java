package com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.PotRecipe;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Optional;

public class PotRecipeSerializer {
    // 汤锅省略载体时默认使用碗，空数组表示不需要载体。
    public static final Codec<Optional<Ingredient>> CARRIER_CODEC = Codec.either(
            Ingredient.CODEC, Codec.STRING.listOf(0, 0)
    ).xmap(
            either -> either.left(),
            carrier -> {
                if (carrier.isPresent()) {
                    return Either.left(carrier.get());
                }
                return Either.right(List.of());
            }
    );
    public static final MapCodec<PotRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.optionalFieldOf("time", 200).forGetter(PotRecipe::time),
                    Codec.INT.optionalFieldOf("stir_fry_count", 3).forGetter(PotRecipe::stirFryCount),
                    CARRIER_CODEC.optionalFieldOf("carrier", Optional.empty()).forGetter(PotRecipe::carrier),
                    Ingredient.CODEC.listOf().fieldOf("ingredients").xmap(
                            list -> list,
                            list -> list.stream().filter(i -> !i.isEmpty()).toList()
                    ).forGetter(recipe -> recipe.ingredients().stream().toList()),
                    ItemStack.CODEC.fieldOf("result").forGetter(PotRecipe::result)
            ).apply(instance, PotRecipe::new)
    );

    // 同步完整配方给客户端时使用。
    public static final StreamCodec<RegistryFriendlyByteBuf, PotRecipe> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PotRecipe::time,
            ByteBufCodecs.INT, PotRecipe::stirFryCount,
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC, PotRecipe::carrier,
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), PotRecipe::ingredients,
            ItemStack.STREAM_CODEC, PotRecipe::result,
            PotRecipe::new);
}
