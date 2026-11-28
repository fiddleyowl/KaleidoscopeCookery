package com.github.ysbbbbbb.kaleidoscopecookery.client.model;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.TrashCanAnimation;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.TrashCanBlockEntityRender;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class TrashCanModel extends Model<TrashCanBlockEntityRender.State> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "trash_can"), "main");
    private final KeyframeAnimation put;
    private final KeyframeAnimation withdraw;
    private final KeyframeAnimation player1;
    private final KeyframeAnimation player2;
    private final KeyframeAnimation enter;

    public TrashCanModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        ModelPart animationRoot = root.getChild("root");
        this.put = TrashCanAnimation.PUT.bake(animationRoot);
        this.withdraw = TrashCanAnimation.WITHDRAW.bake(animationRoot);
        this.player1 = TrashCanAnimation.PLAYER1.bake(animationRoot);
        this.player2 = TrashCanAnimation.PLAYER2.bake(animationRoot);
        this.enter = TrashCanAnimation.ENTER.bake(animationRoot);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(16, 40).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition bone2 = root.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(8, 0).addBox(-7.0F, -0.25F, -7.0F, 14.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.75F, 0.0F));

        PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(6, 26).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(-1.0F, -0.5F, -3.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(6, 26).addBox(-1.0F, -0.5F, 3.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition eye = root.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(24, 22).addBox(-5.0F, -1.5F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 0.0F));

        PartDefinition bone3 = eye.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(34, 36).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -0.1F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(TrashCanBlockEntityRender.State state) {
        super.setupAnim(state);
        this.put.apply(state.putState, state.ageInTicks);
        this.withdraw.apply(state.withdrawState, state.ageInTicks);
        this.player1.apply(state.player1State, state.ageInTicks);
        this.player2.apply(state.player2State, state.ageInTicks);
        this.enter.apply(state.enterState, state.ageInTicks);
    }
}
