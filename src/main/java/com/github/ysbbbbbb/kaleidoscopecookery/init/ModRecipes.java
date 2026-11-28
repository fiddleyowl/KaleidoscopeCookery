package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber
public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, KaleidoscopeCookery.MOD_ID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PotRecipe>> POT_SERIALIZER =
            RECIPE_SERIALIZERS.register("pot", () -> new RecipeSerializer<>(
                    PotRecipeSerializer.CODEC, PotRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FlexPotRecipe>> FLEX_POT_SERIALIZER =
            RECIPE_SERIALIZERS.register("flex_pot", () -> new RecipeSerializer<>(
                    FlexPotRecipeSerializer.CODEC, FlexPotRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ChoppingBoardRecipe>> CHOPPING_BOARD_SERIALIZER =
            RECIPE_SERIALIZERS.register("chopping_board", () -> new RecipeSerializer<>(
                    ChoppingBoardRecipeSerializer.CODEC, ChoppingBoardRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<StockpotRecipe>> STOCKPOT_SERIALIZER =
            RECIPE_SERIALIZERS.register("stockpot", () -> new RecipeSerializer<>(
                    StockpotRecipeSerializer.CODEC, StockpotRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FlexStockpotRecipe>> FLEX_STOCKPOT_SERIALIZER =
            RECIPE_SERIALIZERS.register("flex_stockpot", () -> new RecipeSerializer<>(
                    FlexStockpotRecipeSerializer.CODEC, FlexStockpotRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<MillstoneRecipe>> MILLSTONE_SERIALIZER =
            RECIPE_SERIALIZERS.register("millstone", () -> new RecipeSerializer<>(
                    MillstoneRecipeSerializer.CODEC, MillstoneRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<SteamerRecipe>> STEAMER_SERIALIZER =
            RECIPE_SERIALIZERS.register("steamer", () -> new RecipeSerializer<>(
                    SteamerRecipeSerializer.CODEC, SteamerRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TeapotRecipe>> TEAPOT_SERIALIZER =
            RECIPE_SERIALIZERS.register("teapot", () -> new RecipeSerializer<>(
                    TeapotRecipeSerializer.CODEC, TeapotRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<RiceBowlRecipe>> RICE_BOWL_SERIALIZER =
            RECIPE_SERIALIZERS.register("rice_bowl", () -> new RecipeSerializer<>(
                    RiceBowlRecipeSerializer.CODEC, RiceBowlRecipeSerializer.STREAM_CODEC));
    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<BambooTrayRecipe>> BAMBOO_TRAY_SERIALIZER =
            RECIPE_SERIALIZERS.register("bamboo_tray", () -> new RecipeSerializer<>(
                    BambooTrayRecipeSerializer.CODEC, BambooTrayRecipeSerializer.STREAM_CODEC));

    public static RecipeType<PotRecipe> POT_RECIPE;
    public static RecipeType<FlexPotRecipe> FLEX_POT_RECIPE;
    public static RecipeType<ChoppingBoardRecipe> CHOPPING_BOARD_RECIPE;
    public static RecipeType<StockpotRecipe> STOCKPOT_RECIPE;
    public static RecipeType<FlexStockpotRecipe> FLEX_STOCKPOT_RECIPE;
    public static RecipeType<MillstoneRecipe> MILLSTONE_RECIPE;
    public static RecipeType<SteamerRecipe> STEAMER_RECIPE;
    public static RecipeType<TeapotRecipe> TEAPOT_RECIPE;
    public static RecipeType<BambooTrayRecipe> BAMBOO_TRAY_RECIPE;

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        evt.register(Registries.RECIPE_TYPE, helper -> {
            POT_RECIPE = registerType(helper, "pot");
            FLEX_POT_RECIPE = registerType(helper, "flex_pot");
            CHOPPING_BOARD_RECIPE = registerType(helper, "chopping_board");
            STOCKPOT_RECIPE = registerType(helper, "stockpot");
            FLEX_STOCKPOT_RECIPE = registerType(helper, "flex_stockpot");
            MILLSTONE_RECIPE = registerType(helper, "millstone");
            STEAMER_RECIPE = registerType(helper, "steamer");
            TEAPOT_RECIPE = registerType(helper, "teapot");
            BAMBOO_TRAY_RECIPE = registerType(helper, "bamboo_tray");
        });
    }

    private static <T extends Recipe<?>> RecipeType<T> registerType(RegisterEvent.RegisterHelper<RecipeType<?>> helper, String name) {
        Identifier id = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, name);
        RecipeType<T> type = RecipeType.simple(id);
        helper.register(id, type);
        return type;
    }
}
