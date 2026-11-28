package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.inventory.tooltip.ItemContainerTooltip;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class FruitBasketItem extends BlockItem {
    private static final int MAX_SLOTS = 8;

    public FruitBasketItem(Properties properties) {
        super(ModBlocks.FRUIT_BASKET.get(), properties.stacksTo(1));
    }

    public static ItemStackHandler getItems(ItemStack stack) {
        ItemContainer container = stack.get(ModDataComponents.FRUIT_BASKET_ITEMS);
        if (container != null) {
            return container.items();
        }
        return new ItemStackHandler(MAX_SLOTS);
    }

    public static void saveItems(ItemStack stack, ItemStackHandler items) {
        stack.set(ModDataComponents.FRUIT_BASKET_ITEMS, new ItemContainer(items));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.fruit_basket").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        ItemContainer container = stack.get(ModDataComponents.FRUIT_BASKET_ITEMS);
        if (container != null) {
            return Optional.of(new ItemContainerTooltip(container.items()));
        }
        return Optional.empty();
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    public record ItemContainer(ItemStackHandler items) {
        public static final Codec<ItemContainer> CODEC = ItemStack.OPTIONAL_CODEC.listOf().xmap(
                list -> {
                    ItemStackHandler handler = new ItemStackHandler(8);
                    for (int i = 0; i < Math.min(list.size(), handler.getSlots()); i++) {
                        handler.setStackInSlot(i, list.get(i));
                    }
                    return new ItemContainer(handler);
                },
                container -> {
                    ItemStackHandler handler = container.items();
                    List<ItemStack> output = Lists.newArrayList();
                    for (int i = 0; i < handler.getSlots(); i++) {
                        output.add(handler.getStackInSlot(i));
                    }
                    return output;
                }
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainer> STREAM_CODEC = new StreamCodec<>() {
            @Override
            public ItemContainer decode(RegistryFriendlyByteBuf buffer) {
                CompoundTag compoundTag = buffer.readNbt();
                ItemStackHandler handler = new ItemStackHandler(8);
                if (compoundTag != null) {
                    handler.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, buffer.registryAccess(), compoundTag));
                }
                return new ItemContainer(handler);
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, ItemContainer value) {
                TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, buffer.registryAccess());
                value.items().serialize(output);
                buffer.writeNbt(output.buildResult());
            }
        };
    }
}
