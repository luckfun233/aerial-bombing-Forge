package com.aerial.bombing.capability;

import com.aerial.bombing.AerialBombingForge;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType; // 导入 EntityType
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OwnerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<IOwner> OWNER_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    private IOwner owner = null;
    private final LazyOptional<IOwner> optional = LazyOptional.of(this::getOrCreate);

    private IOwner getOrCreate() {
        if (this.owner == null) {
            this.owner = new OwnerCapability(null);
        }
        return this.owner;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == OWNER_CAP) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (getOrCreate().getOwnerUuid() != null) {
            tag.putUUID("ownerUuid", getOrCreate().getOwnerUuid());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.hasUUID("ownerUuid")) {
            getOrCreate().setOwnerUuid(nbt.getUUID("ownerUuid"));
        }
    }

    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        // 全新的方法：直接比较实体的类型，不再使用 instanceof
        if (event.getObject().getType() == EntityType.TNT) {
            event.addCapability(ResourceLocation.fromNamespaceAndPath(AerialBombingForge.MOD_ID, "owner"), new OwnerProvider());
        }
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(IOwner.class);
    }
}
