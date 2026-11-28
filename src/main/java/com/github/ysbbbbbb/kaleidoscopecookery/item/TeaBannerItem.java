package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.TeaBannerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;

import java.util.function.Consumer;

public class TeaBannerItem extends BlockItem {
    public TeaBannerItem() {
        super(ModBlocks.TEA_BANNER.get(), new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        var data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        DyeColor color = DyeColor.RED;
        ItemStack patternItem = ItemStack.EMPTY;
        if (data != null) {
            HolderLookup.Provider registries = context.registries();
            ValueInput input = TagValueInput.create(ProblemReporter.DISCARDING,
                    registries == null ? RegistryAccess.EMPTY : registries, data.copyTagWithoutId());
            color = TeaBannerBlockEntity.getColor(input);
            if (registries != null) {
                patternItem = TeaBannerBlockEntity.getPatternItem(input);
            }
        }

        MutableComponent pattern = Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.pattern.tea");
        if (patternItem.getItem() instanceof BannerPatternItem bannerPatternItem) {
            pattern = bannerPatternItem.getDisplayName();
        }
        MutableComponent colorName = Component.translatable("color.minecraft." + color.getName());

        tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.color", colorName)
                .withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.tea_banner.pattern", pattern)
                .withStyle(ChatFormatting.GRAY));
    }
}
