package net.nimbu.scabbards.renderer.entity.layers;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.nimbu.scabbards.Scabbards;

public class ModModelLayers {

    public static final ModelLayerLocation SCABBARD_LAYER =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(Scabbards.MOD_ID, "scabbard"),
                    "main"
            );
}
