package net.nimbu.scabbards.renderer.entity.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ScabbardModel {


    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

//        root.addOrReplaceChild(
//                "scabbard",
//                CubeListBuilder.create()
//                        .texOffs(0, 0)
//                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 16.0F, 2.0F),
//                PartPose.ZERO
//        );

        return LayerDefinition.create(mesh, 32, 32);
    }
}
