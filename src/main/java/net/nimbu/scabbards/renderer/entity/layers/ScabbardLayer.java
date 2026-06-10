package net.nimbu.scabbards.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.nimbu.scabbards.item.ModItems;

public class ScabbardLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public ScabbardLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        super(renderer);
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
        if(!abstractClientPlayer.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) //if a chestpiece is worn
        {
            armourDepth = 0.05f;
        }

        poseStack.translate(-0.1, 0.20, 0.15+armourDepth);
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        ItemRenderer itemRenderer =
                Minecraft.getInstance().getItemRenderer();

        itemRenderer.renderStatic(
                equipStack,
                ItemDisplayContext.NONE,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                multiBufferSource,
                abstractClientPlayer.level(),
                abstractClientPlayer.getId()
        );

        poseStack.popPose();
    }
}
