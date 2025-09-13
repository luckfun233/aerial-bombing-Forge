package com.aerial.bombing.util;

import com.aerial.bombing.capability.OwnerProvider;
import com.aerial.bombing.config.ConfigHolder; // <-- 导入新的持有者类
import com.aerial.bombing.physics.AdvancedMomentumCalculator;
import com.aerial.bombing.physics.AdvancedPhysicsConfig;
import com.aerial.bombing.physics.BombMotionState;
import com.aerial.bombing.physics.MomentumCalculator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AerialBombingManager {
    private static AerialBombingManager INSTANCE;
    public static final Logger LOGGER = LoggerFactory.getLogger("AerialBombingManager");

    private final Map<UUID, Long> lastBombTime = new HashMap<>();

    public static AerialBombingManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AerialBombingManager();
        }
        return INSTANCE;
    }

    public void initialize() {
        MinecraftForge.EVENT_BUS.addListener(this::onServerTick);
    }

    private void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            long currentTime = System.currentTimeMillis();
            lastBombTime.entrySet().removeIf(entry -> currentTime - entry.getValue() > 10000);
        }
    }

    public boolean tryAerialBombing(Player player, Level world) {
        // 从 ConfigHolder 读取配置值
        if (!ConfigHolder.enableAerialBombing) {
            return false;
        }

        if (!player.isFallFlying()) {
            if (!world.isClientSide) {
                player.sendSystemMessage(Component.translatable("text.aerial_bombing.requires_elytra"));
            }
            return false;
        }

        ItemStack mainHandStack = player.getMainHandItem();
        if (!TntValidator.isValidTnt(mainHandStack)) {
            if (!world.isClientSide) {
                player.sendSystemMessage(Component.translatable("text.aerial_bombing.requires_tnt"));
            }
            return false;
        }

        if (ConfigHolder.requireFlintAndSteel) {
            ItemStack offHandStack = player.getOffhandItem();
            if (offHandStack.getItem() != Items.FLINT_AND_STEEL) {
                if (!world.isClientSide) {
                    player.sendSystemMessage(Component.translatable("text.aerial_bombing.requires_flint"));
                }
                return false;
            }
        }

        UUID playerId = player.getUUID();
        long currentTime = System.currentTimeMillis();
        long lastTime = lastBombTime.getOrDefault(playerId, 0L);

        if (currentTime - lastTime < ConfigHolder.bombCooldownMs) {
            return false;
        }

        return executeBombing(player, world, mainHandStack);
    }

    private boolean executeBombing(Player player, Level world, ItemStack tntStack) {
        if (world.isClientSide()) {
            return true;
        }

        ResourceLocation itemIdentifier = BuiltInRegistries.ITEM.getKey(tntStack.getItem());
        ResourceLocation entityIdentifier = ResourceLocation.fromNamespaceAndPath(itemIdentifier.getNamespace(), itemIdentifier.getPath());

        Optional<EntityType<?>> entityTypeOptional = EntityType.byString(entityIdentifier.toString());

        if (entityTypeOptional.isEmpty()) {
            LOGGER.warn("Could not find a matching entity type for item '{}', falling back to minecraft:tnt", itemIdentifier);
            entityTypeOptional = Optional.of(EntityType.TNT);
        }

        EntityType<?> entityType = entityTypeOptional.get();
        Entity spawnedEntity = entityType.create(world);

        if (spawnedEntity == null) {
            LOGGER.error("Failed to create entity for type '{}'!", entityIdentifier);
            return false;
        }

        if (!player.isCreative()) {
            tntStack.shrink(1);
        }

        lastBombTime.put(player.getUUID(), System.currentTimeMillis());

        BombMotionState motionState;
        if (ConfigHolder.useAdvancedPhysics) {
            motionState = AdvancedMomentumCalculator.calculateAdvancedMomentum(player, AdvancedPhysicsConfig.createFromConfig());
        } else {
            Vec3 position = MomentumCalculator.calculateDropPosition(player);
            Vec3 velocity = MomentumCalculator.calculateRealisticMomentum(player);
            motionState = new BombMotionState(position, velocity, Vec3.ZERO);
        }

        spawnedEntity.setPos(motionState.position);
        spawnedEntity.setDeltaMovement(motionState.velocity);

        spawnedEntity.getCapability(OwnerProvider.OWNER_CAP).ifPresent(ownerCap -> {
            if (player instanceof ServerPlayer sp) {
                ownerCap.setOwner(sp);
            }
        });

        world.addFreshEntity(spawnedEntity);
        world.playSound(null, player.blockPosition(), SoundEvents.TNT_PRIMED, SoundSource.PLAYERS, 1.0F, 1.0F);
        LOGGER.info("Player {} dropped a {} entity.", player.getName().getString(), entityIdentifier);

        return true;
    }

    public boolean canPlayerBomb(Player player) {
        if (!ConfigHolder.enableAerialBombing) return false;
        if (!player.isFallFlying()) return false;
        if (!TntValidator.isValidTnt(player.getMainHandItem())) return false;

        if (ConfigHolder.requireFlintAndSteel) {
            return player.getOffhandItem().getItem() == Items.FLINT_AND_STEEL;
        }

        return true;
    }
}
