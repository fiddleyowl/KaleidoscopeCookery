package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.StockpotBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.event.ModModelEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotVisuals;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class StockpotBlockEntityRender implements BlockEntityRenderer<StockpotBlockEntity, StockpotBlockEntityRender.State> {
    private final ItemModelResolver itemModelResolver;
    private final SpriteGetter sprites;
    private final Function<Identifier, ISoupBaseRender> soupBaseRender;

    public StockpotBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
        this.sprites = context.sprites();
        this.soupBaseRender = Util.memoize(id -> SoupBaseManager.getSoupBase(id).getRender());
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(StockpotBlockEntity stockpot, State state, float partialTick,
                                   Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(stockpot, state, partialTick, cameraPosition, breakProgress);
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        if (stockpot.getBlockState().getValue(StockpotBlock.HAS_LID)) {
            return;
        }

        int status = stockpot.getStatus();
        if (status != StockpotBlockEntity.PUT_INGREDIENT && status != StockpotBlockEntity.COOKING && status != StockpotBlockEntity.FINISHED) {
            return;
        }
        ISoupBaseRender soupBase = this.soupBaseRender.apply(stockpot.getSoupBaseId());
        ModModelEvent.copyBreakProgress(state);
        state.status = status;
        state.soupBase = soupBase;

        if (status == StockpotBlockEntity.PUT_INGREDIENT) {
            state.soupState = soupBase.extractRenderState(stockpot, partialTick, this.sprites, state, null, 0.38f);
            this.extractItems(stockpot, state, ItemRenderReplacerReloadListener.INSTANCE.stockpotCooking());
        } else if (status == StockpotBlockEntity.COOKING) {
            StockpotVisuals visuals = Objects.requireNonNullElse(stockpot.visuals, StockpotVisuals.DEFAULT);
            state.soupState = soupBase.extractRenderState(
                    stockpot, partialTick, this.sprites, state,
                    visuals.cookingTexture(), 0.38f
            );
            this.extractItems(stockpot, state, ItemRenderReplacerReloadListener.INSTANCE.stockpotFinished());
        } else if (status == StockpotBlockEntity.FINISHED) {
            StockpotVisuals visuals = Objects.requireNonNullElse(stockpot.visuals, StockpotVisuals.DEFAULT);
            float soupHeight = 0.065f;

            // 如果是糖浆
            int syrupUses = stockpot.getSyrupUses();
            if (syrupUses > 0) {
                soupHeight += 0.065f + 0.3f / 9 * syrupUses;
            } else {
                int takeoutCount = stockpot.getTakeoutCount();
                int maxCount = Math.min(stockpot.getResult().getCount(), StockpotBlockEntity.MAX_TAKEOUT_COUNT);
                soupHeight += 0.315f / maxCount * takeoutCount;
            }

            state.soupState = soupBase.extractRenderState(
                    stockpot, partialTick, this.sprites, state,
                    visuals.finishedTexture(), soupHeight
            );
        }
    }

    private void extractItems(StockpotBlockEntity stockpot, State state, Map<Identifier, Identifier> models) {
        NonNullList<ItemStack> items = stockpot.getInputs();
        for (ItemStack stack : items) {
            if (stack.isEmpty()) {
                continue;
            }
            ItemStackRenderState item = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(item, ItemRenderReplacer.display(stack, models),
                    ItemDisplayContext.FIXED, stockpot.getLevel(), null, 0);
            state.items.add(new ItemDisplay(item, stack.hashCode()));
        }
    }

    @Override
    public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        ISoupBaseRender soupBase = state.soupBase;
        ISoupBaseRender.SoupBaseRenderState soupState = state.soupState;
        if (soupBase == null || soupState == null) {
            return;
        }

        int status = state.status;
        if (status == StockpotBlockEntity.PUT_INGREDIENT) {
            soupBase.renderWhenPutIngredient(soupState, poseStack, collector, camera);
            this.renderItems(state, poseStack, collector);
        } else if (status == StockpotBlockEntity.COOKING) {
            soupBase.renderWhenCooking(soupState, poseStack, collector, camera);
            this.renderItems(state, poseStack, collector);
        } else if (status == StockpotBlockEntity.FINISHED) {
            soupBase.renderWhenFinished(soupState, poseStack, collector, camera);
        }
    }

    private void renderItems(State state, PoseStack poseStack, SubmitNodeCollector collector) {
        for (ItemDisplay item : state.items) {
            int animationSeed = item.animationSeed();
            long time = animationSeed + System.currentTimeMillis();
            float offsetX = (animationSeed % 100) * 0.002f;
            float offsetZ = (float) (Math.sin(time * 0.0005) * 0.2);
            float offsetY = animationSeed % 50 * 0.004f;
            float yRot = (animationSeed % 2 == 0 ? -1 : 1) * 20 + animationSeed % 10;

            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(85 + animationSeed % 10));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(0.9 + offsetX, 0.9 + offsetY, -0.5 + offsetZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(animationSeed % 360));
            ModModelEvent.submitItem(item.model(), state, poseStack, collector);
            poseStack.popPose();
        }
    }

    private record ItemDisplay(ItemStackRenderState model, int animationSeed) {
    }

    public static final class State extends BlockEntityRenderState {
        private final List<ItemDisplay> items = new ArrayList<>();
        private @Nullable ISoupBaseRender soupBase;
        private ISoupBaseRender.@Nullable SoupBaseRenderState soupState;
        private int status;
    }
}
