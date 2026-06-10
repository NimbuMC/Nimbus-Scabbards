package net.nimbu.scabbards.networking;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.nimbu.scabbards.Scabbards;

public class ModNetworking {

    public static final CustomPacketPayload.Type<ScabbardKeyPressedPayload> SCABBARD_KEY =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(Scabbards.MOD_ID, "scabbard_key")
            );

    public static void register(RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                ScabbardKeyPressedPayload.TYPE,
                ScabbardKeyPressedPayload.STREAM_CODEC,
                ScabbardKeyPressedPayload::handle
        );
    }


}