package net.nimbu.scabbards.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class WeaponHolsterModel extends AbstractScabbardModel{

    private final ModelPart root;

    public WeaponHolsterModel(ModelPart root){
        super(root);
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "weapon_holster",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.0F, 0.0F, -2.5F, 5.0F, 3.0F, 1.0F),
                PartPose.ZERO
        );

        return LayerDefinition.create(mesh, 16, 16);
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int light, int color) {
        root.getChild("weapon_holster").render(poseStack, buffer, light, OverlayTexture.NO_OVERLAY, 0xFF000000 | color);
    }
}
