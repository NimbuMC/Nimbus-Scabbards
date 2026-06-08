package net.nimbu.scabbards.component;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record StoredItem(ItemStack stack) {

    public static final Codec<StoredItem> CODEC =
            ItemStack.CODEC.xmap(
                    StoredItem::new,
                    StoredItem::stack
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, StoredItem> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC,
                    StoredItem::stack,
                    StoredItem::new
            );
}
