package com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import org.jspecify.annotations.Nullable;

public class MobSoupBaseRender extends FluidSoupBaseRender {
    private final EntityType<?> mobType;

    public MobSoupBaseRender(Fluid fluid, EntityType<?> mobType) {
        super(fluid);
        this.mobType = mobType;
    }

    @Override
    public SoupBaseRenderState extractRenderState(StockpotBlockEntity stockpot, float partialTick, SpriteGetter sprites,
                                    BlockEntityRenderState blockEntityState, @Nullable Identifier texture, float soupHeight) {
        SoupBaseRenderState state = super.extractRenderState(stockpot, partialTick, sprites, blockEntityState, texture, soupHeight);
        if (stockpot.getStatus() == StockpotBlockEntity.FINISHED) {
            return state;
        }
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return state;
        }
        Entity renderEntity = stockpot.renderEntity;
        boolean shouldRefreshCache = renderEntity == null || renderEntity.getType() != mobType;
        if (shouldRefreshCache) {
            renderEntity = this.mobType.create(world, EntitySpawnReason.LOAD);
            stockpot.renderEntity = renderEntity;
            if (renderEntity != null) {
                renderEntity.setOnGround(true);
            }
        }
        if (renderEntity != null) {
            EntityRenderState mobRenderState = Minecraft.getInstance().getEntityRenderDispatcher().extractEntity(renderEntity, 0);
            mobRenderState.lightCoords = blockEntityState.lightCoords;
            state.mobRenderState = mobRenderState;
            state.mobAnimationSeed = renderEntity.hashCode();
        }
        return state;
    }

    @Override
    public void renderWhenPutIngredient(SoupBaseRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.renderWhenPutIngredient(state, poseStack, collector, camera);
        this.renderInputEntity(state, poseStack, collector, camera);
    }

    @Override
    public void renderWhenCooking(SoupBaseRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.renderWhenCooking(state, poseStack, collector, camera);
        this.renderInputEntity(state, poseStack, collector, camera);
    }

    private void renderInputEntity(SoupBaseRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        EntityRenderState mobRenderState = state.mobRenderState;
        if (mobRenderState == null) {
            return;
        }
        int animationSeed = state.mobAnimationSeed;
        float entityY = (float) (Math.sin(animationSeed + System.currentTimeMillis() * 0.0005) * 0.25);

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(animationSeed % 360));
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        Minecraft.getInstance().getEntityRenderDispatcher().submit(mobRenderState, camera, 1, 0.375f + entityY, 1, poseStack, collector);
        poseStack.popPose();
    }
}
