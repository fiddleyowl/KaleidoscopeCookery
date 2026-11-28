package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

import java.util.Map;

public class ModArmorMaterials {
    private static final ArmorMaterial FARMER = new ArmorMaterial(
            0,
            Map.of(ArmorType.HELMET, 3, ArmorType.CHESTPLATE, 4, ArmorType.LEGGINGS, 5, ArmorType.BOOTS, 2),
            12,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0, 0,
            ItemTags.REPAIRS_LEATHER_ARMOR,
            ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "cookery_farmer"))
    );

    public static Item.Properties farmerArmor(Item.Properties properties, ArmorType type) {
        return properties.stacksTo(1)
                .attributes(FARMER.createAttributes(type))
                .component(DataComponents.EQUIPPABLE, Equippable.builder(type.getSlot())
                        .setEquipSound(FARMER.equipSound())
                        .setAsset(FARMER.assetId())
                        .build())
                .repairable(Items.LEATHER);
    }
}
