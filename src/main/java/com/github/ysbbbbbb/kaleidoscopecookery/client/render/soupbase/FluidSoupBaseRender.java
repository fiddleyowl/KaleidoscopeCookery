package com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import org.jspecify.annotations.Nullable;

public class FluidSoupBaseRender implements ISoupBaseRender {
    private final Fluid fluid;

    public FluidSoupBaseRender(Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public SoupBaseRenderState extractRenderState(StockpotBlockEntity stockpot, float partialTick, SpriteGetter sprites,
                                    BlockEntityRenderState blockEntityState, @Nullable Identifier texture, float soupHeight) {
        if (texture != null) {
            return new SoupBaseRenderState(sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, texture)),
                    0xFFFFFFFF, soupHeight, blockEntityState);
        }

        FluidState fluidState = this.fluid.defaultFluidState();
        FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidState);
        FluidTintSource tintSource = model.fluidTintSource();
        int color = tintSource == null ? 0xFFFFFFFF : tintSource.color(fluidState);
        return new SoupBaseRenderState(model.stillMaterial().sprite(), color, soupHeight, blockEntityState);
    }
}
