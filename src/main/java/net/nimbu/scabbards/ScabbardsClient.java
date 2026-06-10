package net.nimbu.scabbards;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.nimbu.scabbards.keybinds.ModKeybinds;
import net.nimbu.scabbards.networking.ScabbardKeyPressedPayload;
import net.nimbu.scabbards.renderer.entity.layers.ModModelLayers;
import net.nimbu.scabbards.renderer.entity.layers.ScabbardLayer;
import net.nimbu.scabbards.renderer.entity.model.ScabbardModel;

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
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        EntityRenderer<? extends Player> renderer =
                event.getSkin(PlayerSkin.Model.WIDE);

        if (renderer instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(
                    new ScabbardLayer(playerRenderer)
            );
        }

        EntityRenderer<? extends Player> slimRenderer =
                event.getSkin(PlayerSkin.Model.SLIM);

        if (slimRenderer instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(
                    new ScabbardLayer(playerRenderer)
            );
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) { //detect client input and send to server
        while (ModKeybinds.SCABBARD_KEY.consumeClick()) {

            PacketDistributor.sendToServer(new ScabbardKeyPressedPayload());
        }
    }
}


