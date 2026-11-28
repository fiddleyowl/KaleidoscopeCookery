package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods.Food;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class BowlFoodOnlyItem extends FoodWithEffectsItem implements IHasContainer {
    public BowlFoodOnlyItem(Item.Properties itemProperties, Food properties) {
        super(itemProperties.usingConvertsTo(Items.BOWL), properties);
    }

    public BowlFoodOnlyItem(Item.Properties itemProperties, Food properties, Item craftingItem) {
        super(itemProperties, properties, craftingItem);
    }

    @Override
    public Item getContainerItem() {
        return Items.BOWL;
    }
}
