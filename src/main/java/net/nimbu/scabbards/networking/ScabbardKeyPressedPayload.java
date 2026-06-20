package net.nimbu.scabbards.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.nimbu.scabbards.Scabbards;
import net.nimbu.scabbards.item.ModItems;
import net.nimbu.scabbards.item.custom.ScabbardItem;
import top.theillusivec4.curios.api.CuriosApi;

public record ScabbardKeyPressedPayload() implements CustomPacketPayload {

    public static final Type<ScabbardKeyPressedPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Scabbards.MOD_ID, "scabbard_key"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ScabbardKeyPressedPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, pkt) -> {},
                    buf -> new ScabbardKeyPressedPayload()
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ScabbardKeyPressedPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();

        CuriosApi.getCuriosInventory(player).ifPresent(curios -> {


            Item[] items = {
                    ModItems.SCABBARD.get(),
                    ModItems.WEAPON_HOlSTER.get()
            };

            for (Item item : items) {
                curios.findFirstCurio(item).ifPresent(slot -> {
                    ItemStack stack = slot.stack();
                    if (!stack.isEmpty()) {
                        if (stack.getItem() instanceof ScabbardItem scabbardItem) {
                            scabbardItem.drawOrSheathSword(player, stack);
                        }
                    }
                });
            }


        });


    }
}