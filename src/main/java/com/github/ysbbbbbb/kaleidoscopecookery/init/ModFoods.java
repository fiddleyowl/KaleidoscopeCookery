package com.github.ysbbbbbb.kaleidoscopecookery.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects.*;
import static net.minecraft.world.effect.MobEffects.*;

public interface ModFoods {
    // 26.1 分别表达 FoodProperties 和 Consumable 。把他们放在同一份定义里
    record Food(FoodProperties properties, Consumable consumable) {
        public Item.Properties applyTo(Item.Properties itemProperties) {
            return itemProperties.food(this.properties, this.consumable);
        }

        public List<MobEffectInstance> guaranteedEffects() {
            List<MobEffectInstance> effects = new ArrayList<>();
            for (ConsumeEffect effect : this.consumable.onConsumeEffects()) {
                if (!(effect instanceof ApplyStatusEffectsConsumeEffect statusEffect)) {
                    continue;
                }
                if (statusEffect.probability() >= 1F) {
                    effects.addAll(statusEffect.effects());
                }
            }
            return Collections.unmodifiableList(effects);
        }
    }

    final class Builder {
        private final FoodProperties.Builder food = new FoodProperties.Builder();
        private final Consumable.Builder consumable = Consumable.builder();

        public Builder nutrition(int nutrition) {
            this.food.nutrition(nutrition);
            return this;
        }

        public Builder saturationModifier(float saturation) {
            this.food.saturationModifier(saturation);
            return this;
        }

        public Builder alwaysEdible() {
            this.food.alwaysEdible();
            return this;
        }

        public Builder fast() {
            this.consumable.consumeSeconds(0.8F);
            return this;
        }

        public Builder effect(Supplier<MobEffectInstance> effect, float probability) {
            this.consumable.onConsume(new ApplyStatusEffectsConsumeEffect(effect.get(), probability));
            return this;
        }

        public Food build() {
            return new Food(this.food.build(), this.consumable.build());
        }
    }

    // 番茄
    Food TOMATO = (new Builder())
            .nutrition(2).saturationModifier(0.5F)
            .alwaysEdible().build();

    // 辣椒
    Food CHILI = (new Builder())
            .nutrition(1).saturationModifier(0)
            .alwaysEdible().build();

    // 生菜
    Food LETTUCE = (new Builder())
            .nutrition(2).saturationModifier(0)
            .alwaysEdible().build();

    // 猪儿虫
    Food CATERPILLAR = (new Builder())
            .nutrition(18).saturationModifier(0.2F).alwaysEdible()
            .effect(() -> new MobEffectInstance(NAUSEA, 200), 1F)
            .build();

    // 刺身
    Food SASHIMI = (new Builder())
            .nutrition(1).saturationModifier(0.5F)
            .alwaysEdible().build();

    // 生羊排
    Food RAW_LAMB_CHOPS = (new Builder())
            .nutrition(1).saturationModifier(0.5F)
            .alwaysEdible().build();

    // 生牛杂
    Food RAW_COW_OFFAL = (new Builder())
            .nutrition(2).saturationModifier(0.3F)
            .alwaysEdible().build();

    // 生五花肉
    Food RAW_PORK_BELLY = (new Builder())
            .nutrition(2).saturationModifier(0.3F)
            .alwaysEdible().build();

    // 生驴肉
    Food RAW_DONKEY_MEAT = (new Builder())
            .nutrition(2).saturationModifier(0.3F)
            .alwaysEdible().build();

    // 生切制小肉
    Food RAW_CUT_SMALL_MEATS = (new Builder())
            .nutrition(2).saturationModifier(0.3F)
            .alwaysEdible().build();

    // 生丸子
    Food RAW_MEATBALL = (new Builder())
            .nutrition(4).saturationModifier(0.3F)
            .alwaysEdible().build();

    // 熟羊排
    Food COOKED_LAMB_CHOPS = (new Builder())
            .nutrition(3).saturationModifier(0.8F)
            .alwaysEdible().build();

    // 熟牛杂
    Food COOKED_COW_OFFAL = (new Builder())
            .nutrition(4).saturationModifier(0.8F)
            .alwaysEdible().build();

    // 熟五花肉
    Food COOKED_PORK_BELLY = (new Builder())
            .nutrition(4).saturationModifier(0.8F)
            .alwaysEdible().build();

    // 熟驴肉
    Food COOKED_DONKEY_MEAT = (new Builder())
            .nutrition(6).saturationModifier(0.8F)
            .alwaysEdible().build();

    // 熟切制小肉
    Food COOKED_CUT_SMALL_MEATS = (new Builder())
            .nutrition(4).saturationModifier(0.8F)
            .alwaysEdible().build();

    // 熟丸子
    Food COOKED_MEATBALL = (new Builder())
            .nutrition(8).saturationModifier(0.8F)
            .alwaysEdible().build();

    // 驴肉火烧
    Food DONKEY_BURGER = (new Builder())
            .nutrition(12).saturationModifier(0.8F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 45 * 20), 1.0F)
            .alwaysEdible().build();

    // 包子
    Food BAOZI = (new Builder())
            .nutrition(8).saturationModifier(1)
            .effect(() -> new MobEffectInstance(ABSORPTION, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 饺子
    Food DUMPLING = (new Builder())
            .nutrition(8).saturationModifier(1)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 烤包子
    Food SAMSA = (new Builder())
            .nutrition(8).saturationModifier(1)
            .effect(() -> new MobEffectInstance(HASTE, 100 * 20, 1), 1.0F)
            .alwaysEdible().build();

    // 馒头
    Food MANTOU = (new Builder())
            .nutrition(6).saturationModifier(0.9F)
            .fast().alwaysEdible().build();

    // 馅饼
    Food MEAT_PIE = (new Builder())
            .nutrition(8).saturationModifier(1)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 青团
    Food QINGTUAN = (new Builder())
            .nutrition(5).saturationModifier(0.6F)
            .alwaysEdible().build();

    // 牛皮糖
    Food STICKY_CANDY = (new Builder())
            .nutrition(6).saturationModifier(1)
            .alwaysEdible().build();

    // 糍粑
    Food STICKY_RICE_CAKE = (new Builder())
            .nutrition(8).saturationModifier(0.875F)
            .alwaysEdible().build();

    // 粽子
    Food ZONGZI = (new Builder())
            .nutrition(8).saturationModifier(0.625F)
            .effect(() -> new MobEffectInstance(REGENERATION, 20 * 20), 1.0F)
            .alwaysEdible().build();

    // 竹筒饭
    Food RAW_BAMBOO_TUBE_RICE = (new Builder())
            .nutrition(10).saturationModifier(0.5F)
            .alwaysEdible().build();

    Food BAMBOO_TUBE_RICE = (new Builder())
            .nutrition(10).saturationModifier(0.5F)
            .alwaysEdible().build();

    // 牛肉面
    Food BEEF_NOODLE = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(VITALITY, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 烩面
    Food HUI_NOODLE = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(VITALITY, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 乌冬面
    Food UDON_NOODLE = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(VITALITY, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 热干面
    Food HOT_DRY_NOODLES = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(VITALITY, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 腊八粥
    Food LABA_CONGEE = (new Builder())
            .nutrition(12).saturationModifier(0.5F)
            .effect(() -> new MobEffectInstance(WARMTH, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 煎蛋
    Food FRIED_EGG = (new Builder())
            .nutrition(4).saturationModifier(0.5F)
            .alwaysEdible().build();

    // 茶叶蛋
    Food TEA_EGG = (new Builder())
            .nutrition(4).saturationModifier(0.5F)
            .effect(() -> new MobEffectInstance(SULFUR, 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 黑暗料理
    Food DARK_CUISINE_BLOCK = (new Builder())
            .nutrition(1).saturationModifier(0).alwaysEdible()
            .effect(() -> new MobEffectInstance(BLINDNESS, 300), 0.33F)
            .effect(() -> new MobEffectInstance(POISON, 100), 0.33F)
            .build();

    Food DARK_CUISINE_ITEM = (new Builder())
            .nutrition(2).saturationModifier(0).alwaysEdible()
            .effect(() -> new MobEffectInstance(BLINDNESS, 300), 0.33F)
            .effect(() -> new MobEffectInstance(POISON, 100), 0.33F)
            .build();

    // 迷之炒菜
    Food SUSPICIOUS_STIR_FRY_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.4F).alwaysEdible()
            .effect(() -> new MobEffectInstance(SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(JUMP_BOOST, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(HASTE, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(LUCK, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(MINING_FATIGUE, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(NAUSEA, 400), 0.15F)
            .build();

    Food SUSPICIOUS_STIR_FRY_ITEM = (new Builder())
            .nutrition(4).saturationModifier(0.4F).alwaysEdible()
            .effect(() -> new MobEffectInstance(SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(JUMP_BOOST, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(HASTE, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(LUCK, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(MINING_FATIGUE, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(NAUSEA, 400), 0.15F)
            .build();

    // 粘液饭
    Food SLIME_BALL_MEAL_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0)
            .effect(() -> new MobEffectInstance(FLATULENCE, 35 * 20), 1)
            .alwaysEdible().build();

    Food SLIME_BALL_MEAL_ITEM = (new Builder())
            .nutrition(9).saturationModifier(0)
            .effect(() -> new MobEffectInstance(FLATULENCE, 35 * 20), 1)
            .alwaysEdible().build();

    // 翻糖派
    Food FONDANT_PIE_BLOCK = (new Builder())
            .nutrition(5).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food FONDANT_PIE_ITEM = (new Builder())
            .nutrition(20).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 东坡肉
    Food DONGPO_PORK_BLOCK = (new Builder())
            .nutrition(7).saturationModifier(0.521f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food DONGPO_PORK_ITEM = (new Builder())
            .nutrition(20).saturationModifier(0.55f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 翻糖蛛眼
    Food FONDANT_SPIDER_EYE_BLOCK = (new Builder())
            .nutrition(2).saturationModifier(0)
            .effect(() -> new MobEffectInstance(FLATULENCE, 35 * 20), 1.0F)
            .alwaysEdible().build();

    Food FONDANT_SPIDER_EYE_ITEM = (new Builder())
            .nutrition(8).saturationModifier(0)
            .effect(() -> new MobEffectInstance(FLATULENCE, 35 * 20), 1.0F)
            .alwaysEdible().build();

    // 荷包紫颂烧
    Food CHORUS_FRIED_EGG_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0)
            .effect(() -> new MobEffectInstance(FLATULENCE, 35 * 20), 1.0F)
            .alwaysEdible().build();

    Food CHORUS_FRIED_EGG_ITEM = (new Builder())
            .nutrition(9).saturationModifier(0)
            .effect(() -> new MobEffectInstance(FLATULENCE, 35 * 20), 1.0F)
            .alwaysEdible().build();

    // 红烧鱼
    Food BRAISED_FISH_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WATER_BREATHING, 3600), 1.0F)
            .alwaysEdible().build();

    Food BRAISED_FISH_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(WATER_BREATHING, 3600), 1.0F)
            .alwaysEdible().build();

    // 黄金沙拉
    Food GOLDEN_SALAD_BLOCK = (new Builder())
            .nutrition(5).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(RESISTANCE, 2000), 1.0F)
            .effect(() -> new MobEffectInstance(REGENERATION, 200), 1.0F)
            .alwaysEdible().build();

    Food GOLDEN_SALAD_ITEM = (new Builder())
            .nutrition(20).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(RESISTANCE, 2000), 1.0F)
            .effect(() -> new MobEffectInstance(REGENERATION, 200), 1.0F)
            .alwaysEdible().build();

    // 辣子鸡
    Food SPICY_CHICKEN_BLOCK = (new Builder())
            .nutrition(5).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food SPICY_CHICKEN_ITEM = (new Builder())
            .nutrition(20).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 烧鸟串
    Food YAKITORI_BLOCK = (new Builder())
            .nutrition(5).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food YAKITORI_ITEM = (new Builder())
            .nutrition(20).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 水晶羊排
    Food CRYSTAL_LAMB_CHOP_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.625F)
            .effect(() -> new MobEffectInstance(HASTE, 5 * 60 * 20, 1), 1.0F)
            .alwaysEdible().build();

    Food CRYSTAL_LAMB_CHOP_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(HASTE, 5 * 60 * 20, 1), 1.0F)
            .alwaysEdible().build();

    // 下界风味刺身
    Food NETHER_STYLE_SASHIMI_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food NETHER_STYLE_SASHIMI_ITEM = (new Builder())
            .nutrition(16).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 香煎骑士牛排
    Food PAN_SEARED_KNIGHT_STEAK_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.625F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 1800), 1.0F)
            .effect(() -> new MobEffectInstance(WARMTH, 800), 1.0F)
            .alwaysEdible().build();

    Food PAN_SEARED_KNIGHT_STEAK_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 1800), 1.0F)
            .alwaysEdible().build();

    // 仰望星空派
    Food STARGAZY_PIE_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(FLATULENCE, 600), 1.0F)
            .effect(() -> new MobEffectInstance(UNLUCK, 3600), 1.0F)
            .alwaysEdible().build();

    Food STARGAZY_PIE_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(FLATULENCE, 600), 1.0F)
            .effect(() -> new MobEffectInstance(UNLUCK, 3600), 1.0F)
            .alwaysEdible().build();

    // 珍珠咕噜肉
    Food SWEET_AND_SOUR_ENDER_PEARLS_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0)
            .effect(() -> new MobEffectInstance(FLATULENCE, 35 * 20), 1.0F)
            .alwaysEdible().build();

    Food SWEET_AND_SOUR_ENDER_PEARLS_ITEM = (new Builder())
            .nutrition(9).saturationModifier(0)
            .effect(() -> new MobEffectInstance(FLATULENCE, 35 * 20), 1.0F)
            .alwaysEdible().build();

    // 烈焰羊排
    Food BLAZE_LAMB_CHOP_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.625f)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 1600), 1.0F)
            .alwaysEdible().build();

    Food BLAZE_LAMB_CHOP_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 1600), 1.0F)
            .alwaysEdible().build();

    // 凛冬羊排
    Food FROST_LAMB_CHOP_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.625f)
            .effect(() -> new MobEffectInstance(TUNDRA_STRIDER, 1600), 1.0F)
            .alwaysEdible().build();

    Food FROST_LAMB_CHOP_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(TUNDRA_STRIDER, 1600), 1.0F)
            .alwaysEdible().build();

    // 末地风味刺身
    Food END_STYLE_SASHIMI_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food END_STYLE_SASHIMI_ITEM = (new Builder())
            .nutrition(16).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 沙漠风味刺身
    Food DESERT_STYLE_SASHIMI_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food DESERT_STYLE_SASHIMI_ITEM = (new Builder())
            .nutrition(16).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 苔原风味刺身
    Food TUNDRA_STYLE_SASHIMI_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food TUNDRA_STYLE_SASHIMI_ITEM = (new Builder())
            .nutrition(16).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 寒带风味刺身
    Food COLD_STYLE_SASHIMI_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food COLD_STYLE_SASHIMI_ITEM = (new Builder())
            .nutrition(16).saturationModifier(0.1875F)
            .effect(() -> new MobEffectInstance(MUSTARD, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 水煎包
    Food SHENGJIAN_MANTOU_ITEM = (new Builder())
            .nutrition(8).saturationModifier(1)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food SHENGJIAN_MANTOU_BLOCK = (new Builder())
            .nutrition(2).saturationModifier(1)
            .effect(() -> new MobEffectInstance(WARMTH, 6000), 1.0F)
            .alwaysEdible().build();

    // 番茄炒蛋
    Food SCRAMBLE_EGG_WITH_TOMATOES = (new Builder())
            .nutrition(9).saturationModifier(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR, 90 * 20), 1.0F)
            .alwaysEdible().build();

    // 爆炒牛杂
    Food STIR_FRIED_BEEF_OFFAL = (new Builder())
            .nutrition(9).saturationModifier(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR, 90 * 20), 1.0F)
            .alwaysEdible().build();

    // 红烧牛肉
    Food BRAISED_BEEF = (new Builder())
            .nutrition(9).saturationModifier(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR, 90 * 20), 1.0F)
            .alwaysEdible().build();

    // 青椒炒肉
    Food STIR_FRIED_PORK_WITH_PEPPERS = (new Builder())
            .nutrition(9).saturationModifier(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR, 90 * 20), 1.0F)
            .alwaysEdible().build();

    // 糖醋里脊
    Food SWEET_AND_SOUR_PORK = (new Builder())
            .nutrition(9).saturationModifier(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR, 90 * 20), 1.0F)
            .alwaysEdible().build();

    // 鱼香肉丝
    Food FISH_FLAVORED_SHREDDED_PORK = (new Builder())
            .nutrition(9).saturationModifier(0.611f)
            .effect(() -> new MobEffectInstance(VIGOR, 90 * 20), 1.0F)
            .alwaysEdible().build();

    // 田园杂蔬
    Food COUNTRY_STYLE_MIXED_VEGETABLES = (new Builder())
            .nutrition(9).saturationModifier(0.611f)
            .effect(() -> new MobEffectInstance(PRESERVATION, 90 * 20), 1.0F)
            .alwaysEdible().build();

    // 米饭
    Food COOKED_RICE = (new Builder())
            .nutrition(6).saturationModifier(1)
            .alwaysEdible().build();

    // 蛋炒饭
    Food EGG_FRIED_RICE = (new Builder())
            .nutrition(8).saturationModifier(0.875F)
            .alwaysEdible().build();

    // 美味蛋炒饭
    Food DELICIOUS_EGG_FRIED_RICE = (new Builder())
            .nutrition(12).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 谜之炒菜盖饭
    Food SUSPICIOUS_STIR_FRY_RICE_BOWL = (new Builder())
            .nutrition(8).saturationModifier(0.4F)
            .effect(() -> new MobEffectInstance(SPEED, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(JUMP_BOOST, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(HASTE, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(LUCK, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(MINING_FATIGUE, 1200), 0.15F)
            .effect(() -> new MobEffectInstance(NAUSEA, 400), 0.15F)
            .alwaysEdible().build();

    // 番茄炒蛋盖饭
    Food SCRAMBLE_EGG_WITH_TOMATOES_RICE_BOWL = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 爆炒牛杂盖饭
    Food STIR_FRIED_BEEF_OFFAL_RICE_BOWL = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 红烧牛肉盖饭
    Food BRAISED_BEEF_RICE_BOWL = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 青椒炒肉盖饭
    Food STIR_FRIED_PORK_WITH_PEPPERS_RICE_BOWL = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 糖醋里脊盖饭
    Food SWEET_AND_SOUR_PORK_RICE_BOWL = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 鱼香肉丝盖饭
    Food FISH_FLAVORED_SHREDDED_PORK_RICE_BOWL = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 红烧鱼盖饭
    Food BRAISED_FISH_RICE_BOWL = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 辣子鸡盖饭
    Food SPICY_CHICKEN_RICE_BOWL = (new Builder())
            .nutrition(14).saturationModifier(0.643f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 大骨汤
    Food PORK_BONE_SOUP = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(VIGOR, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 海鲜味噌汤
    Food SEAFOOD_MISO_SOUP = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(WATER_BREATHING, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 恐惧浓汤
    Food FEARSOME_THICK_SOUP = (new Builder())
            .nutrition(4).saturationModifier(0)
            .effect(() -> new MobEffectInstance(SULFUR, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 萝卜羊肉汤
    Food LAMB_AND_RADISH_SOUP = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(TUNDRA_STRIDER, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 土豆炖牛肉
    Food BRAISED_BEEF_WITH_POTATOES = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 野菌兔肉汤
    Food WILD_MUSHROOM_RABBIT_SOUP = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(SPEED, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 番茄牛腩汤
    Food TOMATO_BEEF_BRISKET_SOUP = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 河豚汤
    Food PUFFERFISH_SOUP = (new Builder())
            .nutrition(4).saturationModifier(0)
            .effect(() -> new MobEffectInstance(MUSTARD, 10 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 罗宋汤
    Food BORSCHT = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(FLATULENCE, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 牛丸汤
    Food BEEF_MEATBALL_SOUP = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 小鸡炖蘑菇
    Food CHICKEN_AND_MUSHROOM_STEW = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 驴肉汤
    Food DONKEY_SOUP = (new Builder())
            .nutrition(6).saturationModifier(0.667f)
            .effect(() -> new MobEffectInstance(WARMTH, 8 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 拔丝土豆
    Food CANDIED_POTATO_BLOCK = (new Builder())
            .nutrition(5).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food CANDIED_POTATO_ITEM = (new Builder())
            .nutrition(20).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 疙瘩汤
    Food DOUGH_DROP_SOUP_BLOCK = (new Builder())
            .nutrition(2).saturationModifier(1)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food DOUGH_DROP_SOUP_ITEM = (new Builder())
            .nutrition(8).saturationModifier(1)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 虎皮青椒酿肉
    Food STUFFED_TIGER_SKIN_PEPPER_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food STUFFED_TIGER_SKIN_PEPPER_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 麻辣兔头
    Food SPICY_RABBIT_HEAD_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food SPICY_RABBIT_HEAD_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 四喜丸子汤
    Food FOUR_JOY_MEATBALL_SOUP_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food FOUR_JOY_MEATBALL_SOUP_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 椒麻鸡
    Food NUMBING_SPICY_CHICKEN_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food NUMBING_SPICY_CHICKEN_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 油炸猪儿虫
    Food FRIED_CATERPILLAR_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.8F)
            .effect(() -> new MobEffectInstance(FLATULENCE, 10 * 20), 1.0F)
            .alwaysEdible().build();

    Food FRIED_CATERPILLAR_ITEM = (new Builder())
            .nutrition(12).saturationModifier(0.8F)
            .effect(() -> new MobEffectInstance(FLATULENCE, 10 * 20), 1.0F)
            .alwaysEdible().build();

    // 炸春卷
    Food FRIED_SPRING_ROLL_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food FRIED_SPRING_ROLL_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 毛血旺
    Food SPICY_BLOOD_STEW_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food SPICY_BLOOD_STEW_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 棕色蘑菇瓦罐汤
    Food BROWN_MUSHROOM_POT_SOUP_BLOCK = (new Builder())
            .nutrition(6).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food BROWN_MUSHROOM_POT_SOUP_ITEM = (new Builder())
            .nutrition(18).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 红色蘑菇瓦罐汤
    Food RED_MUSHROOM_POT_SOUP_BLOCK = (new Builder())
            .nutrition(6).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food RED_MUSHROOM_POT_SOUP_ITEM = (new Builder())
            .nutrition(18).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 5 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 诡异菌瓦罐汤
    Food WARPED_FUNGUS_POT_SOUP_BLOCK = (new Builder())
            .nutrition(6).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food WARPED_FUNGUS_POT_SOUP_ITEM = (new Builder())
            .nutrition(18).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 绯红菌瓦罐汤
    Food CRIMSON_FUNGUS_POT_SOUP_BLOCK = (new Builder())
            .nutrition(6).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food CRIMSON_FUNGUS_POT_SOUP_ITEM = (new Builder())
            .nutrition(18).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(FIRE_RESISTANCE, 2 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 佛跳墙
    Food BUDDHA_JUMPS_OVER_THE_WALL_BLOCK = (new Builder())
            .nutrition(7).saturationModifier(0.521f)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    Food BUDDHA_JUMPS_OVER_THE_WALL_ITEM = (new Builder())
            .nutrition(20).saturationModifier(0.55F)
            .effect(() -> new MobEffectInstance(SATIATED_SHIELD, 3 * 60 * 20), 1.0F)
            .alwaysEdible().build();

    // 红烧排骨
    Food BRAISED_PORK_RIBS_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.8f)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food BRAISED_PORK_RIBS_ITEM = (new Builder())
            .nutrition(16).saturationModifier(0.8f)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 冷肉炙
    Food COLD_ROASTED_MEAT_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.8f)
            .alwaysEdible().build();

    Food COLD_ROASTED_MEAT_ITEM = (new Builder())
            .nutrition(16).saturationModifier(0.8f)
            .alwaysEdible().build();

    // 油泼鱼: oil_splashed_fish
    Food OIL_SPLASHED_FISH_BLOCK = (new Builder())
            .nutrition(3).saturationModifier(0.667F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    Food OIL_SPLASHED_FISH_ITEM = (new Builder())
            .nutrition(13).saturationModifier(0.615F)
            .effect(() -> new MobEffectInstance(WARMTH, 80 * 20), 1.0F)
            .alwaysEdible().build();

    // 冷切火腿片，只有方块形态才能进食
    Food COLD_CUT_HAM_SLICES_BLOCK = (new Builder())
            .nutrition(4).saturationModifier(0.8f)
            .alwaysEdible().build();
}
