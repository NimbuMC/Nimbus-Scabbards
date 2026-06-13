package net.nimbu.scabbards.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nimbu.scabbards.Scabbards;
import net.nimbu.scabbards.item.custom.ScabbardItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Scabbards.MOD_ID);

    public static final DeferredItem<Item> SCABBARD = ITEMS.register("scabbard",
            () -> new ScabbardItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> WEAPON_HOlSTER = ITEMS.register("weapon_holster",
            () -> new ScabbardItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
