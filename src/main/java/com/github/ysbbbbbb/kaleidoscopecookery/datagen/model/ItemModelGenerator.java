package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.init.ClientSetupEvent.ItemModelProperty;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.PlateRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static net.minecraft.client.data.models.model.ItemModelUtils.override;
import static net.minecraft.client.data.models.model.ItemModelUtils.plainModel;
import static net.minecraft.client.data.models.model.ItemModelUtils.rangeSelect;
import static net.minecraft.client.data.models.model.ItemModelUtils.select;
import static net.minecraft.client.data.models.model.ItemModelUtils.when;

public class ItemModelGenerator extends ModelProvider {
    private ItemModelGenerators models;

    public ItemModelGenerator(PackOutput output) {
        super(output, KaleidoscopeCookery.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        new BlockModelGenerator(blockModels).registerModels();
        new BlockStateGenerator(blockModels).registerStatesAndModels();
        this.models = itemModels;
        registerModels();
    }

    protected void registerModels() {
        withExistingParent("stove", modLoc("block/stove"));
        withExistingParent("pot", modLoc("block/pot"));
        withExistingParent("stockpot", modLoc("block/stockpot"));

        handheldItem(ModItems.IRON_KITCHEN_KNIFE.get());
        handheldItem(ModItems.GOLD_KITCHEN_KNIFE.get());
        handheldItem(ModItems.DIAMOND_KITCHEN_KNIFE.get());
        handheldItem(ModItems.NETHERITE_KITCHEN_KNIFE.get());
        handheldItem(ModItems.SICKLE.get());

        basicItem(ModItems.OIL.get());
        basicItem(ModItems.FRIED_EGG.get());
        basicItem(ModItems.TEA_EGG.get());
        basicItem(ModItems.SCARECROW.get());
        basicItem(ModItems.TEA_BANNER.get());
        basicItem(ModItems.TOMATO.get());
        basicItem(ModItems.SCRAMBLE_EGG_WITH_TOMATOES.get());
        basicItem(ModItems.SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL.get());
        basicItem(ModItems.BRAISED_BEEF.get());
        basicItem(ModItems.BRAISED_BEEF_RICE_BOWL.get());
        basicItem(ModItems.STIR_FRIED_PORK_WITH_PEPPERS.get());
        basicItem(ModItems.STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL.get());
        basicItem(ModItems.SWEET_AND_SOUR_PORK.get());
        basicItem(ModItems.SWEET_AND_SOUR_PORK_RICE_BOWL.get());
        basicItem(ModItems.FISH_FLAVORED_SHREDDED_PORK.get());
        basicItem(ModItems.FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL.get());
        basicItem(ModItems.EGG_FRIED_RICE.get());
        basicItem(ModItems.PORK_BONE_SOUP.get());
        basicItem(ModItems.SEAFOOD_MISO_SOUP.get());
        basicItem(ModItems.FEARSOME_THICK_SOUP.get());
        basicItem(ModItems.LAMB_AND_RADISH_SOUP.get());
        basicItem(ModItems.BRAISED_BEEF_WITH_POTATOES.get());
        basicItem(ModItems.WILD_MUSHROOM_RABBIT_SOUP.get());
        basicItem(ModItems.PUFFERFISH_SOUP.get());
        basicItem(ModItems.BORSCHT.get());
        basicItem(ModItems.BEEF_MEATBALL_SOUP.get());
        basicItem(ModItems.CHICKEN_AND_MUSHROOM_STEW.get());
        basicItem(ModItems.STRAW_HAT.get());
        basicItem(ModItems.STRAW_HAT_FLOWER.get());
        basicItem(ModItems.FARMER_CHEST_PLATE.get());
        basicItem(ModItems.FARMER_LEGGINGS.get());
        basicItem(ModItems.FARMER_BOOTS.get());
        basicItem(ModItems.TOMATO_SEED.get());
        basicItem(ModItems.TEA_SEED.get());
        basicItem(ModItems.FRESH_TEA_LEAVES.get());
        basicItem(ModItems.DRIED_TEA_LEAVES.get());
        basicItem(ModItems.TIEGUANYIN_TEA_BAG.get());
        basicItem(ModItems.BILUOCHUN_TEA_BAG.get());
        basicItem(ModItems.OOLONG_TEA_BAG.get());
        basicItem(ModItems.SAKURA_FUBUKI_TEA_BAG.get());
        basicItem(ModItems.BARLEY_TEA_BAG.get());
        basicItem(ModItems.BUTTER_TEA_BAG.get());
        basicItem(ModItems.CLAY_POT_MILK_TEA.get());
        basicItem(ModItems.RICE_SEED.get());
        basicItem(ModItems.WILD_RICE_SEED.get());
        basicItem(ModItems.RICE_PANICLE.get());
        basicItem(ModItems.SASHIMI.get());
        basicItem(ModItems.RAW_LAMB_CHOPS.get());
        basicItem(ModItems.RAW_COW_OFFAL.get());
        basicItem(ModItems.RAW_PORK_BELLY.get());
        basicItem(ModItems.COOKED_LAMB_CHOPS.get());
        basicItem(ModItems.COOKED_COW_OFFAL.get());
        basicItem(ModItems.COOKED_PORK_BELLY.get());
        basicItem(ModItems.COOKED_RICE.get());
        basicItem(ModItems.RED_CHILI.get());
        basicItem(ModItems.GREEN_CHILI.get());
        basicItem(ModItems.CHILI_SEED.get());
        basicItem(ModItems.LETTUCE.get());
        basicItem(ModItems.LETTUCE_SEED.get());
        basicItem(ModItems.CATERPILLAR.get());
        basicItem(ModItems.ENAMEL_BASIN.get());
        basicItem(ModItems.KITCHENWARE_RACKS.get());
        basicItem(ModItems.MILLSTONE.get());
        basicItem(ModItems.RAW_NOODLES.get());
        basicItem(ModItems.STUFFED_DOUGH_FOOD.get());
        basicItem(ModItems.DONKEY_BURGER.get());
        basicItem(ModItems.FLOUR.get());
        basicItem(ModItems.RAW_CUT_SMALL_MEATS.get());
        basicItem(ModItems.COOKED_CUT_SMALL_MEATS.get());
        basicItem(ModItems.RAW_MEATBALL.get());
        basicItem(ModItems.COOKED_MEATBALL.get());

        basicItem(ModItems.BAOZI.get());
        basicItem(ModItems.SHENGJIAN_MANTOU.get());
        basicItem(ModItems.DUMPLING.get());
        basicItem(ModItems.SAMSA.get());
        basicItem(ModItems.MANTOU.get());
        basicItem(ModItems.MEAT_PIE.get());
        basicItem(ModItems.STICKY_CANDY.get());
        basicItem(ModItems.STICKY_RICE_CAKE.get());
        basicItem(ModItems.QINGTUAN.get());
        basicItem(ModItems.RAW_ZONGZI.get());
        basicItem(ModItems.ZONGZI.get());
        basicItem(ModItems.RAW_BAMBOO_TUBE_RICE.get());
        basicItem(ModItems.BAMBOO_TUBE_RICE.get());

        basicItem(ModItems.BEEF_NOODLE.get());
        basicItem(ModItems.HUI_NOODLE.get());
        basicItem(ModItems.UDON_NOODLE.get());
        basicItem(ModItems.HOT_DRY_NOODLES.get());
        basicItem(ModItems.LABA_CONGEE.get());

        handheldItem(ModItems.GLOW_BERRY_SKEWER.get());
        handheldItem(ModItems.SWEET_BERRY_SKEWER.get());
        handheldItem(ModItems.CHORUS_FRUIT_SKEWER.get());
        handheldItem(ModItems.APPLE_SKEWER.get());
        handheldItem(ModItems.CATERPILLAR_SKEWER.get());
        handheldItem(ModItems.CANDIED_GLOW_BERRY_SKEWER.get());
        handheldItem(ModItems.CANDIED_SWEET_BERRY_SKEWER.get());
        handheldItem(ModItems.CANDIED_CHORUS_FRUIT_SKEWER.get());
        handheldItem(ModItems.CANDIED_APPLE_SKEWER.get());
        handheldItem(ModItems.CANDIED_CATERPILLAR_SKEWER.get());

        basicItem(modLoc("honey"));
        basicItem(modLoc("egg"));
        basicItem(modLoc("oil_in_millstone"));

        withExistingParent("bamboo_tray", modLoc("block/bamboo_tray/base"));

        Identifier chileRistra = BuiltInRegistries.ITEM.getKey(ModItems.CHILI_RISTRA.get());
        registerClientItem(chileRistra, plainModel(basicItem(chileRistra,
                Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block/chili_ristra/head"))));

        Identifier strungMushrooms = BuiltInRegistries.ITEM.getKey(ModItems.STRUNG_MUSHROOMS.get());
        registerClientItem(strungMushrooms, plainModel(basicItem(strungMushrooms,
                Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "block/strung_mushrooms/head"))));

        Identifier shovel = BuiltInRegistries.ITEM.getKey(ModItems.KITCHEN_SHOVEL.get());
        Identifier shovelNoOil = handheldItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "kitchen_shovel_no_oil"));
        Identifier shovelHasOil = handheldItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "kitchen_shovel_has_oil"));
        rangeModel(shovel, ItemModelProperty.HAS_OIL,
                new RangeEntry(0, shovelNoOil), new RangeEntry(1, shovelHasOil));

        Identifier stockpotLid = BuiltInRegistries.ITEM.getKey(ModItems.STOCKPOT_LID.get());
        Identifier normal = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/stockpot_lid_normal");
        Identifier using = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/stockpot_lid_using");
        rangeModel(stockpotLid, ItemModelProperty.USING,
                new RangeEntry(0, normal), new RangeEntry(1, using));

        Identifier oilPot = BuiltInRegistries.ITEM.getKey(ModItems.OIL_POT.get());
        if (oilPot != null) {
            Identifier potNoOil = basicItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "pot_no_oil"));
            Identifier potHasOil = basicItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "pot_has_oil"));
            rangeModel(oilPot, ItemModelProperty.HAS_OIL,
                    new RangeEntry(0, potNoOil), new RangeEntry(1, potHasOil));
        }

        Identifier recipeItem = BuiltInRegistries.ITEM.getKey(ModItems.RECIPE_ITEM.get());
        if (recipeItem != null) {
            Identifier noRecipe = basicItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "recipe_item_no_recipe"));
            Identifier hasRecipe = basicItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "recipe_item_has_recipe"));
            rangeModel(recipeItem, ItemModelProperty.HAS_RECIPE,
                    new RangeEntry(0, noRecipe), new RangeEntry(1, hasRecipe));
        }

        Identifier bagItem = BuiltInRegistries.ITEM.getKey(ModItems.TRANSMUTATION_LUNCH_BAG.get());
        if (bagItem != null) {
            Identifier noItems = basicItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "transmutation_lunch_bag_no_items"));
            Identifier hasItems = basicItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "transmutation_lunch_bag_has_items"));
            rangeModel(bagItem, ItemModelProperty.HAS_ITEMS,
                    new RangeEntry(0, noItems), new RangeEntry(1, hasItems));
        }

        Identifier rawDough = BuiltInRegistries.ITEM.getKey(ModItems.RAW_DOUGH.get());
        if (rawDough != null) {
            Identifier file0 = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/raw_dough_0");
            Identifier file1 = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/raw_dough_1");
            Identifier file2 = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/raw_dough_2");
            Identifier file3 = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/raw_dough_3");
            Identifier file4 = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item/raw_dough_4");

            rangeModel(rawDough, ItemModelProperty.PULL,
                    new RangeEntry(0, file0),
                    new RangeEntry(0.1f, file1),
                    new RangeEntry(1, file2),
                    new RangeEntry(2, file3),
                    new RangeEntry(3, file4));
        }

        Identifier steamerItem = BuiltInRegistries.ITEM.getKey(ModItems.STEAMER.get());
        if (steamerItem != null) {
            Identifier noItems = basicItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "steamer_no_items"));
            Identifier hasItems = basicItem(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "steamer_has_items"));
            rangeModel(steamerItem, ItemModelProperty.HAS_ITEMS,
                    new RangeEntry(0, noItems), new RangeEntry(1, hasItems));
        }

        FoodBiteRegistry.FOOD_DATA_MAP.forEach((key, value) -> {
            Item item = BuiltInRegistries.ITEM.getValue(key);
            basicItem(item);
        });

        PlateRegistry.PLATE_DATA_MAP.forEach((key, value) -> {
            Item item = BuiltInRegistries.ITEM.getValue(key);
            basicItem(item);
        });

        basicItem(ModItems.EMPTY_CUP.get());
        TeacupRegistry.TEACUP_DATA_MAP.forEach((key, value) -> {
            Item item = BuiltInRegistries.ITEM.getValue(key);
            basicItem(item);
        });

        withExistingParent("cook_stool_oak", modLoc("block/cook_stool/oak"));
        withExistingParent("cook_stool_spruce", modLoc("block/cook_stool/spruce"));
        withExistingParent("cook_stool_acacia", modLoc("block/cook_stool/acacia"));
        withExistingParent("cook_stool_bamboo", modLoc("block/cook_stool/bamboo"));
        withExistingParent("cook_stool_birch", modLoc("block/cook_stool/birch"));
        withExistingParent("cook_stool_cherry", modLoc("block/cook_stool/cherry"));
        withExistingParent("cook_stool_crimson", modLoc("block/cook_stool/crimson"));
        withExistingParent("cook_stool_dark_oak", modLoc("block/cook_stool/dark_oak"));
        withExistingParent("cook_stool_jungle", modLoc("block/cook_stool/jungle"));
        withExistingParent("cook_stool_mangrove", modLoc("block/cook_stool/mangrove"));
        withExistingParent("cook_stool_warped", modLoc("block/cook_stool/warped"));

        withExistingParent("chair_oak", modLoc("block/chair/oak"));
        withExistingParent("chair_spruce", modLoc("block/chair/spruce"));
        withExistingParent("chair_acacia", modLoc("block/chair/acacia"));
        withExistingParent("chair_bamboo", modLoc("block/chair/bamboo"));
        withExistingParent("chair_birch", modLoc("block/chair/birch"));
        withExistingParent("chair_cherry", modLoc("block/chair/cherry"));
        withExistingParent("chair_crimson", modLoc("block/chair/crimson"));
        withExistingParent("chair_dark_oak", modLoc("block/chair/dark_oak"));
        withExistingParent("chair_jungle", modLoc("block/chair/jungle"));
        withExistingParent("chair_mangrove", modLoc("block/chair/mangrove"));
        withExistingParent("chair_warped", modLoc("block/chair/warped"));

        withExistingParent("long_bench", modLoc("block/long_bench/single"));
        withExistingParent("red_lantern", modLoc("block/red_lantern"));

        withExistingParent("table_oak", modLoc("block/table/oak_single"));
        withExistingParent("table_spruce", modLoc("block/table/spruce_single"));
        withExistingParent("table_acacia", modLoc("block/table/acacia_single"));
        withExistingParent("table_bamboo", modLoc("block/table/bamboo_single"));
        withExistingParent("table_birch", modLoc("block/table/birch_single"));
        withExistingParent("table_cherry", modLoc("block/table/cherry_single"));
        withExistingParent("table_crimson", modLoc("block/table/crimson_single"));
        withExistingParent("table_dark_oak", modLoc("block/table/dark_oak_single"));
        withExistingParent("table_jungle", modLoc("block/table/jungle_single"));
        withExistingParent("table_mangrove", modLoc("block/table/mangrove_single"));
        withExistingParent("table_warped", modLoc("block/table/warped_single"));

        withExistingParent("chopping_board", modLoc("block/chopping_board"));
        withExistingParent("oil_block", modLoc("block/oil_block"));
        withExistingParent("straw_block", modLoc("block/straw_block"));

        Identifier fruitBasketFull = modLoc("item/fruit_basket_full");
        ItemModel.Unbaked fruitBasketItem = plainModel(basicItem(modLoc("fruit_basket_gui"), modLoc("item/fruit_basket")));
        registerClientItem(modLoc("fruit_basket"), select(new DisplayContext(),
                plainModel(parentModel(modLoc("fruit_basket"), fruitBasketFull)),
                when(ItemDisplayContext.GUI, fruitBasketItem),
                when(ItemDisplayContext.FIXED, fruitBasketItem),
                when(ItemDisplayContext.GROUND, fruitBasketItem)));

        ItemModel.Unbaked flatColdCutHamSlices = plainModel(basicItem(modLoc("cold_cut_ham_slices_gui")));
        Identifier blockColdCutHamSlices = modLoc("item/cold_cut_ham_slices_block");
        registerClientItem(modLoc("cold_cut_ham_slices"), select(new DisplayContext(),
                plainModel(parentModel(modLoc("cold_cut_ham_slices"), blockColdCutHamSlices)),
                when(ItemDisplayContext.GUI, flatColdCutHamSlices),
                when(ItemDisplayContext.FIXED, flatColdCutHamSlices)));

        ItemModel.Unbaked teapotItem = plainModel(basicItem(modLoc("teapot_gui"), modLoc("item/teapot")));
        Identifier teapotBlock = modLoc("item/teapot_3d");
        registerClientItem(modLoc("teapot"), select(new DisplayContext(),
                plainModel(parentModel(modLoc("teapot"), teapotBlock)),
                when(ItemDisplayContext.GUI, teapotItem),
                when(ItemDisplayContext.FIXED, teapotItem),
                when(ItemDisplayContext.GROUND, teapotItem)));
    }

    public Identifier handheldItem(Item item) {
        Identifier id = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
        Identifier model = handheldItem(id);
        registerClientItem(id, plainModel(model));
        return model;
    }

    public Identifier handheldItem(Identifier item) {
        Identifier texture = item.withPrefix("item/");
        return ModelTemplates.FLAT_HANDHELD_ITEM.create(texture, TextureMapping.layer0(new Material(texture)), models.modelOutput);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return Stream.empty();
    }

    private void basicItem(Item item) {
        Identifier id = BuiltInRegistries.ITEM.getKey(item);
        registerClientItem(id, plainModel(basicItem(id)));
    }

    private Identifier basicItem(Identifier item) {
        return basicItem(item, item.withPrefix("item/"));
    }

    private Identifier basicItem(Identifier item, Identifier texture) {
        return ModelTemplates.FLAT_ITEM.create(item.withPrefix("item/"), TextureMapping.layer0(new Material(texture)), models.modelOutput);
    }

    private void withExistingParent(String name, Identifier parent) {
        Identifier item = modLoc(name);
        registerClientItem(item, plainModel(parentModel(item, parent)));
    }

    private Identifier parentModel(Identifier item, Identifier parent) {
        return new ModelTemplate(Optional.of(parent), Optional.empty())
                .create(item.withPrefix("item/"), new TextureMapping(), models.modelOutput);
    }

    private void rangeModel(Identifier item, ItemModelProperty property, RangeEntry... overrides) {
        Identifier base = parentModel(item, overrides[0].model());
        List<RangeSelectItemModel.Entry> entries = Arrays.stream(overrides)
                .map(entry -> override(plainModel(entry.model()), entry.threshold()))
                .toList();
        registerClientItem(item, rangeSelect(property, plainModel(base), entries));
    }

    private void registerClientItem(Identifier item, ItemModel.Unbaked model) {
        models.itemModelOutput.register(item, new ClientItem(model, ClientItem.Properties.DEFAULT));
    }

    static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, path);
    }

    private record RangeEntry(float threshold, Identifier model) {
    }
}
