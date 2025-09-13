package com.aerial.bombing.capability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OwnerCapability implements IOwner {
    @Nullable
    private UUID ownerUuid;
    private Level level;

    public OwnerCapability(Level level) {
        this.level = level;
    }

    @Override
    public @Nullable UUID getOwnerUuid() {
        return ownerUuid;
    }

    @Override
    public void setOwnerUuid(@Nullable UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    @Override
    public @Nullable ServerPlayer getOwner() {
        if (ownerUuid != null && level != null && !level.isClientSide()) {
            return level.getServer().getPlayerList().getPlayer(ownerUuid);
        }
        return null;
    }

    @Override
    public void setOwner(@Nullable ServerPlayer owner) {
        this.ownerUuid = owner != null ? owner.getUUID() : null;
    }

    @Override
    public void copyFrom(IOwner source) {
        this.setOwnerUuid(source.getOwnerUuid());
    }
}
