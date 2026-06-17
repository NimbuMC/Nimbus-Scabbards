package net.nimbu.scabbards.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class ScabbardConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> SCABBARD_EXTRAS;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> WEAPON_HOLSTER_EXTRAS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("scabbard");

        SCABBARD_EXTRAS = builder
                .comment("Extra items, similar in size to regular default swords (note that any item already under minecraft's SwordItem class will already be in here)")
                .defineListAllowEmpty(
                        "scabbard_extras",
                        List.of(),
                        o -> o instanceof String
                );

        WEAPON_HOLSTER_EXTRAS = builder
                .comment("Extra items, any size (any item under minecraft's TieredItem class will already be in here)")
                .defineListAllowEmpty(
                        "weapon_holster_extras",
                        List.of(),
                        o -> o instanceof String
                );

        builder.pop();

        SPEC=builder.build();
    }
}
