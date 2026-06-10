package net.nimbu.scabbards.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.nimbu.scabbards.item.ModItems;
import net.nimbu.scabbards.renderer.SheathedSwordRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void renderMixin(
            ItemStack stack,
            ItemDisplayContext displayContext, //if gui etc
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay,
            BakedModel model,
            CallbackInfo ci
    ) {
        //If rendered item is a scabbard, render sword inside it
        if(stack.is(ModItems.SCABBARD)) {
            SheathedSwordRenderer.renderItem(stack, displayContext, leftHanded, poseStack, buffer, light, overlay);
        }
    }
}