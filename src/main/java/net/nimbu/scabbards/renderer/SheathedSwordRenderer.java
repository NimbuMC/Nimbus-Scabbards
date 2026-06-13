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
            int overlay
    ) {


        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);
        if (storedItem==null) {return;}
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 0.95F);
        poseStack.translate((float) -0.001 /16, (float) -0.001 /16, (float) -0.0);

        itemRenderer.renderStatic(storedItem.stack(), displayContext, light, overlay, poseStack, multiBufferSource, null, 0);

        poseStack.popPose();
    }
}
