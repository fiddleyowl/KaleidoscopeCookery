package com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public class SimpleSoupBaseRender implements ISoupBaseRender {
    private final Identifier soupBaseTexture;

    public SimpleSoupBaseRender(Identifier soupBaseTexture) {
        this.soupBaseTexture = soupBaseTexture;
    }

    @Override
    public SoupBaseRenderState extractRenderState(StockpotBlockEntity stockpot, float partialTick, SpriteGetter sprites,
                                    BlockEntityRenderState blockEntityState, @Nullable Identifier texture, float soupHeight) {
        Identifier location = Objects.requireNonNullElse(texture, this.soupBaseTexture);
        TextureAtlasSprite sprite = sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, location));
        return new SoupBaseRenderState(sprite, 0xFFFFFFFF, soupHeight, blockEntityState);
    }
}
