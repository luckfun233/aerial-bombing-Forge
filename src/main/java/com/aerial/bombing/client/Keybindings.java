package com.aerial.bombing.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class Keybindings {
    public static final String KEY_CATEGORY_AERIAL_BOMBING = "key.category.aerialbombing";
    public static final String KEY_DROP_BOMB = "key.aerialbombing.drop_bomb";

    public static final KeyMapping DROP_BOMB_KEY = new KeyMapping(
            KEY_DROP_BOMB,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_G,
            KEY_CATEGORY_AERIAL_BOMBING
    );
}

