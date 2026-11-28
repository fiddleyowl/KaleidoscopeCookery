package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;


import com.github.ysbbbbbb.kaleidoscopecookery.item.quality.QualityUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra.TetraCompat;
import squeek.appleskin.api.event.FoodValuesEvent;
import static com.github.ysbbbbbb.kaleidoscopecookery.compat.tetra.TetraCompat.TETRA_ID;


@EventBusSubscriber
public class CompatRegistry {
    public static boolean SHOW_POTION_EFFECT_TOOLTIPS = true;

    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> {
            // 当安装 Food Effect Tooltips (Forge) 模组时，关闭药水效果提示
            SHOW_POTION_EFFECT_TOOLTIPS = !ModList.get().isLoaded("foodeffecttooltips");
            if (FMLEnvironment.getDist() == Dist.CLIENT && ModList.get().isLoaded("appleskin")) {
                AppleSkin.init();
            }
        });
        event.enqueueWork(() -> checkModLoad(TETRA_ID, TetraCompat::init));
    }

    private static void checkModLoad(String modId, Runnable runnable) {
        if (ModList.get().isLoaded(modId)) {
            runnable.run();
        }
    }

    private static final class AppleSkin {
        private static void init() {
            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, AppleSkin::onFoodValues);
        }

        private static void onFoodValues(FoodValuesEvent event) {
            FoodProperties raw = event.itemStack.get(DataComponents.FOOD);
            FoodProperties adjusted = QualityUtils.getFoodProperties(event.itemStack);
            if (adjusted == null || adjusted.equals(raw)) {
                return;
            }

            if (event.defaultFoodProperties == raw) {
                event.defaultFoodProperties = adjusted;
            }
            if (event.modifiedFoodProperties == raw) {
                event.modifiedFoodProperties = adjusted;
            }
        }
    }
}
