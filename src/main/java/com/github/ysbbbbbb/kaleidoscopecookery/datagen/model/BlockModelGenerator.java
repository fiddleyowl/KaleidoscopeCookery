package com.github.ysbbbbbb.kaleidoscopecookery.datagen.model;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

import java.util.Optional;
import java.util.stream.IntStream;

import static com.github.ysbbbbbb.kaleidoscopecookery.datagen.model.ItemModelGenerator.modLoc;

public class BlockModelGenerator {
    private final BlockModelGenerators models;

    public BlockModelGenerator(BlockModelGenerators models) {
        this.models = models;
    }

    protected void registerModels() {
        Identifier stoveSide = modLoc("block/stove_side");
        Identifier stoveTop = modLoc("block/stove_top");
        Identifier stoveTopLit = modLoc("block/stove_top_lit");

        stove("stove", stoveTop, modLoc("block/stove_front"), stoveSide);
        stove("stove_lit", stoveTopLit, modLoc("block/stove_front_lit"), stoveSide);

        cross("block/chili_ristra/head", modLoc("block/chili_ristra/head"));
        cross("block/chili_ristra/body", modLoc("block/chili_ristra/body"));
        cross("block/chili_ristra/head_sheared", modLoc("block/chili_ristra/head_sheared"));
        cross("block/chili_ristra/body_sheared", modLoc("block/chili_ristra/body_sheared"));

        cross("block/strung_mushrooms/head", modLoc("block/strung_mushrooms/head"));
        cross("block/strung_mushrooms/body", modLoc("block/strung_mushrooms/body"));
        cross("block/strung_mushrooms/head_sheared", modLoc("block/strung_mushrooms/head_sheared"));
        cross("block/strung_mushrooms/body_sheared", modLoc("block/strung_mushrooms/body_sheared"));

        ModelTemplates.CUBE_COLUMN.create(modLoc("block/straw_block"), TextureMapping.column(
                new Material(modLoc("block/straw_block_side")), new Material(modLoc("block/straw_block_end"))), models.modelOutput);

        wood("oak");
        wood("spruce");
        wood("acacia");
        wood("bamboo");
        wood("birch");
        wood("cherry");
        wood("crimson");
        wood("dark_oak");
        wood("jungle");
        wood("mangrove");
        wood("warped");

        crop("tomato", 8);
        crop("chili", 8);
        crop("lettuce", 8);

        riceCrop();
        carpet();
    }

    public void wood(String name) {
        String cookStool = "block/cook_stool/" + name;
        withExistingParent(cookStool, modLoc("block/cook_stool/cook_stool"), modLoc(cookStool));

        String chair = "block/chair/" + name;
        withExistingParent(chair, modLoc("block/chair/chair"), modLoc(chair));

        String tableSingle = "block/table/" + name + "_single";
        Identifier tableTexture = modLoc("block/table/" + name);
        withExistingParent(tableSingle, modLoc("block/table/single"), tableTexture);

        String tableLeft = "block/table/" + name + "_left";
        withExistingParent(tableLeft, modLoc("block/table/left"), tableTexture);
        String tableRight = "block/table/" + name + "_right";
        withExistingParent(tableRight, modLoc("block/table/right"), tableTexture);
        String tableMiddle = "block/table/" + name + "_middle";
        withExistingParent(tableMiddle, modLoc("block/table/middle"), tableTexture);

        String tableLeftRot = "block/table/" + name + "_left_rot";
        withExistingParent(tableLeftRot, modLoc("block/table/left_rot"), tableTexture);
        String tableRightRot = "block/table/" + name + "_right_rot";
        withExistingParent(tableRightRot, modLoc("block/table/right_rot"), tableTexture);
        String tableMiddleRot = "block/table/" + name + "_middle_rot";
        withExistingParent(tableMiddleRot, modLoc("block/table/middle_rot"), tableTexture);
    }

    public void carpet() {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName();

            String chairCarpet = "block/carpet/chair/" + name;
            withExistingParent(chairCarpet, modLoc("block/carpet/chair/chair_carpet"), modLoc(chairCarpet));

            Identifier tableCarpetTexture = modLoc("block/carpet/table/" + name);
            String tableCarpetSingle = "block/carpet/table/" + name + "_single";
            withExistingParent(tableCarpetSingle, modLoc("block/carpet/table/single"), tableCarpetTexture);
            String tableCarpetLeft = "block/carpet/table/" + name + "_left";
            withExistingParent(tableCarpetLeft, modLoc("block/carpet/table/left"), tableCarpetTexture);
            String tableCarpetRight = "block/carpet/table/" + name + "_right";
            withExistingParent(tableCarpetRight, modLoc("block/carpet/table/right"), tableCarpetTexture);
            String tableCarpetMiddle = "block/carpet/table/" + name + "_middle";
            withExistingParent(tableCarpetMiddle, modLoc("block/carpet/table/middle"), tableCarpetTexture);
        }
    }

    public void crop(String name, int stage) {
        IntStream.range(0, stage).forEach(i -> {
            String id = "block/crop/%s/stage%d".formatted(name, i);
            new ModelTemplate(Optional.of(modLoc("block/cross")), Optional.empty(), TextureSlot.CROSS)
                    .create(modLoc(id), TextureMapping.cross(new Material(modLoc(id))), models.modelOutput);
        });
    }

    public void riceCrop() {
        IntStream.range(0, 8).forEach(i -> {
            String down = "block/crop/rice/stage%d_down".formatted(i);
            String middle = "block/crop/rice/stage%d_middle".formatted(i);
            String up = "block/crop/rice/stage%d_up".formatted(i);
            cross(down, modLoc(down));
            cross(middle, modLoc(middle));
            cross(up, modLoc(up));
        });
    }

    private void stove(String name, Identifier top, Identifier front, Identifier side) {
        Material topMaterial = new Material(top);
        Material sideMaterial = new Material(side);
        ModelTemplates.CUBE.create(modLoc("block/" + name), new TextureMapping()
                .put(TextureSlot.DOWN, topMaterial)
                .put(TextureSlot.UP, topMaterial)
                .put(TextureSlot.NORTH, new Material(front))
                .put(TextureSlot.SOUTH, sideMaterial)
                .put(TextureSlot.EAST, sideMaterial)
                .put(TextureSlot.WEST, sideMaterial)
                .put(TextureSlot.PARTICLE, sideMaterial), models.modelOutput);
    }

    private void withExistingParent(String name, Identifier parent, Identifier particle) {
        new ModelTemplate(Optional.of(parent), Optional.empty(), TextureSlot.PARTICLE)
                .create(modLoc(name), TextureMapping.particle(new Material(particle)), models.modelOutput);
    }

    private void cross(String name, Identifier texture) {
        ModelTemplates.CROSS.create(modLoc(name), TextureMapping.cross(new Material(texture)), models.modelOutput);
    }
}
