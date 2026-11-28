package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.RiceCropBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.crop.TeaTreeBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.decoration.*;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.EmptyCupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.drink.TeacupBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteOneByTwoBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.*;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.ChiliRistraBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.misc.StrungMushroomsBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.FoodBiteRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.PlateRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.TeacupRegistry;
import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.PropertyValueList;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.github.ysbbbbbb.kaleidoscopecookery.datagen.model.ItemModelGenerator.modLoc;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;
import static net.minecraft.client.data.models.BlockModelGenerators.variant;

public class BlockStateGenerator {
    private final BlockModelGenerators models;

    public BlockStateGenerator(BlockModelGenerators models) {
        this.models = models;
    }

    protected void registerStatesAndModels() {
        horizontalBlock(ModBlocks.STOVE.get(), blockState -> {
            if (blockState.getValue(StoveBlock.LIT)) {
                return modLoc("block/stove_lit");
            } else {
                return modLoc("block/stove");
            }
        });

        horizontalBlock(ModBlocks.POT.get(), blockState -> {
            if (blockState.getValue(PotBlock.HAS_OIL) && blockState.getValue(PotBlock.SHOW_OIL)) {
                if (blockState.getValue(PotBlock.HAS_BASE)) {
                    return modLoc("block/pot_base_has_oil");
                }
                return modLoc("block/pot_has_oil");
            } else {
                if (blockState.getValue(PotBlock.HAS_BASE)) {
                    return modLoc("block/pot_base");
                }
                return modLoc("block/pot");
            }
        });

        horizontalBlock(ModBlocks.STOCKPOT.get(), blockState -> {
            if (blockState.getValue(StockpotBlock.HAS_LID)) {
                if (blockState.getValue(StockpotBlock.HAS_BASE)) {
                    return modLoc("block/stockpot_base_has_lid");
                }
                if (blockState.getValue(StockpotBlock.HAS_CHAINS)) {
                    return modLoc("block/stockpot_chains_has_lid");
                }
                return modLoc("block/stockpot_has_lid");
            } else {
                if (blockState.getValue(StockpotBlock.HAS_BASE)) {
                    return modLoc("block/stockpot_base");
                }
                if (blockState.getValue(StockpotBlock.HAS_CHAINS)) {
                    return modLoc("block/stockpot_chains");
                }
                return modLoc("block/stockpot");
            }
        });

        horizontalBlock(ModBlocks.SHAWARMA_SPIT.get(), blockState -> {
            if (blockState.getValue(ShawarmaSpitBlock.POWERED)) {
                if (blockState.getValue(ShawarmaSpitBlock.HALF) == DoubleBlockHalf.LOWER) {
                    return modLoc("block/shawarma_spit_powered_lower");
                }
                return modLoc("block/shawarma_spit_powered_upper");
            } else {
                if (blockState.getValue(ShawarmaSpitBlock.HALF) == DoubleBlockHalf.LOWER) {
                    return modLoc("block/shawarma_spit_lower");
                }
                return modLoc("block/shawarma_spit_upper");
            }
        });

        horizontalBlock(ModBlocks.OIL_POT.get(), blockState -> {
            if (blockState.getValue(OilPotBlock.HAS_OIL)) {
                return modLoc("block/oil_pot_with_oil");
            } else {
                return modLoc("block/oil_pot");
            }
        });

        horizontalBlock(ModBlocks.STEAMER.get(), blockState -> {
            boolean hasLid = blockState.getValue(SteamerBlock.HAS_LID);
            boolean hasBase = blockState.getValue(SteamerBlock.HAS_BASE);
            boolean half = blockState.getValue(SteamerBlock.HALF);

            if (hasLid && hasBase && half) {
                return modLoc("block/steamer_half_lid_base");
            } else if (hasLid && hasBase) {
                return modLoc("block/steamer_full_lid_base");
            } else if (hasLid && half) {
                return modLoc("block/steamer_half_lid");
            } else if (hasLid) {
                return modLoc("block/steamer_full_lid");
            } else if (hasBase && half) {
                return modLoc("block/steamer_half_base");
            } else if (hasBase) {
                return modLoc("block/steamer_full_base");
            } else if (half) {
                return modLoc("block/steamer_half");
            } else {
                return modLoc("block/steamer_full");
            }
        });

        FoodBiteRegistry.FOOD_DATA_MAP.forEach((key, value) -> {
            Block block = BuiltInRegistries.BLOCK.getValue(key);
            if (value.blockType() == FoodBiteRegistry.BlockType.ONE_BY_TWO) {
                addOneByTwoFoodBiteBlock(block, key);
            } else {
                addFoodBiteBlock(block, key);
            }
        });

        PlateRegistry.PLATE_DATA_MAP.forEach((key, value) -> {
            Block block = BuiltInRegistries.BLOCK.getValue(key);
            addPlateBlock(block, key);
        });

        horizontalBlock(ModBlocks.EMPTY_CUP.get(), blockState -> {
            int count = blockState.getValue(EmptyCupBlock.CUP_COUNT);
            Identifier model = modLoc("block/teacup/empty_cup/count%d".formatted(count));
            return model;
        });

        horizontalBlock(ModBlocks.CLAY_POT_MILK_TEA.get(),
                modLoc("block/clay_pot_milk_tea"));

        TeacupRegistry.TEACUP_DATA_MAP.forEach((key, value) -> {
            Block block = BuiltInRegistries.BLOCK.getValue(key);
            addTeacupBlock(block, key);
        });

        addStackableFoodBlock(ModBlocks.BAMBOO_TUBE_RICE.get(), modLoc("bamboo_tube_rice"));

        horizontalBlock(ModBlocks.FRUIT_BASKET.get(), modLoc("block/fruit_basket"));
        horizontalBlock(ModBlocks.CHOPPING_BOARD.get(), modLoc("block/chopping_board"));
        horizontalBlock(ModBlocks.KITCHENWARE_RACKS.get(), modLoc("block/kitchenware_racks"));

        // 果串
        candiedSkewer(ModBlocks.CANDIED_GLOW_BERRY_SKEWER_UPRIGHT.get(), "candied_glow_berry_skewer", true);
        candiedSkewer(ModBlocks.CANDIED_GLOW_BERRY_SKEWER_SIDE.get(), "candied_glow_berry_skewer", false);
        candiedSkewer(ModBlocks.CANDIED_SWEET_BERRY_SKEWER_UPRIGHT.get(), "candied_sweet_berry_skewer", true);
        candiedSkewer(ModBlocks.CANDIED_SWEET_BERRY_SKEWER_SIDE.get(), "candied_sweet_berry_skewer", false);
        candiedSkewer(ModBlocks.CANDIED_CHORUS_FRUIT_SKEWER_UPRIGHT.get(), "candied_chorus_fruit_skewer", true);
        candiedSkewer(ModBlocks.CANDIED_CHORUS_FRUIT_SKEWER_SIDE.get(), "candied_chorus_fruit_skewer", false);
        candiedSkewer(ModBlocks.CANDIED_APPLE_SKEWER_UPRIGHT.get(), "candied_apple_skewer", true);
        candiedSkewer(ModBlocks.CANDIED_APPLE_SKEWER_SIDE.get(), "candied_apple_skewer", false);
        candiedSkewer(ModBlocks.CANDIED_CATERPILLAR_SKEWER_UPRIGHT.get(), "candied_caterpillar_skewer", true);
        candiedSkewer(ModBlocks.CANDIED_CATERPILLAR_SKEWER_SIDE.get(), "candied_caterpillar_skewer", false);

        horizontalBlock(ModBlocks.KITCHEN_SHELF.get(), state ->
                modLoc("block/kitchen_shelf/" + state.getValue(KitchenShelfBlock.PART).getSerializedName()));

        variantBlock(ModBlocks.BAMBOO_TRAY.get(), state -> modLoc(
                state.getValue(BambooTrayBlock.STAND)
                        ? "block/bamboo_tray/stand"
                        : "block/bamboo_tray/base"
        ));

        cookStool(ModBlocks.COOK_STOOL_OAK, "oak");
        cookStool(ModBlocks.COOK_STOOL_SPRUCE, "spruce");
        cookStool(ModBlocks.COOK_STOOL_ACACIA, "acacia");
        cookStool(ModBlocks.COOK_STOOL_BAMBOO, "bamboo");
        cookStool(ModBlocks.COOK_STOOL_BIRCH, "birch");
        cookStool(ModBlocks.COOK_STOOL_CHERRY, "cherry");
        cookStool(ModBlocks.COOK_STOOL_CRIMSON, "crimson");
        cookStool(ModBlocks.COOK_STOOL_DARK_OAK, "dark_oak");
        cookStool(ModBlocks.COOK_STOOL_JUNGLE, "jungle");
        cookStool(ModBlocks.COOK_STOOL_MANGROVE, "mangrove");
        cookStool(ModBlocks.COOK_STOOL_WARPED, "warped");

        chair(ModBlocks.CHAIR_OAK, "oak");
        chair(ModBlocks.CHAIR_SPRUCE, "spruce");
        chair(ModBlocks.CHAIR_ACACIA, "acacia");
        chair(ModBlocks.CHAIR_BAMBOO, "bamboo");
        chair(ModBlocks.CHAIR_BIRCH, "birch");
        chair(ModBlocks.CHAIR_CHERRY, "cherry");
        chair(ModBlocks.CHAIR_CRIMSON, "crimson");
        chair(ModBlocks.CHAIR_DARK_OAK, "dark_oak");
        chair(ModBlocks.CHAIR_JUNGLE, "jungle");
        chair(ModBlocks.CHAIR_MANGROVE, "mangrove");
        chair(ModBlocks.CHAIR_WARPED, "warped");

        longBench(ModBlocks.LONG_BENCH);
        simpleBlock(ModBlocks.RED_LANTERN.get(), modLoc("block/red_lantern"));

        table(ModBlocks.TABLE_OAK, "oak");
        table(ModBlocks.TABLE_SPRUCE, "spruce");
        table(ModBlocks.TABLE_ACACIA, "acacia");
        table(ModBlocks.TABLE_BAMBOO, "bamboo");
        table(ModBlocks.TABLE_BIRCH, "birch");
        table(ModBlocks.TABLE_CHERRY, "cherry");
        table(ModBlocks.TABLE_CRIMSON, "crimson");
        table(ModBlocks.TABLE_DARK_OAK, "dark_oak");
        table(ModBlocks.TABLE_JUNGLE, "jungle");
        table(ModBlocks.TABLE_MANGROVE, "mangrove");
        table(ModBlocks.TABLE_WARPED, "warped");

        eightImmortalsTable(ModBlocks.EIGHT_IMMORTALS_TABLE);

        simpleBlock(ModBlocks.OIL_BLOCK.get(), ModelTemplates.CUBE_ALL.create(ModBlocks.OIL_BLOCK.get(),
                TextureMapping.cube(ModBlocks.OIL_BLOCK.get()), models.modelOutput));

        crop(ModBlocks.TOMATO_CROP, "tomato");
        crop(ModBlocks.CHILI_CROP, "chili");
        crop(ModBlocks.LETTUCE_CROP, "lettuce");

        teaTree();

        axisBlock((RotatedPillarBlock) ModBlocks.STRAW_BLOCK.get());

        horizontalFaceBlock(ModBlocks.RECIPE_BLOCK.get(), modLoc("block/recipe_block"));

        simpleBlock(ModBlocks.TEA_BANNER.get(), ModelTemplates.PARTICLE_ONLY.create(ModBlocks.TEA_BANNER.get(),
                TextureMapping.particle(new Material(modLoc("item/tea_banner"))), models.modelOutput));

        riceCrop();

        variantBlock(ModBlocks.ENAMEL_BASIN.get(), blockState -> {
            if (blockState.getValue(EnamelBasinBlock.HAS_LID)) {
                return modLoc("block/enamel_basin/base");
            }
            int oilCount = blockState.getValue(EnamelBasinBlock.OIL_COUNT);
            if (oilCount <= 0) {
                return modLoc("block/enamel_basin/empty");
            }
            if (oilCount <= EnamelBasinBlock.MAX_OIL_COUNT / 3) {
                return modLoc("block/enamel_basin/low");
            }
            if (oilCount <= EnamelBasinBlock.MAX_OIL_COUNT / 3 * 2) {
                return modLoc("block/enamel_basin/middle");
            }
            return modLoc("block/enamel_basin/high");
        });

        variantBlock(ModBlocks.CHILI_RISTRA.get(), blockState -> {
            if (blockState.getValue(ChiliRistraBlock.IS_HEAD)) {
                if (blockState.getValue(ChiliRistraBlock.SHEARED)) {
                    return modLoc("block/chili_ristra/head_sheared");
                }
                return modLoc("block/chili_ristra/head");
            } else {
                if (blockState.getValue(ChiliRistraBlock.SHEARED)) {
                    return modLoc("block/chili_ristra/body_sheared");
                }
                return modLoc("block/chili_ristra/body");
            }
        });

        variantBlock(ModBlocks.STRUNG_MUSHROOMS.get(), blockState -> {
            if (blockState.getValue(StrungMushroomsBlock.IS_HEAD)) {
                if (blockState.getValue(StrungMushroomsBlock.SHEARED)) {
                    return modLoc("block/strung_mushrooms/head_sheared");
                }
                return modLoc("block/strung_mushrooms/head");
            } else {
                if (blockState.getValue(StrungMushroomsBlock.SHEARED)) {
                    return modLoc("block/strung_mushrooms/body_sheared");
                }
                return modLoc("block/strung_mushrooms/body");
            }
        });
    }

    public void variantBlock(Block block, Function<BlockState, Identifier> modelFunc) {
        forAllStates(block, state -> new Variant(modelFunc.apply(state)));
    }

    public void crop(DeferredBlock<Block> block, String name) {
        forAllStates(block.get(), state -> {
            int age = state.getValue(CropBlock.AGE);
            Identifier file = modLoc("block/crop/%s/stage%d".formatted(name, age));
            return new Variant(file);
        });
    }

    public void teaTree() {
        forAllStates(ModBlocks.TEA_TREE.get(), state -> {
            int age = state.getValue(TeaTreeBlock.AGE);
            Identifier file = modLoc("block/crop/tea_tree/stage%d".formatted(age));
            return new Variant(file);
        });
    }

    public void riceCrop() {
        forAllStates(ModBlocks.RICE_CROP.get(), state -> {
            int age = state.getValue(CropBlock.AGE);
            int location = state.getValue(RiceCropBlock.LOCATION);
            Identifier file;
            if (location == RiceCropBlock.DOWN) {
                file = modLoc("block/crop/rice/stage%d_down".formatted(age));
            } else if (location == RiceCropBlock.MIDDLE) {
                file = modLoc("block/crop/rice/stage%d_middle".formatted(age));
            } else {
                file = modLoc("block/crop/rice/stage%d_up".formatted(age));
            }
            return new Variant(file);
        });
    }

    public void cookStool(DeferredBlock<Block> block, String name) {
        horizontalBlock(block.get(), modLoc("block/cook_stool/" + name));
    }

    public void chair(DeferredBlock<Block> block, String name) {
        horizontalBlock(block.get(), modLoc("block/chair/" + name));
    }

    private void longBench(DeferredBlock<Block> block) {
        forAllStates(block.get(), state -> {
            String modelName = switch (state.getValue(LongBenchBlock.POSITION)) {
                case LongBenchBlock.LEFT -> "left";
                case LongBenchBlock.MIDDLE -> "middle";
                case LongBenchBlock.RIGHT -> "right";
                default -> "single";
            };
            return new Variant(modLoc("block/long_bench/" + modelName))
                    .withYRot(state.getValue(LongBenchBlock.AXIS) == Direction.Axis.Z ? Quadrant.R90 : Quadrant.R0);
        });
    }

    private void table(DeferredBlock<Block> block, String name) {
        Identifier leftModel = modLoc("block/table/%s_left".formatted(name));
        Identifier rightModel = modLoc("block/table/%s_right".formatted(name));
        Identifier middleModel = modLoc("block/table/%s_middle".formatted(name));

        Identifier leftModelRot = modLoc("block/table/%s_left_rot".formatted(name));
        Identifier rightModelRot = modLoc("block/table/%s_right_rot".formatted(name));
        Identifier middleModelRot = modLoc("block/table/%s_middle_rot".formatted(name));


        forAllStates(block.get(), blockState -> {
            int position = blockState.getValue(TableBlock.POSITION);
            if (position == TableBlock.SINGLE) {
                return new Variant(modLoc("block/table/%s_single".formatted(name)));
            }
            boolean isRotation = blockState.getValue(TableBlock.AXIS) == Direction.Axis.Z;
            if (position == TableBlock.LEFT) {
                if (isRotation) {
                    return new Variant(rightModelRot);
                } else {
                    return new Variant(rightModel);
                }
            }
            if (position == TableBlock.RIGHT) {
                if (isRotation) {
                    return new Variant(leftModelRot);
                } else {
                    return new Variant(leftModel);
                }
            }
            if (isRotation) {
                return new Variant(middleModelRot);
            } else {
                return new Variant(middleModel);
            }
        });
    }

    private void eightImmortalsTable(DeferredBlock<Block> block) {
        forAllStates(block.get(), state -> {
            Direction facing = state.getValue(EightImmortalsTableBlock.FACING);
            String part = state.getValue(EightImmortalsTableBlock.PART).getSerializedName();
            Quadrant rotation = Quadrant.values()[(int) facing.toYRot() / 90];
            return new Variant(modLoc("block/eight_immortals_table/%s".formatted(part))).withYRot(rotation);
        });
    }

    public void addTeacupBlock(Block block, Identifier id) {
        horizontalBlock(block, blockState -> {
            if (!(blockState.getBlock() instanceof TeacupBlock teacupBlock)) {
                throw new IllegalArgumentException("Block must be an instance of TeacupBlock");
            }

            int cupCount = blockState.getValue(teacupBlock.getCupCountProperty());
            int teaCount = blockState.getValue(teacupBlock.getTeaCountProperty());
            if (teaCount > cupCount) {
                teaCount = cupCount;
            }

            String modelName = "count%d_%d".formatted(cupCount, teaCount);
            String modelPath = "block/teacup/%s/%s".formatted(id.getPath(), modelName);
            Identifier modelLoc = Identifier.fromNamespaceAndPath(id.getNamespace(), modelPath);
            return modelLoc;
        });
    }

    public void addFoodBiteBlock(Block block, Identifier id) {
        horizontalBlock(block, blockState -> {
            if (!(blockState.getBlock() instanceof FoodBiteBlock foodBiteBlock)) {
                throw new IllegalArgumentException("Block must be an instance of FoodBiteBlock");
            }
            int bites = blockState.getValue(foodBiteBlock.getBites());
            Identifier model = Identifier.fromNamespaceAndPath(
                    id.getNamespace(),
                    "block/food/%s/%s_%d".formatted(id.getPath(), id.getPath(), bites)
            );
            return model;
        });
    }

    public void addOneByTwoFoodBiteBlock(Block block, Identifier id) {
        horizontalBlock(block, blockState -> {
            if (!(blockState.getBlock() instanceof FoodBiteOneByTwoBlock foodBiteBlock)) {
                throw new IllegalArgumentException("Block must be an instance of OneByTwoFoodBiteBlock");
            }
            int bites = blockState.getValue(foodBiteBlock.getBites());
            int position = blockState.getValue(FoodBiteOneByTwoBlock.POSITION);
            Identifier model;
            if (position == FoodBiteOneByTwoBlock.LEFT) {
                model = Identifier.fromNamespaceAndPath(
                        id.getNamespace(),
                        "block/food/%s/%s_left_%d".formatted(id.getPath(), id.getPath(), bites)
                );
            } else {
                model = Identifier.fromNamespaceAndPath(
                        id.getNamespace(),
                        "block/food/%s/%s_right_%d".formatted(id.getPath(), id.getPath(), bites)
                );
            }
            return model;
        });
    }

    public void addStackableFoodBlock(Block block, Identifier id) {
        horizontalBlock(block, blockState -> {
            if (!(blockState.getBlock() instanceof StackableFoodBlock stackableFoodBlock)) {
                throw new IllegalArgumentException("Block must be an instance of StackableFoodBlock");
            }
            int count = blockState.getValue(stackableFoodBlock.getCountProperty());
            Identifier model = Identifier.fromNamespaceAndPath(
                    id.getNamespace(), "block/food/%s/%s_%d".formatted(id.getPath(), id.getPath(), count)
            );
            return model;
        });
    }

    public void addPlateBlock(Block block, Identifier id) {
        horizontalBlock(block, blockState -> {
            if (!(blockState.getBlock() instanceof PlateBlock plateBlock)) {
                throw new IllegalArgumentException("Block must be an instance of PlateBlock");
            }
            int count = blockState.getValue(plateBlock.getServingsProperty());
            Identifier model = Identifier.fromNamespaceAndPath(
                    id.getNamespace(), "block/plate/%s/%s_%d".formatted(id.getPath(), id.getPath(), count)
            );
            return model;
        });
    }

    private void candiedSkewer(Block block, String name, boolean upright) {
        if (!(block instanceof CandiedSkewerBlock skewerBlock)) {
            return;
        }
        horizontalBlock(block, state -> {
            int count = state.getValue(skewerBlock.getCountProperty());
            String shape = upright ? "upright" : "side";
            return modLoc("block/candied_skewers/%s/%s_%d".formatted(name, shape, count));
        });
    }

    private void simpleBlock(Block block, Identifier model) {
        models.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, plainVariant(model)));
    }

    private void axisBlock(RotatedPillarBlock block) {
        Identifier horizontal = ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, TextureMapping.column(
                new Material(modLoc("block/straw_block_side")), new Material(modLoc("block/straw_block_end"))), models.modelOutput);
        models.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.AXIS)
                        .select(Direction.Axis.Y, plainVariant(modLoc("block/straw_block")))
                        .select(Direction.Axis.Z, variant(new Variant(horizontal).withXRot(Quadrant.R90)))
                        .select(Direction.Axis.X, variant(new Variant(horizontal).withXRot(Quadrant.R90).withYRot(Quadrant.R90)))));
    }

    private void horizontalBlock(Block block, Identifier model) {
        horizontalBlock(block, state -> model);
    }

    private void horizontalBlock(Block block, Function<BlockState, Identifier> modelFunc) {
        forAllStates(block, state -> new Variant(modelFunc.apply(state)).withYRot(yRotation(state, 180)));
    }

    private void horizontalFaceBlock(Block block, Identifier model) {
        forAllStates(block, state -> {
            AttachFace face = state.getValue(BlockStateProperties.ATTACH_FACE);
            return new Variant(model)
                    .withXRot(Quadrant.values()[face.ordinal()])
                    .withYRot(yRotation(state, face == AttachFace.CEILING ? 360 : 180));
        });
    }

    private static Quadrant yRotation(BlockState state, int angleOffset) {
        int rotation = (int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + angleOffset;
        return Quadrant.values()[(rotation % 360) / 90];
    }

    private void forAllStates(Block block, Function<BlockState, Variant> modelFunc) {
        Map<String, BlockStateModel.Unbaked> variants = new LinkedHashMap<>();
        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            String key = new PropertyValueList(state.getValues().toList()).getKey();
            variants.put(key, variant(modelFunc.apply(state)).toUnbaked());
        }
        models.blockStateOutput.accept(new BlockVariants(block, variants));
    }

    private record BlockVariants(Block block, Map<String, BlockStateModel.Unbaked> variants) implements BlockModelDefinitionGenerator {
        @Override
        public BlockStateModelDispatcher create() {
            return new BlockStateModelDispatcher(Optional.of(new BlockStateModelDispatcher.SimpleModelSelectors(variants)), Optional.empty());
        }
    }
}
