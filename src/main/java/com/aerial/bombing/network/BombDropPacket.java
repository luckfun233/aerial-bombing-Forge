package com.aerial.bombing.network;

import com.aerial.bombing.util.AerialBombingManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BombDropPacket {

    public BombDropPacket() {
    }

    public BombDropPacket(FriendlyByteBuf buf) {
        // No data to read
    }

    public void toBytes(FriendlyByteBuf buf) {
        // No data to write
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // Server-side logic
            ServerPlayer player = context.getSender();
            if (player != null) {
                AerialBombingManager.getInstance().tryAerialBombing(player, player.level());
            }
        });
        return true;
    }
}
