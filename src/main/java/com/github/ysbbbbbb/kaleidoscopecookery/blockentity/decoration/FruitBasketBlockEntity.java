package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FruitBasketBlockEntity extends BaseBlockEntity {
    public static final String ITEMS = "BasketItems";
    private final ItemStackHandler items = new ItemStackHandler(8);

    public FruitBasketBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.FRUIT_BASKET_BE.get(), pPos, pBlockState);
    }

    public void putOn(ItemStack stack) {
        if (!stack.getItem().canFitInsideContainerItems()) {
            return;
        }
        ItemStack reminder = ItemHandlerHelper.insertItemStacked(this.items, stack.copy(), false);
        if (stack.getCount() != reminder.getCount()) {
            stack.shrink(stack.getCount() - reminder.getCount());
            if (this.level != null) {
                this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
            }
            this.refresh();
        }
    }

    public void takeOut(Player player) {
        for (int i = 0; i < items.getSlots(); i++) {
            ItemStack stack = items.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ItemStack extractItem = items.extractItem(i, items.getSlotLimit(i), false);
                ItemHandlerHelper.giveItemToPlayer(player, extractItem);
                if (this.level != null) {
                    this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
                }
                this.refresh();
                return;
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putChild(ITEMS, this.items);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items.deserialize(input.childOrEmpty(ITEMS));
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public void setItems(ItemStackHandler items, RegistryAccess access) {
        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, access);
        items.serialize(output);
        this.items.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, access, output.buildResult()));
        this.refresh();
    }
}
