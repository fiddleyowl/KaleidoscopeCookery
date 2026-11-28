package com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.ScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.layer.ScarecrowHandLayer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity.layer.ScarecrowParrotOnShoulderLayer;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.animal.parrot.ParrotModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.ParrotRenderState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.parrot.Parrot;
import org.jspecify.annotations.Nullable;

public class ScarecrowRender extends LivingEntityRenderer<ScarecrowEntity, ScarecrowRender.State, ScarecrowModel> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/entity/scarecrow.png");

    public ScarecrowRender(EntityRendererProvider.Context context) {
        super(context, new ScarecrowModel(context.bakeLayer(ScarecrowModel.LAYER_LOCATION)), 0);
        this.addLayer(new ScarecrowHandLayer(this));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
        this.addLayer(new ScarecrowParrotOnShoulderLayer(this, context.getModelSet()));
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(ScarecrowEntity scarecrow, State state, float partialTick) {
        super.extractRenderState(scarecrow, state, partialTick);
        ArmedEntityRenderState.extractArmedEntityRenderState(scarecrow, state, this.itemModelResolver, partialTick);
        state.showDefaultHead = scarecrow.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
        state.hitAnimationTime = (float) (scarecrow.level().getGameTime() - scarecrow.lastHit) + partialTick;

        state.shoulderParrot = null;
        CompoundTag tag = scarecrow.getShoulderEntity();
        if (tag.isEmpty()) {
            return;
        }
        EntityType.byString(tag.getStringOr("id", "")).filter(type -> type == EntityType.PARROT).ifPresent(type -> {
            ParrotRenderState parrot = new ParrotRenderState();
            parrot.variant = Parrot.Variant.byId(tag.getIntOr("Variant", 0));
            parrot.pose = ParrotModel.Pose.ON_SHOULDER;
            parrot.ageInTicks = scarecrow.tickCount;
            parrot.walkAnimationPos = state.walkAnimationPos;
            parrot.walkAnimationSpeed = state.walkAnimationSpeed;
            parrot.yRot = state.yRot;
            parrot.xRot = state.xRot;
            state.shoulderParrot = parrot;
        });
    }

    @Override
    protected void setupRotations(State state, PoseStack poseStack, float yBodyRot, float scale) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yBodyRot));
        float time = state.hitAnimationTime;
        if (time < 5.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(time / 1.5F * Mth.PI) * 3.0F));
        }
    }

    @Override
    protected boolean shouldShowName(ScarecrowEntity scarecrow, double distance) {
        return distance < 4096 && scarecrow.isCustomNameVisible();
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return TEXTURE;
    }

    public static final class State extends ArmedEntityRenderState {
        public boolean showDefaultHead = true;
        public @Nullable ParrotRenderState shoulderParrot;
        private float hitAnimationTime;
    }
}
