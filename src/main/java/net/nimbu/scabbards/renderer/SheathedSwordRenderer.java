package net.nimbu.scabbards.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SheathedSwordRenderer {

    public static void renderItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int light,
            int overlay,
            BakedModel model
    ) {


        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack swordStack = new ItemStack(Items.IRON_SWORD);
        model = itemRenderer.getModel(swordStack, null,null,0);

        poseStack.pushPose();

        model.applyTransform(
                displayContext,
                poseStack,
                leftHanded); //left handed?

        //matrices.scale(scale, scale, scale);
        //poseStack.translate(-0.5F, -0.1F, -0.5F);

        poseStack.scale(1.0F, 1.0F, 0.9F);

        itemRenderer.render(swordStack, displayContext, leftHanded, poseStack, multiBufferSource, light, overlay, model);


        //renderBakedItemModel(model, stack, 255, overlay, matrices, vertexConsumer);
        poseStack.popPose();
    }

}
