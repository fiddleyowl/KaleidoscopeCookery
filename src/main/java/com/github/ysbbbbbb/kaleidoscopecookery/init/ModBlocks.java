package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.*;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.*;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.ClayPotMilkTeaBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.EmptyCupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteThreeByThreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.*;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.*;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.*;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.food.FoodBiteThreeByThreeBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.*;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.misc.TrashCanBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeCookery.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, KaleidoscopeCookery.MOD_ID);

    public static DeferredBlock<Block> STOVE = BLOCKS.registerBlock("stove", StoveBlock::new);
    public static DeferredBlock<Block> POT = BLOCKS.registerBlock("pot", PotBlock::new);
    public static DeferredBlock<Block> STOCKPOT = BLOCKS.registerBlock("stockpot", StockpotBlock::new);
    public static DeferredBlock<Block> FRUIT_BASKET = BLOCKS.register("fruit_basket", FruitBasketBlock::new);
    public static DeferredBlock<Block> CHOPPING_BOARD = BLOCKS.register("chopping_board", ChoppingBoardBlock::new);
    public static DeferredBlock<Block> OIL_BLOCK = BLOCKS.register("oil_block", OilBlock::new);
    public static DeferredBlock<Block> ENAMEL_BASIN = BLOCKS.register("enamel_basin", EnamelBasinBlock::new);
    public static DeferredBlock<Block> KITCHENWARE_RACKS = BLOCKS.register("kitchenware_racks", KitchenwareRacksBlock::new);
    public static DeferredBlock<Block> KITCHEN_SHELF = BLOCKS.register("kitchen_shelf", KitchenShelfBlock::new);
    public static DeferredBlock<Block> BAMBOO_TRAY = BLOCKS.register("bamboo_tray", BambooTrayBlock::new);
    public static DeferredBlock<Block> CHILI_RISTRA = BLOCKS.register("chili_ristra", ChiliRistraBlock::new);
    public static DeferredBlock<Block> STRUNG_MUSHROOMS = BLOCKS.register("strung_mushrooms", StrungMushroomsBlock::new);
    public static DeferredBlock<Block> STRAW_BLOCK = BLOCKS.register("straw_block", StrawBlocks::new);
    public static DeferredBlock<Block> SHAWARMA_SPIT = BLOCKS.register("shawarma_spit", ShawarmaSpitBlock::new);
    public static DeferredBlock<Block> MILLSTONE = BLOCKS.register("millstone", MillstoneBlock::new);
    public static DeferredBlock<Block> STEAMER = BLOCKS.register("steamer", SteamerBlock::new);

    public static DeferredBlock<Block> TEAPOT = BLOCKS.register("teapot", TeapotBlock::new);
    public static DeferredBlock<Block> EMPTY_CUP = BLOCKS.register("empty_cup", EmptyCupBlock::new);
    public static DeferredBlock<Block> CLAY_POT_MILK_TEA = BLOCKS.register("clay_pot_milk_tea", ClayPotMilkTeaBlock::new);

    public static DeferredBlock<Block> TRASH_CAN = BLOCKS.register("trash_can", TrashCanBlock::new);

    public static DeferredBlock<Block> OIL_POT = BLOCKS.register("oil_pot", OilPotBlock::new);
    public static DeferredBlock<Block> RECIPE_BLOCK = BLOCKS.register("recipe_block", RecipeBlock::new);
    public static DeferredBlock<Block> TEA_BANNER = BLOCKS.register("tea_banner", TeaBannerBlock::new);

    public static DeferredBlock<Block> TOMATO_CROP = BLOCKS.registerBlock("tomato_crop", properties -> new BaseCropBlock(properties, ModItems.TOMATO, ModItems.TOMATO_SEED));
    public static DeferredBlock<Block> CHILI_CROP = BLOCKS.registerBlock("chili_crop", ChiliCropBlock::new);
    public static DeferredBlock<Block> LETTUCE_CROP = BLOCKS.registerBlock("lettuce_crop", LettuceCropBlock::new);
    public static DeferredBlock<Block> RICE_CROP = BLOCKS.registerBlock("rice_crop", RiceCropBlock::new);
    public static DeferredBlock<Block> TEA_TREE = BLOCKS.register("tea_tree", TeaTreeBlock::new);

    public static DeferredBlock<Block> COOK_STOOL_OAK = BLOCKS.registerBlock("cook_stool_oak", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_SPRUCE = BLOCKS.registerBlock("cook_stool_spruce", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_ACACIA = BLOCKS.registerBlock("cook_stool_acacia", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_BAMBOO = BLOCKS.registerBlock("cook_stool_bamboo", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_BIRCH = BLOCKS.registerBlock("cook_stool_birch", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_CHERRY = BLOCKS.registerBlock("cook_stool_cherry", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_CRIMSON = BLOCKS.registerBlock("cook_stool_crimson", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_DARK_OAK = BLOCKS.registerBlock("cook_stool_dark_oak", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_JUNGLE = BLOCKS.registerBlock("cook_stool_jungle", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_MANGROVE = BLOCKS.registerBlock("cook_stool_mangrove", CookStoolBlock::new);
    public static DeferredBlock<Block> COOK_STOOL_WARPED = BLOCKS.registerBlock("cook_stool_warped", CookStoolBlock::new);

    public static DeferredBlock<Block> CHAIR_OAK = BLOCKS.registerBlock("chair_oak", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_SPRUCE = BLOCKS.registerBlock("chair_spruce", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_ACACIA = BLOCKS.registerBlock("chair_acacia", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_BAMBOO = BLOCKS.registerBlock("chair_bamboo", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_BIRCH = BLOCKS.registerBlock("chair_birch", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_CHERRY = BLOCKS.registerBlock("chair_cherry", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_CRIMSON = BLOCKS.registerBlock("chair_crimson", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_DARK_OAK = BLOCKS.registerBlock("chair_dark_oak", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_JUNGLE = BLOCKS.registerBlock("chair_jungle", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_MANGROVE = BLOCKS.registerBlock("chair_mangrove", ChairBlock::new);
    public static DeferredBlock<Block> CHAIR_WARPED = BLOCKS.registerBlock("chair_warped", ChairBlock::new);

    public static DeferredBlock<Block> TABLE_OAK = BLOCKS.registerBlock("table_oak", TableBlock::new);
    public static DeferredBlock<Block> TABLE_SPRUCE = BLOCKS.registerBlock("table_spruce", TableBlock::new);
    public static DeferredBlock<Block> TABLE_ACACIA = BLOCKS.registerBlock("table_acacia", TableBlock::new);
    public static DeferredBlock<Block> TABLE_BAMBOO = BLOCKS.registerBlock("table_bamboo", TableBlock::new);
    public static DeferredBlock<Block> TABLE_BIRCH = BLOCKS.registerBlock("table_birch", TableBlock::new);
    public static DeferredBlock<Block> TABLE_CHERRY = BLOCKS.registerBlock("table_cherry", TableBlock::new);
    public static DeferredBlock<Block> TABLE_CRIMSON = BLOCKS.registerBlock("table_crimson", TableBlock::new);
    public static DeferredBlock<Block> TABLE_DARK_OAK = BLOCKS.registerBlock("table_dark_oak", TableBlock::new);
    public static DeferredBlock<Block> TABLE_JUNGLE = BLOCKS.registerBlock("table_jungle", TableBlock::new);
    public static DeferredBlock<Block> TABLE_MANGROVE = BLOCKS.registerBlock("table_mangrove", TableBlock::new);
    public static DeferredBlock<Block> TABLE_WARPED = BLOCKS.registerBlock("table_warped", TableBlock::new);

    public static DeferredBlock<Block> LONG_BENCH = BLOCKS.register("long_bench", LongBenchBlock::new);
    public static DeferredBlock<Block> RED_LANTERN = BLOCKS.register("red_lantern", RedLanternBlock::new);
    public static DeferredBlock<Block> EIGHT_IMMORTALS_TABLE = BLOCKS.register("eight_immortals_table", EightImmortalsTableBlock::new);

    public static DeferredBlock<Block> CANDIED_GLOW_BERRY_SKEWER_UPRIGHT = candiedSkewer(
            "candied_glow_berry_skewer_upright", 12, true,
            () -> ModItems.CANDIED_GLOW_BERRY_SKEWER.get(), 8
    );

    public static DeferredBlock<Block> CANDIED_GLOW_BERRY_SKEWER_SIDE = candiedSkewer(
            "candied_glow_berry_skewer_side", 5, false,
            () -> ModItems.CANDIED_GLOW_BERRY_SKEWER.get(), 8
    );

    public static DeferredBlock<Block> CANDIED_SWEET_BERRY_SKEWER_UPRIGHT = candiedSkewer(
            "candied_sweet_berry_skewer_upright", 12, true,
            () -> ModItems.CANDIED_SWEET_BERRY_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_SWEET_BERRY_SKEWER_SIDE = candiedSkewer(
            "candied_sweet_berry_skewer_side", 5, false,
            () -> ModItems.CANDIED_SWEET_BERRY_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_CHORUS_FRUIT_SKEWER_UPRIGHT = candiedSkewer(
            "candied_chorus_fruit_skewer_upright", 5, true,
            () -> ModItems.CANDIED_CHORUS_FRUIT_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_CHORUS_FRUIT_SKEWER_SIDE = candiedSkewer(
            "candied_chorus_fruit_skewer_side", 4, false,
            () -> ModItems.CANDIED_CHORUS_FRUIT_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_APPLE_SKEWER_UPRIGHT = candiedSkewer(
            "candied_apple_skewer_upright", 5, true,
            () -> ModItems.CANDIED_APPLE_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_APPLE_SKEWER_SIDE = candiedSkewer(
            "candied_apple_skewer_side", 4, false,
            () -> ModItems.CANDIED_APPLE_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_CATERPILLAR_SKEWER_UPRIGHT = candiedSkewer(
            "candied_caterpillar_skewer_upright", 12, true,
            () -> ModItems.CANDIED_CATERPILLAR_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> CANDIED_CATERPILLAR_SKEWER_SIDE = candiedSkewer(
            "candied_caterpillar_skewer_side", 5, false,
            () -> ModItems.CANDIED_CATERPILLAR_SKEWER.get(), 0
    );

    public static DeferredBlock<Block> COLD_CUT_HAM_SLICES = BLOCKS.registerBlock(
            "cold_cut_ham_slices", properties -> new FoodBiteThreeByThreeBlock(
            properties, ModFoods.COLD_CUT_HAM_SLICES_BLOCK, 8, null
    ));

    public static DeferredBlock<Block> BAMBOO_TUBE_RICE = BLOCKS.register("bamboo_tube_rice", StackableFoodBlock.create()
            .maxCount(4)
            .item(() -> ModItems.BAMBOO_TUBE_RICE.get())
            .shapes(
                    Block.box(4, 0, 4, 12, 10, 12),
                    Shapes.or(
                            Block.box(7, 0, 1, 15, 10, 9),
                            Block.box(1, 0, 7, 9, 10, 15)
                    ),
                    Shapes.or(
                            Block.box(0, 0, 6, 16, 10, 15),
                            Block.box(4, 0, 0, 12, 10, 15)
                    ),
                    Block.box(0, 0, 0, 16, 10, 16)
            ).build());

    public static Supplier<BlockEntityType<PotBlockEntity>> POT_BE = BLOCK_ENTITIES.register("pot",
            () -> new BlockEntityType<>(PotBlockEntity::new, POT.get()));

    public static Supplier<BlockEntityType<StockpotBlockEntity>> STOCKPOT_BE = BLOCK_ENTITIES.register("stockpot",
            () -> new BlockEntityType<>(StockpotBlockEntity::new, STOCKPOT.get()));

    public static Supplier<BlockEntityType<FruitBasketBlockEntity>> FRUIT_BASKET_BE = BLOCK_ENTITIES.register("fruit_basket",
            () -> new BlockEntityType<>(FruitBasketBlockEntity::new, FRUIT_BASKET.get()));

    public static Supplier<BlockEntityType<ChoppingBoardBlockEntity>> CHOPPING_BOARD_BE = BLOCK_ENTITIES.register("chopping_board",
            () -> new BlockEntityType<>(ChoppingBoardBlockEntity::new, CHOPPING_BOARD.get()));

    public static Supplier<BlockEntityType<KitchenwareRacksBlockEntity>> KITCHENWARE_RACKS_BE = BLOCK_ENTITIES.register("kitchenware_racks",
            () -> new BlockEntityType<>(KitchenwareRacksBlockEntity::new, KITCHENWARE_RACKS.get()));

    public static Supplier<BlockEntityType<KitchenShelfBlockEntity>> KITCHEN_SHELF_BE = BLOCK_ENTITIES.register("kitchen_shelf",
            () -> new BlockEntityType<>(KitchenShelfBlockEntity::new, KITCHEN_SHELF.get()));

    public static Supplier<BlockEntityType<BambooTrayBlockEntity>> BAMBOO_TRAY_BE = BLOCK_ENTITIES.register("bamboo_tray",
            () -> new BlockEntityType<>(BambooTrayBlockEntity::new, BAMBOO_TRAY.get()));

    public static Supplier<BlockEntityType<ShawarmaSpitBlockEntity>> SHAWARMA_SPIT_BE = BLOCK_ENTITIES.register("shawarma_spit",
            () -> new BlockEntityType<>(ShawarmaSpitBlockEntity::new, SHAWARMA_SPIT.get()));

    public static Supplier<BlockEntityType<MillstoneBlockEntity>> MILLSTONE_BE = BLOCK_ENTITIES.register("millstone",
            () -> new BlockEntityType<>(MillstoneBlockEntity::new, MILLSTONE.get()));

    public static Supplier<BlockEntityType<RecipeBlockEntity>> RECIPE_BLOCK_BE = BLOCK_ENTITIES.register("recipe_block",
            () -> new BlockEntityType<>(RecipeBlockEntity::new, RECIPE_BLOCK.get()));

    public static Supplier<BlockEntityType<TeaBannerBlockEntity>> TEA_BANNER_BE = BLOCK_ENTITIES.register("tea_banner",
            () -> new BlockEntityType<>(TeaBannerBlockEntity::new, TEA_BANNER.get()));

    public static Supplier<BlockEntityType<SteamerBlockEntity>> STEAMER_BE = BLOCK_ENTITIES.register("steamer",
            () -> new BlockEntityType<>(SteamerBlockEntity::new, STEAMER.get()));

    public static Supplier<BlockEntityType<TeapotBlockEntity>> TEAPOT_BE = BLOCK_ENTITIES.register("teapot",
            () -> new BlockEntityType<>(TeapotBlockEntity::new, TEAPOT.get()));

    public static Supplier<BlockEntityType<OilPotBlockEntity>> OIL_POT_BE = BLOCK_ENTITIES.register("oil_pot",
            () -> new BlockEntityType<>(OilPotBlockEntity::new, OIL_POT.get()));

    public static Supplier<BlockEntityType<TrashCanBlockEntity>> TRASH_CAN_BE = BLOCK_ENTITIES.register("trash_can",
            () -> new BlockEntityType<>(TrashCanBlockEntity::new, TRASH_CAN.get()));

    public static Supplier<BlockEntityType<FoodBiteThreeByThreeBlockEntity>> FOOD_BITE_THREE_BY_THREE_BE = BLOCK_ENTITIES.register("food_bite_three_by_three",
            () -> new BlockEntityType<>(FoodBiteThreeByThreeBlockEntity::new, COLD_CUT_HAM_SLICES.get()));

    public static Supplier<BlockEntityType<ChairBlockEntity>> CHAIR_BE = BLOCK_ENTITIES.register("chair", () -> new BlockEntityType<>(ChairBlockEntity::new,
            CHAIR_OAK.get(), CHAIR_SPRUCE.get(), CHAIR_ACACIA.get(), CHAIR_BAMBOO.get(),
            CHAIR_BIRCH.get(), CHAIR_CHERRY.get(), CHAIR_CRIMSON.get(), CHAIR_DARK_OAK.get(),
            CHAIR_JUNGLE.get(), CHAIR_MANGROVE.get(), CHAIR_WARPED.get()
    ));

    public static Supplier<BlockEntityType<TableBlockEntity>> TABLE_BE = BLOCK_ENTITIES.register("table", () -> new BlockEntityType<>(TableBlockEntity::new,
            TABLE_OAK.get(), TABLE_SPRUCE.get(), TABLE_ACACIA.get(), TABLE_BAMBOO.get(),
            TABLE_BIRCH.get(), TABLE_CHERRY.get(), TABLE_CRIMSON.get(), TABLE_DARK_OAK.get(),
            TABLE_JUNGLE.get(), TABLE_MANGROVE.get(), TABLE_WARPED.get()
    ));

    private static DeferredBlock<Block> candiedSkewer(String name, int maxCount, boolean upright, Supplier<Item> item, int lightLevel) {
        return BLOCKS.register(name, () -> new CandiedSkewerBlock(maxCount, item, lightLevel, upright));
    }
}
