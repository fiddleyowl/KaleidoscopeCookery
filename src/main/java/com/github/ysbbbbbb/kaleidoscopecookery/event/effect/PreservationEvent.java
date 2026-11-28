package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import static net.minecraft.world.effect.MobEffectCategory.HARMFUL;

@EventBusSubscriber(modid = KaleidoscopeCookery.MOD_ID)
public class PreservationEvent {
    @SubscribeEvent
    public static void onEatFood(LivingEntityUseItemEvent.Finish event) {
        ItemStack stack = event.getItem();
        LivingEntity entity = event.getEntity();
        if (stack.has(DataComponents.FOOD) && entity.hasEffect(ModEffects.PRESERVATION)) {
            Consumable consumable = QualityUtils.getFoodConsumable(stack);
            if (consumable == null) {
                return;
            }
            for (ConsumeEffect effect : consumable.onConsumeEffects()) {
                if (effect instanceof ApplyStatusEffectsConsumeEffect statusEffect) {
                    for (MobEffectInstance instance : statusEffect.effects()) {
                        Holder<MobEffect> holder = instance.getEffect();
                        if (holder.value().getCategory() == HARMFUL) {
                            entity.removeEffect(holder);
                        }
                    }
                }
            }
        }
    }
}
