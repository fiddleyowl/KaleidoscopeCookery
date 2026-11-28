package com.github.ysbbbbbb.kaleidoscopecookery.util;

import com.github.ysbbbbbb.kaleidoscopecookery.api.item.IHasContainer;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Supplier;

public class ItemUtils {
    public static void getItemToLivingEntity(LivingEntity entity, ItemStack stack) {
        getItemToLivingEntity(entity, stack, -1);
    }

    public static void getItemToLivingEntity(LivingEntity entity, ItemStack stack, int preferredSlot) {
        if (stack.isEmpty()) {
            return;
        }
        if (entity.getMainHandItem().isEmpty()) {
            RandomSource random = entity.level().getRandom();
            entity.setItemInHand(InteractionHand.MAIN_HAND, stack);
            entity.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        } else if (entity instanceof Player player) {
            ItemHandlerHelper.giveItemToPlayer(player, stack, preferredSlot);
        } else {
            // 否则直接在实体所处位置生成物品
            ItemEntity dropItem = entity.spawnAtLocation(stack);
            if (dropItem != null) {
                dropItem.setPickUpDelay(0);
            }
        }
    }

    public static Pair<Integer, ItemStack> getLastStack(IItemHandler itemHandler) {
        for (int i = itemHandler.getSlots(); i > 0; i--) {
            int index = i - 1;
            ItemStack stack = itemHandler.getStackInSlot(index);
            if (!stack.isEmpty()) {
                return Pair.of(index, stack);
            }
        }
        return Pair.of(0, ItemStack.EMPTY);
    }

    public static boolean isConsumableFood(ItemStack stack) {
        return stack.has(DataComponents.FOOD) && stack.has(DataComponents.CONSUMABLE);
    }

    // 举起物品后，每隔 4 tick 播放一次使用效果。
    public static boolean shouldPlayUseEffects(ItemStack stack, LivingEntity entity, int ticksRemaining) {
        if (stack.has(DataComponents.CONSUMABLE)) {
            return false;
        }
        int duration = stack.getUseDuration(entity);
        int ticksUsed = duration - ticksRemaining;
        return ticksUsed > (int) (duration * 0.21875F) && ticksRemaining % 4 == 0;
    }

    public static void playUseEffects(Level level, LivingEntity entity, ItemStack stack,
                                      Supplier<SoundEvent> drinkingSound, int particles) {
        if (stack.isEmpty() || !entity.isUsingItem() || entity.getUseItem() != stack) {
            return;
        }
        ItemUseAnimation animation = stack.getUseAnimation();
        if (animation == ItemUseAnimation.DRINK) {
            entity.playSound(drinkingSound.get(), 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        } else if (animation == ItemUseAnimation.EAT) {
            entity.spawnItemParticles(stack, particles);
            RandomSource random = entity.getRandom();
            entity.playSound(SoundEvents.GENERIC_EAT.value(), 0.5F + 0.5F * random.nextInt(2),
                    (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    public static Item getContainerItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return Items.AIR;
        }
        if (isConsumableFood(stack)) {
            // 食物容器统一从UseRemainder读取
            UseRemainder remainder = stack.get(DataComponents.USE_REMAINDER);
            if (remainder == null) {
                return Items.AIR;
            }
            return remainder.convertInto().item().value();
        }
        Item item = stack.getItem();
        ItemStackTemplate remainingItem = stack.getCraftingRemainder();
        if (remainingItem != null) {
            return remainingItem.item().value();
        }
        if (item instanceof IHasContainer hasContainer) {
            return hasContainer.getContainerItem();
        } else if (stack.is(TagMod.BOWL_CONTAINER)) {
            return Items.BOWL;
        } else if (stack.is(TagMod.GLASS_BOTTLE_CONTAINER)) {
            return Items.GLASS_BOTTLE;
        } else if (stack.is(TagMod.BUCKET_CONTAINER)) {
            return Items.BUCKET;
        } else if (stack.is(Items.POTION)) {
            return Items.GLASS_BOTTLE;
        }
        return Items.AIR;
    }
}
