package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.config.ClientConfig;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModFoods.Food;
import com.github.ysbbbbbb.kaleidoscopecookery.init.registry.CompatRegistry;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.Quality;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class FoodWithEffectsItem extends Item {
    private final List<MobEffectInstance> effectInstances;
    private final Function<Quality, List<MobEffectInstance>> effectCache;

    public FoodWithEffectsItem(Item.Properties itemProperties, Food properties, Item craftingItem) {
        this(itemProperties.craftRemainder(craftingItem), properties);
    }

    public FoodWithEffectsItem(Item.Properties itemProperties, Food properties) {
        super(properties.applyTo(itemProperties));
        this.effectInstances = properties.guaranteedEffects();
        this.effectCache = Util.memoize(quality -> QualityUtils.modifyEffects(this.effectInstances, quality));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        return QualityUtils.finishUsingFood(stack, level, entity);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String key = "tooltip.%s.%s.maxim".formatted(id.getNamespace(), id.getPath());
        MutableComponent full = Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC);
        // 先拿到纯文本，再按 \n 切
        String text = full.getString();
        for (String line : text.split("\n")) {
            if (!line.isEmpty()) {
                tooltip.accept(Component.literal(line).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
            } else {
                tooltip.accept(CommonComponents.EMPTY);
            }
        }

        boolean showEffect = !this.effectInstances.isEmpty()
                             && CompatRegistry.SHOW_POTION_EFFECT_TOOLTIPS
                             && ClientConfig.SHOW_FOOD_EFFECT_TOOLTIPS.get();

        // 品质
        if (QualityUtils.hasQuality(stack)) {
            Quality quality = QualityUtils.getQuality(stack);
            tooltip.accept(quality.getTooltip());
            if (showEffect) {
                tooltip.accept(CommonComponents.space());
                PotionContents.addPotionTooltip(this.effectCache.apply(quality), tooltip, 1.0F, context.tickRate());
            }
        } else {
            tooltip.accept(CommonComponents.space());
            PotionContents.addPotionTooltip(this.effectInstances, tooltip, 1.0F, context.tickRate());
        }
    }
}
