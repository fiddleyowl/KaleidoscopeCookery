package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods.Food;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class BowlFoodOnlyItem extends FoodWithEffectsItem implements IHasContainer {
    public BowlFoodOnlyItem(Food properties) {
        super(new Item.Properties().usingConvertsTo(Items.BOWL), properties);
    }

    public BowlFoodOnlyItem(Food properties, Item craftingItem) {
        super(properties, craftingItem);
    }

    @Override
    public Item getContainerItem() {
        return Items.BOWL;
    }
}
