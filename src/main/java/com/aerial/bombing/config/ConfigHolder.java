package com.aerial.bombing.config;

import com.aerial.bombing.AerialBombingForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = AerialBombingForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHolder {

    public static boolean enableAerialBombing;
    public static boolean requireFlintAndSteel;
    public static int bombCooldownMs;
    public static boolean useAdvancedPhysics;
    public static int horizontalInertia;
    public static int verticalInertia;
    public static int throwForce;
    public static boolean enableAngularVelocity;
    public static boolean enableAdvancedDrag;
    public static int angularMomentumTransfer;

    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent.Loading event) {
        // 确保配置与我们的MOD ID匹配
        if (event.getConfig().getModId().equals(AerialBombingForge.MOD_ID)) {
            updateConfigValues();
        }
    }

    @SubscribeEvent
    public static void onConfigReload(final ModConfigEvent.Reloading event) {
        // 确保配置与我们的MOD ID匹配
        if (event.getConfig().getModId().equals(AerialBombingForge.MOD_ID)) {
            updateConfigValues();
        }
    }

    @SuppressWarnings("deprecation") // <-- 在这里添加注解，明确告诉IDE忽略弃用警告
    private static void updateConfigValues() {
        // 这个方法现在可以安全地调用 .get() 而不产生任何警告
        enableAerialBombing = ModConfigs.ENABLE_AERIAL_BOMBING.get();
        requireFlintAndSteel = ModConfigs.REQUIRE_FLINT_AND_STEEL.get();
        bombCooldownMs = ModConfigs.BOMB_COOLDOWN_MS.get();
        useAdvancedPhysics = ModConfigs.USE_ADVANCED_PHYSICS.get();
        horizontalInertia = ModConfigs.HORIZONTAL_INERTIA.get();
        verticalInertia = ModConfigs.VERTICAL_INERTIA.get();
        throwForce = ModConfigs.THROW_FORCE.get();
        enableAngularVelocity = ModConfigs.ENABLE_ANGULAR_VELOCITY.get();
        enableAdvancedDrag = ModConfigs.ENABLE_ADVANCED_DRAG.get();
        angularMomentumTransfer = ModConfigs.ANGULAR_MOMENTUM_TRANSFER.get();
    }
}
