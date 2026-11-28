package com.github.ysbbbbbb.kaleidoscopecookery.inventory.itemhandler;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenShelfBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class KitchenShelfItemHandler extends ItemStackHandler {
    public static final int CAPACITY = 256;
    private final KitchenShelfBlockEntity blockEntity;

    public KitchenShelfItemHandler(KitchenShelfBlockEntity blockEntity) {
        super(1);
        this.blockEntity = blockEntity;
    }

    @Override
    public int getSlotLimit(int slot) {
        return CAPACITY;
    }

    @Override
    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return CAPACITY;
    }

    @Override
    public void serialize(ValueOutput output) {
        ItemStack stack = stacks.getFirst();
        output.store("item", ItemStack.OPTIONAL_CODEC, stack.copyWithCount(1));
        output.putInt("count", stack.getCount());
    }

    @Override
    public void deserialize(ValueInput input) {
        int count = input.getIntOr("count", 0);
        ItemStack stack = input.read("item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
        this.stacks.set(0, stack.copyWithCount(count));
    }

    @Override
    protected void onContentsChanged(int slot) {
        this.blockEntity.refresh();
    }
}
