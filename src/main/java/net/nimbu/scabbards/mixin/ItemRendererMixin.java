package net.nimbu.scabbards.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.nimbu.scabbards.item.ModItems;
import net.nimbu.scabbards.item.custom.ScabbardItem;
import net.nimbu.scabbards.renderer.ScabbardRenderer;
import net.nimbu.scabbards.renderer.SheathedSwordRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    private void renderHeldScabbardMixin(
            ItemStack stack,
            ItemDisplayContext displayContext,
            boolean leftHanded,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay,
            BakedModel model,
            CallbackInfo ci
    ) {
        if (stack.is(ModItems.SCABBARD.get()) || stack.is(ModItems.WEAPON_HOlSTER.get()) || stack.is(ModItems.HIP_SCABBARD.get())) {
            SheathedSwordRenderer.renderItem(stack, displayContext, leftHanded, poseStack, buffer, light, overlay, false);
        }
    }
}