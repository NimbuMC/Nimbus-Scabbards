package net.nimbu.scabbards.item;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nimbu.scabbards.Scabbards;
import net.nimbu.scabbards.item.custom.ScabbardItem;
import net.nimbu.scabbards.util.ModTags;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Scabbards.MOD_ID);

    public static final DeferredItem<Item> SCABBARD = ITEMS.register("scabbard",
            () -> new ScabbardItem(new Item.Properties().stacksTo(1), SwordItem.class));

    public static final DeferredItem<Item> BIG_SCABBARD = ITEMS.register("big_scabbard",
            () -> new ScabbardItem(new Item.Properties().stacksTo(1), SwordItem.class));

    public static final DeferredItem<Item> WEAPON_HOlSTER = ITEMS.register("weapon_holster",
            () -> new ScabbardItem(new Item.Properties().stacksTo(1), TieredItem.class));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
