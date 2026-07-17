package net.nimbu.scabbards;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.nimbu.scabbards.item.ModItems;
import net.nimbu.scabbards.keybinds.ModKeybinds;
import net.nimbu.scabbards.networking.ScabbardKeyPressedPayload;
import net.nimbu.scabbards.renderer.ScabbardRenderer;
import net.nimbu.scabbards.renderer.entity.layers.ModModelLayers;
import net.nimbu.scabbards.renderer.entity.model.ScabbardModel;
import net.nimbu.scabbards.renderer.entity.model.WeaponHolsterModel;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// This class only loads on physical clients.
@Mod(value = Scabbards.MOD_ID, dist = Dist.CLIENT)
public class ScabbardsClient {

    public ScabbardsClient(IEventBus modEventBus, ModContainer container) {
        // Keep the existing Mods-screen config button.
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                ConfigurationScreen::new
        );

        // Create the client-only key mapping before NeoForge registers it.
        ModKeybinds.register();

        // Startup/registration events belong to the mod event bus.
        modEventBus.addListener(ScabbardsClient::registerLayerDefinitions);
        modEventBus.addListener(ScabbardsClient::registerKeyMappings);
        modEventBus.addListener(ScabbardsClient::onClientSetup);

        // Runtime client ticks belong to the NeoForge/game event bus.
        NeoForge.EVENT_BUS.addListener(ScabbardsClient::onClientTick);
    }

    public static void registerLayerDefinitions(
            EntityRenderersEvent.RegisterLayerDefinitions event
    ) {
        event.registerLayerDefinition(
                ModModelLayers.SCABBARD_LAYER,
                ScabbardModel::createBodyLayer
        );
        event.registerLayerDefinition(
                ModModelLayers.WEAPON_HOLSTER_LAYER,
                WeaponHolsterModel::createBodyLayer
        );
    }

    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ModKeybinds.SCABBARD_KEY);
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        // Safety guard: never crash the client if registration failed.
        if (ModKeybinds.SCABBARD_KEY == null) {
            return;
        }

        while (ModKeybinds.SCABBARD_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ScabbardKeyPressedPayload());
        }
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(
                    ModItems.SCABBARD.get(),
                    () -> new ScabbardRenderer(0)
            );
            CuriosRendererRegistry.register(
                    ModItems.WEAPON_HOlSTER.get(),
                    () -> new ScabbardRenderer(1)
            );

            Minecraft minecraft = Minecraft.getInstance();

            minecraft.getItemColors().register(
                    (stack, tintIndex) -> {
                        if (tintIndex == 0) {
                            return 0xFF000000 |
                                    stack.getOrDefault(
                                            DataComponents.DYED_COLOR,
                                            new DyedItemColor(0xd37d19, false)
                                    ).rgb();
                        }
                        return 0xFFFFFFFF;
                    },
                    ModItems.SCABBARD.get()
            );

            minecraft.getItemColors().register(
                    (stack, tintIndex) -> {
                        if (tintIndex == 0) {
                            return 0xFF000000 |
                                    stack.getOrDefault(
                                            DataComponents.DYED_COLOR,
                                            new DyedItemColor(0xd37d19, false)
                                    ).rgb();
                        }
                        return 0xFFFFFFFF;
                    },
                    ModItems.WEAPON_HOlSTER.get()
            );
        });
    }
}
