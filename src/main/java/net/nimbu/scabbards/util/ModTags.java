package net.nimbu.scabbards.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.nimbu.scabbards.Scabbards;

public class ModTags {


    //public static final TagKey<Item> SCABBARD_ITEMS = createTag("scabbard");

    public static final TagKey<Item> TOOLS = createTag("tools");

    private static TagKey<Item> createTag(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Scabbards.MOD_ID, name));
    }
}
