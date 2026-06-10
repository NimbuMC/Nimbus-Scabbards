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
            BakedModel model
    ) {


        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);
        if (storedItem==null) {return;}
        ItemStack swordStack = storedItem.stack();
        BakedModel swordModel = itemRenderer.getModel(swordStack, null,null,0);

        poseStack.pushPose();
        swordModel = swordModel.applyTransform(displayContext, poseStack, leftHanded);
        poseStack.scale(-1.0F, -1.0F, 0.95F);
        poseStack.translate((float) -8.001 /16, (float) -8.001 /16, (float) -8 /16);
        VertexConsumer vc = ItemRenderer.getFoilBufferDirect(
                multiBufferSource,
                RenderType.entityCutout(swordModel.getParticleIcon().atlasLocation()),
                false,
                swordStack.hasFoil()
        );
        renderBakedItemModel(swordModel, light, overlay, poseStack, vc);
        poseStack.popPose();
    }


    private static void renderBakedItemModel(BakedModel model, int light, int overlay, PoseStack poseStack, VertexConsumer vertices) {
        RandomSource random = RandomSource.create();
        long l = 42L;

        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            renderBakedItemQuads(poseStack, vertices, model.getQuads(null, direction, random), light, overlay);
        }

        random.setSeed(42L);
        renderBakedItemQuads(poseStack, vertices, model.getQuads(null, null, random), light, overlay);
    }

    private static void renderBakedItemQuads(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            List<BakedQuad> quads,
            int light,
            int overlay
    ) {
        PoseStack.Pose pose = poseStack.last();
        for (BakedQuad quad : quads) {
            float r = 1.0f;
            float g = 1.0f;
            float b = 1.0f;
            float a = 1.0f;
            vertexConsumer.putBulkData(
                    pose,
                    quad,
                    r, g, b, a,
                    light,
                    overlay,
                    true
            );
        }
    }
}
