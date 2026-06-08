package net.nimbu.scabbards.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.nimbu.scabbards.Scabbards;

import java.util.function.UnaryOperator;

public class ModDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Scabbards.MOD_ID);


    public static DeferredHolder<DataComponentType<?>, DataComponentType<StoredItem>>
            STORED_ITEM = DATA_COMPONENT_TYPES.register("stored_item",
            () -> DataComponentType.<StoredItem>builder()
                    .persistent(StoredItem.CODEC)
                    .networkSynchronized(StoredItem.STREAM_CODEC)
                    .build());

    private static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                          UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus){
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
