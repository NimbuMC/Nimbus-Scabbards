package net.nimbu.scabbards.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.nimbu.scabbards.component.ModDataComponents;
import net.nimbu.scabbards.component.StoredItem;

import java.util.List;

public class SheathedSwordRenderer {

    public static void renderItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int light,
            int overlay,
            boolean onBack
    ) {

        //The below code is an absolute hellscape caused my some modded weapons having 32x32 handheld sprites, but 16x16 GUI sprites. Its functional, but by no means perfect.

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);
        if (storedItem==null) {return;}

        poseStack.pushPose();

        ItemStack swordStack = storedItem.stack();
        BakedModel swordModel = itemRenderer.getModel(swordStack, null,null,0);
        if(!onBack){ swordModel.applyTransform(displayContext, poseStack, leftHanded); }//i dont know why this line needs to be here but it does

        poseStack.scale(-1.0F, -1.0F, 0.95F);
        poseStack.translate((float) -0.001 /16, (float) -0.001 /16, (float) 0.0);

        ItemDisplayContext spriteDisplayContext = ItemDisplayContext.NONE;
        switch (displayContext){
            case GUI -> {
                spriteDisplayContext = displayContext;
            }
        }

        if (onBack){spriteDisplayContext=ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;}

        itemRenderer.renderStatic(storedItem.stack(), spriteDisplayContext, light, overlay, poseStack, multiBufferSource, null, 0);

        poseStack.popPose();
    }
}


//
//ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);
//        if (storedItem==null) {return;}
//
//        poseStack.pushPose();
//        poseStack.scale(-1.0F, -1.0F, 0.95F);
//        poseStack.translate((float) -0.001 /16, (float) -0.001 /16, (float) -0.0);
//
//        itemRenderer.renderStatic(storedItem.stack(), displayContext, light, overlay, poseStack, multiBufferSource, null, 0);
//
//        poseStack.popPose();



//ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);
//        if (storedItem==null) {return;}

//
//        poseStack.pushPose();
//ItemStack swordStack = storedItem.stack();
//BakedModel swordModel = itemRenderer.getModel(swordStack, null,null,0);
//        swordModel.applyTransform(displayContext, poseStack, leftHanded); //i dont know why this line needs to be here but it does
//        poseStack.scale(-1.0F, -1.0F, 0.95F);
//        poseStack.translate((float) -0.001 /16, (float) -0.001 /16, (float) -0.0);
//
//        itemRenderer.renderStatic(storedItem.stack(), ItemDisplayContext.NONE, light, overlay, poseStack, multiBufferSource, null, 0);
//
//        poseStack.popPose();




//Transformation hell:
//ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
//StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);
//        if (storedItem==null) {return;}
//
//        poseStack.pushPose();
//
//
//        if (!onBack){
//        switch (displayContext){
//        case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> {
//displayContext=ItemDisplayContext.GUI;
//                    poseStack.scale(0.55F, 0.55F, 0.55F);
//                    poseStack.translate((float) -0 /16, (float) 0/16, (float) 2/16);
////                    poseStack.scale(0.85F, 0.85F, 0.85F);
////                    poseStack.mulPose(Axis.XP.rotationDegrees(-34));
////                    poseStack.mulPose(Axis.YP.rotationDegrees(-90));
//        }
//        case GROUND -> {
//        poseStack.translate((float) 0, (float) -4 /16, (float) 0.0);
//        }
//        }
//        }
//
//        poseStack.scale(-1.0F, -1.0F, 0.95F);
//        poseStack.translate((float) -0.001 /16, (float) -0.001 /16, (float) -0.0);
//
//        itemRenderer.renderStatic(storedItem.stack(), displayContext, light, overlay, poseStack, multiBufferSource, null, 0);
//
//        poseStack.popPose();