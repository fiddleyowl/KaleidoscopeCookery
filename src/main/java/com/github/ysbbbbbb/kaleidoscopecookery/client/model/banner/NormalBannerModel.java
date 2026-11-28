package com.github.ysbbbbbb.kaleidoscopecookery.client.model.banner;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class NormalBannerModel extends Model<Float> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "normal_banner"), "main");

    private final ModelPart bone;
    private final ModelPart front;
    private final ModelPart back;

    public NormalBannerModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
        this.bone = root.getChild("bone");
        this.front = this.bone.getChild("front");
        this.back = this.bone.getChild("back");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(64, 0).addBox(-1.0F, -60.0F, -1.0F, 2.0F, 60.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 64).addBox(-2.0F, -64.0F, -14.0F, 4.0F, 4.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition front = bone.addOrReplaceChild("front", CubeListBuilder.create().texOffs(0, -28).addBox(0.0F, 0.0F, -14.0F, 0.0F, 41.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -60.0F, 0.0F));

        PartDefinition back = bone.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, -28).addBox(0.0F, 0.0F, -14.0F, 0.0F, 41.0F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -60.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Float angle) {
        this.front.zRot = angle;
        this.back.zRot = angle;
    }
}
