package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.apache.commons.lang3.StringUtils;

import java.util.function.IntFunction;

public class BambooTrayRecipe extends SingleItemRecipe {
    private final Subtype subtype;
    private final int duration;

    public BambooTrayRecipe(Ingredient ingredient, ItemStack result, Subtype subtype, int duration) {
        super(new Recipe.CommonInfo(true), ingredient, ItemStackTemplate.fromNonEmptyStack(result));
        this.subtype = subtype;
        this.duration = Math.max(duration, 1);
    }

    @Override
    public String group() {
        return StringUtils.EMPTY;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<BambooTrayRecipe> getSerializer() {
        return ModRecipes.BAMBOO_TRAY_SERIALIZER.get();
    }

    @Override
    public RecipeType<BambooTrayRecipe> getType() {
        return ModRecipes.BAMBOO_TRAY_RECIPE;
    }

    public Ingredient getIngredient() {
        return this.input();
    }

    public ItemStack getResult() {
        ItemStackTemplate template = this.result();
        // 竹筛在计算实际产量后才限制堆叠数量。
        return new ItemStack(template.item(), template.count(), template.components());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input) {
        return this.getResult();
    }

    public Subtype getSubtype() {
        return subtype;
    }

    public int getDuration() {
        return duration;
    }

    public enum Subtype implements StringRepresentable {
        WETTING(0, "wetting"),
        DRYING(1, "drying");

        private static final IntFunction<Subtype> BY_ID = ByIdMap.continuous(
                Subtype::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<Subtype> CODEC = StringRepresentable.fromEnum(Subtype::values);
        public static final StreamCodec<ByteBuf, Subtype> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Subtype::getId);

        private final int id;
        private final String serializedName;

        Subtype(int id, String serializedName) {
            this.id = id;
            this.serializedName = serializedName;
        }

        private int getId() {
            return this.id;
        }

        @Override
        public String getSerializedName() {
            return serializedName;
        }

        public static Subtype fromSerializedName(String name) {
            for (Subtype subtype : values()) {
                if (subtype.serializedName.equalsIgnoreCase(name)) {
                    return subtype;
                }
            }
            throw new IllegalArgumentException("Unknown bamboo tray recipe subtype: " + name);
        }
    }
}
