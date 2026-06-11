package net.nimbu.scabbards.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.nimbu.scabbards.item.ModItems;
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
        if (stack.is(ModItems.SCABBARD.get())) {
//            switch (displayContext){
//                case FIRST_PERSON_LEFT_HAND, FIRST_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND ->{ //render the 3d model instead in hand
//                    //render the model.... if i can be bothered
//                    ci.cancel(); //cancel regular rendering
//                }
//            }
            SheathedSwordRenderer.renderItem(stack, displayContext, leftHanded, poseStack, buffer, light, overlay);
        }
    }
}