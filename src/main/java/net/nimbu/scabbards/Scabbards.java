package net.nimbu.scabbards;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.nimbu.scabbards.component.ModDataComponents;
import net.nimbu.scabbards.config.ScabbardConfig;
import net.nimbu.scabbards.config.ScabbardItemCache;
import net.nimbu.scabbards.item.ModItems;
import net.nimbu.scabbards.networking.ModNetworking;
import org.slf4j.Logger;

@Mod(Scabbards.MOD_ID)
public class Scabbards {

    public static final String MOD_ID = "scabbards";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Scabbards(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::onConfigReload);
        modEventBus.addListener(ModNetworking::register);

        modContainer.registerConfig(ModConfig.Type.SERVER, ScabbardConfig.SPEC);

        // Do not initialize client KeyMapping classes here.
        // ScabbardsClient handles key creation and registration on the client.
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.SCABBARD);
            event.accept(ModItems.WEAPON_HOlSTER);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        ScabbardItemCache.reload();
    }

    private void onConfigReload(ModConfigEvent.Reloading event) {
        ScabbardItemCache.reload();
    }
}
