package com.aerial.bombing.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface IOwner {
    @Nullable
    UUID getOwnerUuid();
    void setOwnerUuid(@Nullable UUID ownerUuid);

    @Nullable
    ServerPlayer getOwner();
    void setOwner(@Nullable ServerPlayer owner);

    void copyFrom(IOwner source);
}
