package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.item.*;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeCookery.MOD_ID);

    // 厨具
    public static DeferredItem<Item> STOVE = ITEMS.registerItem("stove",
            properties -> new WithTooltipsBlockItem(ModBlocks.STOVE.get(), properties.useBlockDescriptionPrefix(), "stove"));
    public static DeferredItem<Item> POT = ITEMS.registerItem("pot",
            properties -> new WithTooltipsBlockItem(ModBlocks.POT.get(), properties.useBlockDescriptionPrefix(), "pot", "pot.fail"));
    public static DeferredItem<Item> STOCKPOT = ITEMS.registerItem("stockpot",
            properties -> new WithTooltipsBlockItem(ModBlocks.STOCKPOT.get(), properties.useBlockDescriptionPrefix(), "stockpot", "stockpot.fail"));
    public static DeferredItem<Item> STOCKPOT_LID = ITEMS.register("stockpot_lid", StockpotLidItem::new);
    public static DeferredItem<Item> CHOPPING_BOARD = ITEMS.registerItem("chopping_board",
            properties -> new WithTooltipsBlockItem(ModBlocks.CHOPPING_BOARD.get(), properties.useBlockDescriptionPrefix(), "chopping_board"));
    public static DeferredItem<Item> KITCHENWARE_RACKS = ITEMS.register("kitchenware_racks", () -> new WithTooltipsBlockItem(ModBlocks.KITCHENWARE_RACKS.get(), "kitchenware_racks"));
    public static DeferredItem<Item> KITCHEN_SHELF = ITEMS.register("kitchen_shelf", () -> new BlockItem(ModBlocks.KITCHEN_SHELF.get(), new Item.Properties()));
    public static DeferredItem<Item> BAMBOO_TRAY = ITEMS.register("bamboo_tray", () -> new BlockItem(ModBlocks.BAMBOO_TRAY.get(), new Item.Properties()));
    public static DeferredItem<Item> SHAWARMA_SPIT = ITEMS.registerItem("shawarma_spit",
            properties -> new WithTooltipsBlockItem(ModBlocks.SHAWARMA_SPIT.get(), properties.useBlockDescriptionPrefix(), "shawarma_spit"));
    public static DeferredItem<Item> MILLSTONE = ITEMS.registerItem("millstone",
            properties -> new WithTooltipsBlockItem(ModBlocks.MILLSTONE.get(), properties.useBlockDescriptionPrefix(), "millstone"));
    public static DeferredItem<Item> STEAMER = ITEMS.register("steamer", SteamerItem::new);

    // 茶
    public static DeferredItem<Item> TEAPOT = ITEMS.register("teapot", TeapotItem::new);
    public static DeferredItem<Item> EMPTY_CUP = ITEMS.register("empty_cup", EmptyCupItem::new);
    public static DeferredItem<Item> TIEGUANYIN_TEA_BAG = ITEMS.register("tieguanyin_tea_bag", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> BILUOCHUN_TEA_BAG = ITEMS.register("biluochun_tea_bag", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> OOLONG_TEA_BAG = ITEMS.register("oolong_tea_bag", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> SAKURA_FUBUKI_TEA_BAG = ITEMS.register("sakura_fubuki_tea_bag", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> BARLEY_TEA_BAG = ITEMS.register("barley_tea_bag", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> BUTTER_TEA_BAG = ITEMS.register("butter_tea_bag", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> CLAY_POT_MILK_TEA = ITEMS.register("clay_pot_milk_tea", () -> new ClayPotMilkTeaItem(ModBlocks.CLAY_POT_MILK_TEA.get()));

    // 垃圾桶
    public static DeferredItem<Item> TRASH_CAN = ITEMS.register("trash_can", () -> new BlockItem(ModBlocks.TRASH_CAN.get(), new Item.Properties()));

    // 油
    public static DeferredItem<Item> OIL = ITEMS.register("oil", OilItem::new);
    public static DeferredItem<Item> OIL_POT = ITEMS.register("oil_pot", OilPotItem::new);
    public static DeferredItem<Item> OIL_BLOCK = ITEMS.register("oil_block", () -> new BlockItem(ModBlocks.OIL_BLOCK.get(), new Item.Properties()));
    public static DeferredItem<Item> ENAMEL_BASIN = ITEMS.registerItem("enamel_basin",
            properties -> new WithTooltipsBlockItem(ModBlocks.ENAMEL_BASIN.get(), properties.useBlockDescriptionPrefix(), "enamel_basin"));

    // 杂项
    public static DeferredItem<Item> CHILI_RISTRA = ITEMS.register("chili_ristra", () -> new BlockItem(ModBlocks.CHILI_RISTRA.get(), new Item.Properties()));
    public static DeferredItem<Item> STRUNG_MUSHROOMS = ITEMS.register("strung_mushrooms", () -> new BlockItem(ModBlocks.STRUNG_MUSHROOMS.get(), new Item.Properties()));
    public static DeferredItem<Item> STRAW_BLOCK = ITEMS.register("straw_block", () -> new BlockItem(ModBlocks.STRAW_BLOCK.get(), new Item.Properties()));
    public static DeferredItem<Item> FRUIT_BASKET = ITEMS.registerItem("fruit_basket",
            properties -> new FruitBasketItem(properties.useBlockDescriptionPrefix()));
    public static DeferredItem<Item> SCARECROW = ITEMS.register("scarecrow", ScarecrowItem::new);
    public static DeferredItem<Item> RECIPE_ITEM = ITEMS.register("recipe_item", RecipeItem::new);
    public static DeferredItem<Item> TRANSMUTATION_LUNCH_BAG = ITEMS.register("transmutation_lunch_bag", TransmutationLunchBagItem::new);
    public static DeferredItem<Item> TEA_BANNER = ITEMS.register("tea_banner", TeaBannerItem::new);

    // 工具
    public static DeferredItem<Item> IRON_KITCHEN_KNIFE = ITEMS.registerItem("iron_kitchen_knife",
            properties -> new KitchenKnifeItem(ToolMaterial.IRON, properties));
    public static DeferredItem<Item> GOLD_KITCHEN_KNIFE = ITEMS.registerItem("gold_kitchen_knife",
            properties -> new KitchenKnifeItem(ToolMaterial.GOLD, properties));
    public static DeferredItem<Item> DIAMOND_KITCHEN_KNIFE = ITEMS.registerItem("diamond_kitchen_knife",
            properties -> new KitchenKnifeItem(ToolMaterial.DIAMOND, properties));
    public static DeferredItem<Item> NETHERITE_KITCHEN_KNIFE = ITEMS.registerItem("netherite_kitchen_knife",
            properties -> new KitchenKnifeItem(ToolMaterial.NETHERITE, properties.fireResistant()));
    public static DeferredItem<Item> KITCHEN_SHOVEL = ITEMS.registerItem("kitchen_shovel", KitchenShovelItem::new);
    public static DeferredItem<Item> SICKLE = ITEMS.registerItem("sickle", SickleItem::new);

    // 服装
    public static DeferredItem<Item> STRAW_HAT = ITEMS.registerItem("straw_hat", properties -> new StrawHatItem(false, properties));
    public static DeferredItem<Item> STRAW_HAT_FLOWER = ITEMS.registerItem("straw_hat_flower", properties -> new StrawHatItem(true, properties));
    public static final DeferredItem<Item> FARMER_CHEST_PLATE = ITEMS.registerSimpleItem("farmer_chest_plate",
            properties -> ModArmorMaterials.farmerArmor(properties, ArmorType.CHESTPLATE));
    public static final DeferredItem<Item> FARMER_LEGGINGS = ITEMS.registerSimpleItem("farmer_leggings",
            properties -> ModArmorMaterials.farmerArmor(properties, ArmorType.LEGGINGS));
    public static final DeferredItem<Item> FARMER_BOOTS = ITEMS.registerSimpleItem("farmer_boots",
            properties -> ModArmorMaterials.farmerArmor(properties, ArmorType.BOOTS));

    // 种子
    public static DeferredItem<Item> TOMATO_SEED = ITEMS.registerItem("tomato_seed",
            properties -> new WithTooltipsBlockItem(ModBlocks.TOMATO_CROP.get(), properties.useItemDescriptionPrefix(), "crop_seed"));
    public static DeferredItem<Item> CHILI_SEED = ITEMS.registerItem("chili_seed",
            properties -> new WithTooltipsBlockItem(ModBlocks.CHILI_CROP.get(), properties.useItemDescriptionPrefix(), "crop_seed"));
    public static DeferredItem<Item> LETTUCE_SEED = ITEMS.registerItem("lettuce_seed",
            properties -> new WithTooltipsBlockItem(ModBlocks.LETTUCE_CROP.get(), properties.useItemDescriptionPrefix(), "crop_seed"));
    public static DeferredItem<Item> RICE_SEED = ITEMS.registerItem("rice", RiceItem::new);
    public static DeferredItem<Item> WILD_RICE_SEED = ITEMS.registerItem("wild_rice",
            properties -> new WithTooltipsBlockItem(ModBlocks.RICE_CROP.get(), properties.useItemDescriptionPrefix(), "crop_seed"));
    public static DeferredItem<Item> TEA_SEED = ITEMS.registerItem("tea_seed",
            properties -> new WithTooltipsBlockItem(ModBlocks.TEA_TREE.get(), properties.useItemDescriptionPrefix(), "crop_seed"));

    // 凳子
    public static DeferredItem<Item> COOK_STOOL_OAK = ITEMS.register("cook_stool_oak", () -> new BlockItem(ModBlocks.COOK_STOOL_OAK.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_SPRUCE = ITEMS.register("cook_stool_spruce", () -> new BlockItem(ModBlocks.COOK_STOOL_SPRUCE.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_ACACIA = ITEMS.register("cook_stool_acacia", () -> new BlockItem(ModBlocks.COOK_STOOL_ACACIA.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_BAMBOO = ITEMS.register("cook_stool_bamboo", () -> new BlockItem(ModBlocks.COOK_STOOL_BAMBOO.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_BIRCH = ITEMS.register("cook_stool_birch", () -> new BlockItem(ModBlocks.COOK_STOOL_BIRCH.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_CHERRY = ITEMS.register("cook_stool_cherry", () -> new BlockItem(ModBlocks.COOK_STOOL_CHERRY.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_CRIMSON = ITEMS.register("cook_stool_crimson", () -> new BlockItem(ModBlocks.COOK_STOOL_CRIMSON.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_DARK_OAK = ITEMS.register("cook_stool_dark_oak", () -> new BlockItem(ModBlocks.COOK_STOOL_DARK_OAK.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_JUNGLE = ITEMS.register("cook_stool_jungle", () -> new BlockItem(ModBlocks.COOK_STOOL_JUNGLE.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_MANGROVE = ITEMS.register("cook_stool_mangrove", () -> new BlockItem(ModBlocks.COOK_STOOL_MANGROVE.get(), new Item.Properties()));
    public static DeferredItem<Item> COOK_STOOL_WARPED = ITEMS.register("cook_stool_warped", () -> new BlockItem(ModBlocks.COOK_STOOL_WARPED.get(), new Item.Properties()));

    // 椅子
    public static DeferredItem<Item> CHAIR_OAK = ITEMS.register("chair_oak", () -> new BlockItem(ModBlocks.CHAIR_OAK.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_SPRUCE = ITEMS.register("chair_spruce", () -> new BlockItem(ModBlocks.CHAIR_SPRUCE.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_ACACIA = ITEMS.register("chair_acacia", () -> new BlockItem(ModBlocks.CHAIR_ACACIA.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_BAMBOO = ITEMS.register("chair_bamboo", () -> new BlockItem(ModBlocks.CHAIR_BAMBOO.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_BIRCH = ITEMS.register("chair_birch", () -> new BlockItem(ModBlocks.CHAIR_BIRCH.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_CHERRY = ITEMS.register("chair_cherry", () -> new BlockItem(ModBlocks.CHAIR_CHERRY.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_CRIMSON = ITEMS.register("chair_crimson", () -> new BlockItem(ModBlocks.CHAIR_CRIMSON.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_DARK_OAK = ITEMS.register("chair_dark_oak", () -> new BlockItem(ModBlocks.CHAIR_DARK_OAK.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_JUNGLE = ITEMS.register("chair_jungle", () -> new BlockItem(ModBlocks.CHAIR_JUNGLE.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_MANGROVE = ITEMS.register("chair_mangrove", () -> new BlockItem(ModBlocks.CHAIR_MANGROVE.get(), new Item.Properties()));
    public static DeferredItem<Item> CHAIR_WARPED = ITEMS.register("chair_warped", () -> new BlockItem(ModBlocks.CHAIR_WARPED.get(), new Item.Properties()));

    // 桌子
    public static DeferredItem<Item> TABLE_OAK = ITEMS.register("table_oak", () -> new BlockItem(ModBlocks.TABLE_OAK.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_SPRUCE = ITEMS.register("table_spruce", () -> new BlockItem(ModBlocks.TABLE_SPRUCE.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_ACACIA = ITEMS.register("table_acacia", () -> new BlockItem(ModBlocks.TABLE_ACACIA.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_BAMBOO = ITEMS.register("table_bamboo", () -> new BlockItem(ModBlocks.TABLE_BAMBOO.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_BIRCH = ITEMS.register("table_birch", () -> new BlockItem(ModBlocks.TABLE_BIRCH.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_CHERRY = ITEMS.register("table_cherry", () -> new BlockItem(ModBlocks.TABLE_CHERRY.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_CRIMSON = ITEMS.register("table_crimson", () -> new BlockItem(ModBlocks.TABLE_CRIMSON.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_DARK_OAK = ITEMS.register("table_dark_oak", () -> new BlockItem(ModBlocks.TABLE_DARK_OAK.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_JUNGLE = ITEMS.register("table_jungle", () -> new BlockItem(ModBlocks.TABLE_JUNGLE.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_MANGROVE = ITEMS.register("table_mangrove", () -> new BlockItem(ModBlocks.TABLE_MANGROVE.get(), new Item.Properties()));
    public static DeferredItem<Item> TABLE_WARPED = ITEMS.register("table_warped", () -> new BlockItem(ModBlocks.TABLE_WARPED.get(), new Item.Properties()));
    public static DeferredItem<Item> LONG_BENCH = ITEMS.register("long_bench", () -> new BlockItem(ModBlocks.LONG_BENCH.get(), new Item.Properties()));
    public static DeferredItem<Item> RED_LANTERN = ITEMS.register("red_lantern", () -> new BlockItem(ModBlocks.RED_LANTERN.get(), new Item.Properties()));
    public static DeferredItem<Item> EIGHT_IMMORTALS_TABLE = ITEMS.register("eight_immortals_table", () -> new BlockItem(ModBlocks.EIGHT_IMMORTALS_TABLE.get(), new Item.Properties()));

    // 蔬菜食材
    public static DeferredItem<Item> TOMATO = ITEMS.register("tomato", () -> new Item(ModFoods.TOMATO.applyTo(new Item.Properties())));
    public static DeferredItem<Item> RED_CHILI = ITEMS.register("red_chili", () -> new ChiliItem(2));
    public static DeferredItem<Item> GREEN_CHILI = ITEMS.register("green_chili", () -> new ChiliItem(1));
    public static DeferredItem<Item> LETTUCE = ITEMS.register("lettuce", () -> new Item(ModFoods.LETTUCE.applyTo(new Item.Properties())));
    public static DeferredItem<Item> RICE_PANICLE = ITEMS.register("rice_panicle", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> FRESH_TEA_LEAVES = ITEMS.register("fresh_tea_leaves", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> DRIED_TEA_LEAVES = ITEMS.register("dried_tea_leaves", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> CATERPILLAR = ITEMS.register("caterpillar", () -> new WithTooltipsItem(ModFoods.CATERPILLAR.applyTo(new Item.Properties()), "caterpillar"));

    // 果串
    public static DeferredItem<Item> GLOW_BERRY_SKEWER = ITEMS.registerItem("glow_berry_skewer", properties -> new FruitSkewerItem(properties, ModFoods.BERRY_SKEWER));
    public static DeferredItem<Item> SWEET_BERRY_SKEWER = ITEMS.registerItem("sweet_berry_skewer", properties -> new FruitSkewerItem(properties, ModFoods.BERRY_SKEWER));
    public static DeferredItem<Item> CHORUS_FRUIT_SKEWER = ITEMS.registerItem("chorus_fruit_skewer", properties -> new FruitSkewerItem(properties, ModFoods.FRUIT_SKEWER));
    public static DeferredItem<Item> APPLE_SKEWER = ITEMS.registerItem("apple_skewer", properties -> new FruitSkewerItem(properties, ModFoods.FRUIT_SKEWER));
    public static DeferredItem<Item> CATERPILLAR_SKEWER = ITEMS.registerItem("caterpillar_skewer", properties -> new FruitSkewerItem(properties, ModFoods.CATERPILLAR_SKEWER));

    // 裹糖果串
    public static DeferredItem<Item> CANDIED_GLOW_BERRY_SKEWER = ITEMS.registerItem("candied_glow_berry_skewer",
            properties -> new CandiedSkewerItem(properties.overrideDescription(ModBlocks.CANDIED_GLOW_BERRY_SKEWER_UPRIGHT.get().getDescriptionId()),
                    ModBlocks.CANDIED_GLOW_BERRY_SKEWER_UPRIGHT, ModBlocks.CANDIED_GLOW_BERRY_SKEWER_SIDE, ModFoods.CANDIED_GLOW_BERRY_SKEWER, MobEffects.GLOWING, 5 * 20));
    public static DeferredItem<Item> CANDIED_SWEET_BERRY_SKEWER = ITEMS.registerItem("candied_sweet_berry_skewer",
            properties -> new CandiedSkewerItem(properties.overrideDescription(ModBlocks.CANDIED_SWEET_BERRY_SKEWER_UPRIGHT.get().getDescriptionId()),
                    ModBlocks.CANDIED_SWEET_BERRY_SKEWER_UPRIGHT, ModBlocks.CANDIED_SWEET_BERRY_SKEWER_SIDE, ModFoods.CANDIED_SWEET_BERRY_SKEWER));
    public static DeferredItem<Item> CANDIED_CHORUS_FRUIT_SKEWER = ITEMS.registerItem("candied_chorus_fruit_skewer",
            properties -> new CandiedSkewerItem(properties.overrideDescription(ModBlocks.CANDIED_CHORUS_FRUIT_SKEWER_UPRIGHT.get().getDescriptionId()),
                    ModBlocks.CANDIED_CHORUS_FRUIT_SKEWER_UPRIGHT, ModBlocks.CANDIED_CHORUS_FRUIT_SKEWER_SIDE, ModFoods.CANDIED_FRUIT_SKEWER));
    public static DeferredItem<Item> CANDIED_APPLE_SKEWER = ITEMS.registerItem("candied_apple_skewer",
            properties -> new CandiedSkewerItem(properties.overrideDescription(ModBlocks.CANDIED_APPLE_SKEWER_UPRIGHT.get().getDescriptionId()),
                    ModBlocks.CANDIED_APPLE_SKEWER_UPRIGHT, ModBlocks.CANDIED_APPLE_SKEWER_SIDE, ModFoods.CANDIED_FRUIT_SKEWER));
    public static DeferredItem<Item> CANDIED_CATERPILLAR_SKEWER = ITEMS.registerItem("candied_caterpillar_skewer",
            properties -> new CandiedSkewerItem(properties.overrideDescription(ModBlocks.CANDIED_CATERPILLAR_SKEWER_UPRIGHT.get().getDescriptionId()),
                    ModBlocks.CANDIED_CATERPILLAR_SKEWER_UPRIGHT, ModBlocks.CANDIED_CATERPILLAR_SKEWER_SIDE, ModFoods.CANDIED_CATERPILLAR_SKEWER, MobEffects.NAUSEA, 15 * 20));

    // 面类食材
    public static DeferredItem<Item> FLOUR = ITEMS.register("flour", FlourItem::new);
    public static DeferredItem<Item> RAW_DOUGH = ITEMS.register("raw_dough", RawDoughItem::new);
    public static DeferredItem<Item> RAW_NOODLES = ITEMS.register("raw_noodles", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> STUFFED_DOUGH_FOOD = ITEMS.register("stuffed_dough_food", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> RAW_ZONGZI = ITEMS.register("raw_zongzi", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> RAW_BAMBOO_TUBE_RICE = ITEMS.register("raw_bamboo_tube_rice", () -> new Item(new Item.Properties()));

    // 基础食物
    public static DeferredItem<Item> FRIED_EGG = ITEMS.register("fried_egg", () -> new Item(ModFoods.FRIED_EGG.applyTo(new Item.Properties())));
    public static DeferredItem<Item> TEA_EGG = ITEMS.registerItem("tea_egg", properties -> new FoodWithEffectsItem(properties, ModFoods.TEA_EGG));
    public static DeferredItem<Item> DONKEY_BURGER = ITEMS.registerItem("donkey_burger", properties -> new FoodWithEffectsItem(properties, ModFoods.DONKEY_BURGER));
    public static DeferredItem<Item> BAOZI = ITEMS.registerItem("baozi", properties -> new FoodWithEffectsItem(properties, ModFoods.BAOZI));
    public static DeferredItem<Item> SHENGJIAN_MANTOU = ITEMS.registerItem("shengjian_mantou", properties -> new FoodWithEffectsItem(properties, ModFoods.SHENGJIAN_MANTOU_ITEM));
    public static DeferredItem<Item> DUMPLING = ITEMS.register("dumpling", () -> new Item(ModFoods.DUMPLING.applyTo(new Item.Properties())));
    public static DeferredItem<Item> SAMSA = ITEMS.registerItem("samsa", properties -> new FoodWithEffectsItem(properties, ModFoods.SAMSA));
    public static DeferredItem<Item> MANTOU = ITEMS.registerItem("mantou", properties -> new FoodWithEffectsItem(properties, ModFoods.MANTOU));
    public static DeferredItem<Item> MEAT_PIE = ITEMS.registerItem("meat_pie", properties -> new FoodWithEffectsItem(properties, ModFoods.MEAT_PIE));
    public static DeferredItem<Item> QINGTUAN = ITEMS.registerItem("qingtuan", properties -> new FoodWithEffectsItem(properties, ModFoods.QINGTUAN));
    public static DeferredItem<Item> STICKY_CANDY = ITEMS.registerItem("sticky_candy", properties -> new FoodWithEffectsItem(properties, ModFoods.STICKY_CANDY));
    public static DeferredItem<Item> STICKY_RICE_CAKE = ITEMS.registerItem("sticky_rice_cake", properties -> new FoodWithEffectsItem(properties, ModFoods.STICKY_RICE_CAKE));
    public static DeferredItem<Item> ZONGZI = ITEMS.registerItem("zongzi", properties -> new FoodWithEffectsItem(properties, ModFoods.ZONGZI));
    public static DeferredItem<Item> BAMBOO_TUBE_RICE = ITEMS.registerItem("bamboo_tube_rice", properties -> new BambooTubeRiceBlockItem(properties.useBlockDescriptionPrefix(), ModBlocks.BAMBOO_TUBE_RICE.get(), ModFoods.BAMBOO_TUBE_RICE));

    // 小碗菜和盖饭
    public static DeferredItem<Item> COOKED_RICE = ITEMS.registerItem("cooked_rice",
            properties -> new BowlFoodOnlyItem(properties.usingConvertsTo(Items.BOWL), ModFoods.COOKED_RICE, Items.BOWL));
    public static DeferredItem<Item> EGG_FRIED_RICE = ITEMS.registerItem("egg_fried_rice", properties -> new BowlFoodOnlyItem(properties, ModFoods.EGG_FRIED_RICE));
    public static DeferredItem<Item> SCRAMBLE_EGG_WITH_TOMATOES = ITEMS.registerItem("scramble_egg_with_tomatoes", properties -> new BowlFoodOnlyItem(properties, ModFoods.SCRAMBLE_EGG_WITH_TOMATOES));
    public static DeferredItem<Item> SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL = ITEMS.registerItem("scramble_egg_with_tomatoes_rice_bowl", properties -> new BowlFoodOnlyItem(properties, ModFoods.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL));
    public static DeferredItem<Item> BRAISED_BEEF = ITEMS.registerItem("braised_beef", properties -> new BowlFoodOnlyItem(properties, ModFoods.BRAISED_BEEF));
    public static DeferredItem<Item> BRAISED_BEEF_RICE_BOWL = ITEMS.registerItem("braised_beef_rice_bowl", properties -> new BowlFoodOnlyItem(properties, ModFoods.BRAISED_BEEF_RICE_BOWL));
    public static DeferredItem<Item> STIR_FRIED_PORK_WITH_PEPPERS = ITEMS.registerItem("stir_fried_pork_with_peppers", properties -> new BowlFoodOnlyItem(properties, ModFoods.STIR_FRIED_PORK_WITH_PEPPERS));
    public static DeferredItem<Item> STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL = ITEMS.registerItem("stir_fried_pork_with_peppers_rice_bowl", properties -> new BowlFoodOnlyItem(properties, ModFoods.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL));
    public static DeferredItem<Item> SWEET_AND_SOUR_PORK = ITEMS.registerItem("sweet_and_sour_pork", properties -> new BowlFoodOnlyItem(properties, ModFoods.SWEET_AND_SOUR_PORK));
    public static DeferredItem<Item> SWEET_AND_SOUR_PORK_RICE_BOWL = ITEMS.registerItem("sweet_and_sour_pork_rice_bowl", properties -> new BowlFoodOnlyItem(properties, ModFoods.SWEET_AND_SOUR_PORK_RICE_BOWL));
    public static DeferredItem<Item> FISH_FLAVORED_SHREDDED_PORK = ITEMS.registerItem("fish_flavored_shredded_pork", properties -> new BowlFoodOnlyItem(properties, ModFoods.FISH_FLAVORED_SHREDDED_PORK));
    public static DeferredItem<Item> FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL = ITEMS.registerItem("fish_flavored_shredded_pork_rice_bowl", properties -> new BowlFoodOnlyItem(properties, ModFoods.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL));

    // 汤类
    public static DeferredItem<Item> PORK_BONE_SOUP = ITEMS.registerItem("pork_bone_soup", properties -> new BowlFoodOnlyItem(properties, ModFoods.PORK_BONE_SOUP));
    public static DeferredItem<Item> SEAFOOD_MISO_SOUP = ITEMS.registerItem("seafood_miso_soup", properties -> new BowlFoodOnlyItem(properties, ModFoods.SEAFOOD_MISO_SOUP));
    public static DeferredItem<Item> FEARSOME_THICK_SOUP = ITEMS.registerItem("fearsome_thick_soup", properties -> new BowlFoodOnlyItem(properties, ModFoods.FEARSOME_THICK_SOUP));
    public static DeferredItem<Item> LAMB_AND_RADISH_SOUP = ITEMS.registerItem("lamb_and_radish_soup", properties -> new BowlFoodOnlyItem(properties, ModFoods.LAMB_AND_RADISH_SOUP));
    public static DeferredItem<Item> BRAISED_BEEF_WITH_POTATOES = ITEMS.registerItem("braised_beef_with_potatoes", properties -> new BowlFoodOnlyItem(properties, ModFoods.BRAISED_BEEF_WITH_POTATOES));
    public static DeferredItem<Item> WILD_MUSHROOM_RABBIT_SOUP = ITEMS.registerItem("wild_mushroom_rabbit_soup", properties -> new BowlFoodOnlyItem(properties, ModFoods.WILD_MUSHROOM_RABBIT_SOUP));
    public static DeferredItem<Item> PUFFERFISH_SOUP = ITEMS.registerItem("pufferfish_soup", properties -> new BowlFoodOnlyItem(properties, ModFoods.PUFFERFISH_SOUP));
    public static DeferredItem<Item> BORSCHT = ITEMS.registerItem("borscht", properties -> new BowlFoodOnlyItem(properties, ModFoods.BORSCHT));
    public static DeferredItem<Item> BEEF_MEATBALL_SOUP = ITEMS.registerItem("beef_meatball_soup", properties -> new BowlFoodOnlyItem(properties, ModFoods.BEEF_MEATBALL_SOUP));
    public static DeferredItem<Item> CHICKEN_AND_MUSHROOM_STEW = ITEMS.registerItem("chicken_and_mushroom_stew", properties -> new BowlFoodOnlyItem(properties, ModFoods.CHICKEN_AND_MUSHROOM_STEW));
    public static DeferredItem<Item> LABA_CONGEE = ITEMS.registerItem("laba_congee", properties -> new BowlFoodOnlyItem(properties, ModFoods.LABA_CONGEE));

    // 面条类
    public static DeferredItem<Item> BEEF_NOODLE = ITEMS.registerItem("beef_noodle", properties -> new BowlFoodOnlyItem(properties, ModFoods.BEEF_NOODLE));
    public static DeferredItem<Item> HUI_NOODLE = ITEMS.registerItem("hui_noodle", properties -> new BowlFoodOnlyItem(properties, ModFoods.HUI_NOODLE));
    public static DeferredItem<Item> UDON_NOODLE = ITEMS.registerItem("udon_noodle", properties -> new BowlFoodOnlyItem(properties, ModFoods.UDON_NOODLE));
    public static DeferredItem<Item> HOT_DRY_NOODLES = ITEMS.registerItem("hot_dry_noodles", properties -> new BowlFoodOnlyItem(properties, ModFoods.HOT_DRY_NOODLES));

    // 生肉类
    public static DeferredItem<Item> SASHIMI = ITEMS.register("sashimi", () -> new Item(ModFoods.SASHIMI.applyTo(new Item.Properties())));
    public static DeferredItem<Item> RAW_LAMB_CHOPS = ITEMS.register("raw_lamb_chops", () -> new Item(ModFoods.RAW_LAMB_CHOPS.applyTo(new Item.Properties())));
    public static DeferredItem<Item> RAW_COW_OFFAL = ITEMS.register("raw_cow_offal", () -> new Item(ModFoods.RAW_COW_OFFAL.applyTo(new Item.Properties())));
    public static DeferredItem<Item> RAW_PORK_BELLY = ITEMS.register("raw_pork_belly", () -> new Item(ModFoods.RAW_PORK_BELLY.applyTo(new Item.Properties())));
    public static DeferredItem<Item> RAW_CUT_SMALL_MEATS = ITEMS.register("raw_cut_small_meats", () -> new Item(ModFoods.RAW_CUT_SMALL_MEATS.applyTo(new Item.Properties())));
    public static DeferredItem<Item> RAW_MEATBALL = ITEMS.register("raw_meatball", () -> new Item(ModFoods.RAW_MEATBALL.applyTo(new Item.Properties())));

    // 熟肉类
    public static DeferredItem<Item> COOKED_LAMB_CHOPS = ITEMS.register("cooked_lamb_chops", () -> new Item(ModFoods.COOKED_LAMB_CHOPS.applyTo(new Item.Properties())));
    public static DeferredItem<Item> COOKED_COW_OFFAL = ITEMS.register("cooked_cow_offal", () -> new Item(ModFoods.COOKED_COW_OFFAL.applyTo(new Item.Properties())));
    public static DeferredItem<Item> COOKED_PORK_BELLY = ITEMS.register("cooked_pork_belly", () -> new Item(ModFoods.COOKED_PORK_BELLY.applyTo(new Item.Properties())));
    public static DeferredItem<Item> COOKED_CUT_SMALL_MEATS = ITEMS.register("cooked_cut_small_meats", () -> new Item(ModFoods.COOKED_CUT_SMALL_MEATS.applyTo(new Item.Properties())));
    public static DeferredItem<Item> COOKED_MEATBALL = ITEMS.register("cooked_meatball", () -> new Item(ModFoods.COOKED_MEATBALL.applyTo(new Item.Properties())));

    // 特殊
    public static DeferredItem<Item> COLD_CUT_HAM_SLICES = ITEMS.register("cold_cut_ham_slices", () -> new LiftBlockItem(ModBlocks.COLD_CUT_HAM_SLICES.get(), "cold_cut_ham_slices"));
}
