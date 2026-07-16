package net.nimbu.scabbards;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.nimbu.scabbards.item.ModItems;
import net.nimbu.scabbards.keybinds.ModKeybinds;
import net.nimbu.scabbards.networking.ScabbardKeyPressedPayload;
import net.nimbu.scabbards.renderer.HipScabbardRenderer;
import net.nimbu.scabbards.renderer.ScabbardRenderer;
import net.nimbu.scabbards.renderer.entity.layers.ModModelLayers;
import net.nimbu.scabbards.renderer.entity.model.ScabbardModel;
import net.nimbu.scabbards.renderer.entity.model.WeaponHolsterModel;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = Scabbards.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = Scabbards.MOD_ID, value = Dist.CLIENT)
public class ScabbardsClient {
    public ScabbardsClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                ModModelLayers.SCABBARD_LAYER,
                ScabbardModel::createBodyLayer
        );
        event.registerLayerDefinition(
                ModModelLayers.WEAPON_HOLSTER_LAYER,
                WeaponHolsterModel::createBodyLayer
        );
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        ModKeybinds.register();
        event.register(ModKeybinds.SCABBARD_KEY);
    }


    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) { //detect client input and send to server
        while (ModKeybinds.SCABBARD_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new ScabbardKeyPressedPayload());
        }
    }

    @SubscribeEvent
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
            CuriosRendererRegistry.register(
                    ModItems.HIP_SCABBARD.get(),
                    () -> new HipScabbardRenderer(0)
            );


            Minecraft minecraft = Minecraft.getInstance();

            minecraft.getItemColors().register(
                    (stack, tintIndex) -> {
                        if (tintIndex == 0) {
                            return 0xFF000000 | //ORs in the alpha value
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
                            return 0xFF000000 | //ORs in the alpha value
                                    stack.getOrDefault(
                                            DataComponents.DYED_COLOR,
                                            new DyedItemColor(0xd37d19, false)
                                    ).rgb();
                        }
                        return 0xFFFFFFFF;
                    },
                    ModItems.WEAPON_HOlSTER.get()
            );
            minecraft.getItemColors().register(
                    (stack, tintIndex) -> {
                        if (tintIndex == 0) {
                            return 0xFF000000 | //ORs in the alpha value
                                    stack.getOrDefault(
                                            DataComponents.DYED_COLOR,
                                            new DyedItemColor(0xd37d19, false)
                                    ).rgb();
                        }
                        return 0xFFFFFFFF;
                    },
                    ModItems.HIP_SCABBARD.get()
            );
        });

    }
}


