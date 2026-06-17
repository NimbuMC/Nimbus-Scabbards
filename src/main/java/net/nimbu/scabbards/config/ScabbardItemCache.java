package net.nimbu.scabbards.config;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.Set;

public class ScabbardItemCache {

    private static final Set<Item> SCABBARD_EXTRAS = new HashSet<>();
    private static final Set<Item> WEAPON_HOLSTER_EXTRAS = new HashSet<>();

    public static void reload() {
        Registry<Item> registry = BuiltInRegistries.ITEM;

        SCABBARD_EXTRAS.clear();
        WEAPON_HOLSTER_EXTRAS.clear();

        for (String id : ScabbardConfig.SCABBARD_EXTRAS.get()) {
            Item item = registry.get(ResourceLocation.parse(id));
            if (item != Items.AIR) {
                SCABBARD_EXTRAS.add(item);
            }
        }

        for (String id : ScabbardConfig.WEAPON_HOLSTER_EXTRAS.get()) {
            Item item = registry.get(ResourceLocation.parse(id));
            if (item != Items.AIR) {
                WEAPON_HOLSTER_EXTRAS.add(item);
            }
        }
    }

    public static boolean isScabbardExtra(Item item) {
        return SCABBARD_EXTRAS.contains(item);
    }

    public static boolean isWeaponHolsterExtra(Item item) {
        return WEAPON_HOLSTER_EXTRAS.contains(item);
    }
}
