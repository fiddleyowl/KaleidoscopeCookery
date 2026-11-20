package com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.createmod.ponder.api.client.registration.PonderPlugin;
import net.createmod.ponder.api.client.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.client.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.api.client.PonderIndex;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.Identifier;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KitchenPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return KaleidoscopeCookery.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<Identifier> helper) {
        KitchenBlockPonderScreen.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<Identifier> helper) {
        KitchenBlockPonderTag.register(helper);
    }

    public static void init() {
        PonderIndex.addPlugin(new KitchenPonderPlugin());
    }
}
