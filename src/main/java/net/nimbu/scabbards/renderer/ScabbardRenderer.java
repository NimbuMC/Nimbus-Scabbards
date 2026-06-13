package net.nimbu.scabbards.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.nimbu.scabbards.Scabbards;
import net.nimbu.scabbards.component.ModDataComponents;
import net.nimbu.scabbards.component.StoredItem;
import net.nimbu.scabbards.item.ModItems;
import net.nimbu.scabbards.renderer.entity.model.ScabbardModel;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class ScabbardRenderer implements ICurioRenderer {

    private final ScabbardModel model;

    public ScabbardRenderer() {
        this.model = new ScabbardModel(
                Minecraft.getInstance()
                        .getEntityModels()
                        .bakeLayer(new ModelLayerLocation(
                                ResourceLocation.fromNamespaceAndPath(Scabbards.MOD_ID, "scabbard"),
                                "main"
                        ))
        );
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                          SlotContext slotContext,
                                                                          PoseStack poseStack,
                                                                          RenderLayerParent<T, M> renderLayerParent,
                                                                          MultiBufferSource renderTypeBuffer,
                                                                          int packedLight,
                                                                          float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks,
                                                                          float netHeadYaw,
                                                                          float headPitch) {


        LivingEntity entity = slotContext.entity();

        float armourDepth = 0;
        if(!entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && !entity.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) //if a chestpiece is worn
        {
            armourDepth = 0.05f;
        }

        poseStack.pushPose();

        //position relative to player body
        ((PlayerModel<?>) renderLayerParent.getModel()).body.translateAndRotate(poseStack);

        poseStack.translate(-0.35, 0.05, 0.15+armourDepth);
        poseStack.scale(0.9375F, 0.9375F, 0.9375F);

        poseStack.mulPose(Axis.ZP.rotationDegrees(135));
        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        poseStack.mulPose(Axis.XP.rotationDegrees(-11));

        SheathedSwordRenderer.renderItem(
                stack,
                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, //switch to using this version of rendering for more compatibility with other mods. a ball ache, but necessary
                false,
                poseStack,
                renderTypeBuffer,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();

        poseStack.pushPose();

        ((PlayerModel<?>) renderLayerParent.getModel()).body.translateAndRotate(poseStack);

        poseStack.translate(-0.15, 0.0, 0.03+armourDepth);
        poseStack.scale(1.1F, 1.1F, 1.1F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(45));

        VertexConsumer vc = renderTypeBuffer.getBuffer(RenderType.entityCutout(
                ResourceLocation.fromNamespaceAndPath(Scabbards.MOD_ID, "textures/entity/scabbard_layer_0.png")
        ));

        int color = stack.getOrDefault(
                DataComponents.DYED_COLOR,
                new DyedItemColor(0xd37d19, false)).rgb();
        model.render(poseStack, vc, packedLight, color);

        VertexConsumer overlayVc = renderTypeBuffer.getBuffer(
                RenderType.entityCutout(
                        ResourceLocation.fromNamespaceAndPath(
                                Scabbards.MOD_ID,
                                "textures/entity/scabbard_layer_1.png"
                        )
                )
        );

        model.render(poseStack, overlayVc, packedLight, 0xFFFFFFFF);

        poseStack.popPose();

    }
}
