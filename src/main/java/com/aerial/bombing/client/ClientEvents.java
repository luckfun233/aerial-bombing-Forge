package com.aerial.bombing.client;

import com.aerial.bombing.AerialBombingForge;
import com.aerial.bombing.network.BombDropPacket;
import com.aerial.bombing.network.PacketHandler;
import com.aerial.bombing.util.AerialBombingManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AerialBombingForge.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (Keybindings.DROP_BOMB_KEY.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.level != null) {
                if (AerialBombingManager.getInstance().canPlayerBomb(mc.player)) {
                    PacketHandler.sendToServer(new BombDropPacket());
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = AerialBombingForge.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(Keybindings.DROP_BOMB_KEY);
        }
    }
}
