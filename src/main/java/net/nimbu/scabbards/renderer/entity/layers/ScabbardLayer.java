package net.nimbu.scabbards.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nimbu.scabbards.Scabbards;
import net.nimbu.scabbards.item.ModItems;
import net.nimbu.scabbards.renderer.SheathedSwordRenderer;
import net.nimbu.scabbards.renderer.entity.model.ScabbardModel;

public class ScabbardLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final ScabbardModel model;

    public ScabbardLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);

        this.model = new ScabbardModel(
                Minecraft.getInstance()
                        .getEntityModels()
                        .bakeLayer(
                                new ModelLayerLocation(
                                        ResourceLocation.fromNamespaceAndPath(Scabbards.MOD_ID, "scabbard"),
                                        "main"
                                )
                        )
        );
    }

    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource multiBufferSource,
                       int packedLight, //i think this is right lol
                       AbstractClientPlayer abstractClientPlayer,
                       float v, float v1, float v2, float v3, float v4, float v5) {


        ItemStack equipStack =
                abstractClientPlayer.getItemBySlot(EquipmentSlot.LEGS);


        if (!equipStack.is(ModItems.SCABBARD.get())) {
            return;
        }



        poseStack.pushPose();

        //position relative to player body
        getParentModel().body.translateAndRotate(poseStack);

        float armourDepth = 0;
        if(!abstractClientPlayer.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && !abstractClientPlayer.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) //if a chestpiece is worn
        {
            armourDepth = 0.05f;
        }

        poseStack.translate(-0.12, 0.18, 0.15+armourDepth);
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        SheathedSwordRenderer.renderItem(
                equipStack,
                ItemDisplayContext.NONE,
                false,
                poseStack,
                multiBufferSource,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        poseStack.pushPose();

        getParentModel().body.translateAndRotate(poseStack);

        poseStack.translate(-0.15, 0.0, 0.03+armourDepth);
        poseStack.scale(1.1F, 1.1F, 1.1F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(45));

        VertexConsumer vc = multiBufferSource.getBuffer(RenderType.entityCutout(
                ResourceLocation.fromNamespaceAndPath(Scabbards.MOD_ID, "textures/entity/scabbard_layer.png")
        ));

        model.render(poseStack, vc, packedLight);

        poseStack.popPose();
    }
}
