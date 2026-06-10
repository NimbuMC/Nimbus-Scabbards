package net.nimbu.scabbards.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {

    public static final String CATEGORY = "key.categories.scabbards";

    public static KeyMapping SCABBARD_KEY;

    public static void register() {
        SCABBARD_KEY = new KeyMapping(
                "key.scabbards.scabbard_key",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                CATEGORY
        );
    }
}
