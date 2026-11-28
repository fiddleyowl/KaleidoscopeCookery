package com.github.ysbbbbbb.kaleidoscopecookery.item.quality;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModDataComponents;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import com.github.ysbbbbbb.kaleidoscopecookery.item.FoodWithEffectsItem;
import com.google.common.collect.Lists;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public final class QualityUtils {
    public static void setQuality(ItemStack food, Quality quality) {
        food.set(ModDataComponents.QUALITY, quality);
    }

    public static Quality getQuality(ItemStack food) {
        if (food.has(ModDataComponents.QUALITY)) {
            return food.get(ModDataComponents.QUALITY);
        }
        return Quality.STANDARD;
    }

    public static boolean hasQuality(ItemStack food) {
        return food.has(ModDataComponents.QUALITY);
    }

    public static List<MobEffectInstance> modifyEffects(List<MobEffectInstance> effectInstances, Quality quality) {
        List<MobEffectInstance> list = Lists.newArrayList();
        for (MobEffectInstance instance : effectInstances) {
            int duration = (int) Math.round(quality.getRatio() * instance.getDuration());
            if (duration > 0) {
                list.add(new MobEffectInstance(instance.getEffect(), duration, instance.getAmplifier()));
            }
        }
        return list;
    }

    public static @Nullable Consumable getFoodConsumable(ItemStack stack) {
        Consumable raw = stack.get(DataComponents.CONSUMABLE);
        if (raw == null) {
            return null;
        }
        if (shouldAdjustFood(stack)) {
            return modifyConsumable(raw, getQuality(stack));
        }
        return raw;
    }

    public static @Nullable FoodProperties getFoodProperties(ItemStack stack) {
        FoodProperties raw = stack.get(DataComponents.FOOD);
        if (raw == null) {
            return null;
        }
        if (shouldAdjustFood(stack)) {
            return modifyFoodProperties(raw, getQuality(stack));
        }
        return raw;
    }

    private static boolean shouldAdjustFood(ItemStack stack) {
        if (!hasQuality(stack)) {
            return false;
        }
        if (stack.getItem() instanceof FoodWithEffectsItem) {
            return true;
        }
        return stack.getItem() instanceof BowlFoodBlockItem;
    }

    private static Consumable modifyConsumable(Consumable consumable, Quality quality) {
        List<ConsumeEffect> effects = Lists.newArrayList();
        for (ConsumeEffect effect : consumable.onConsumeEffects()) {
            if (effect instanceof ApplyStatusEffectsConsumeEffect statusEffect) {
                if (statusEffect.probability() >= 1F) {
                    List<MobEffectInstance> modified = modifyEffects(statusEffect.effects(), quality);
                    if (!modified.isEmpty()) {
                        effects.add(new ApplyStatusEffectsConsumeEffect(modified, statusEffect.probability()));
                    }
                }
            } else {
                effects.add(effect);
            }
        }
        return new Consumable(consumable.consumeSeconds(), consumable.animation(), consumable.sound(),
                consumable.hasConsumeParticles(), effects);
    }

    public static FoodProperties modifyFoodProperties(FoodProperties raw, Quality quality) {
        double ratio = quality.getRatio();
        return new FoodProperties((int) Math.round(ratio * raw.nutrition()),
                (float) ratio * raw.saturation(), raw.canAlwaysEat());
    }

    public static ItemStack finishUsingFood(ItemStack stack, Level level, LivingEntity entity) {
        Consumable consumable = getFoodConsumable(stack);
        if (consumable == null) {
            return stack;
        }
        FoodProperties raw = stack.get(DataComponents.FOOD);
        FoodProperties adjusted = getFoodProperties(stack);
        if (Objects.equals(raw, adjusted)) {
            return consumable.onConsume(level, entity, stack);
        }

        // 原版进食直接从物品栈读取 FOOD，因此临时写入按品质计算的数值
        stack.set(DataComponents.FOOD, adjusted);
        try {
            return consumable.onConsume(level, entity, stack);
        } finally {
            stack.set(DataComponents.FOOD, raw);
        }
    }
}
