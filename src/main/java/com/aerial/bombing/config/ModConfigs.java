package com.aerial.bombing.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // 配置项定义
    public static final ForgeConfigSpec.BooleanValue ENABLE_AERIAL_BOMBING;
    public static final ForgeConfigSpec.BooleanValue REQUIRE_FLINT_AND_STEEL;
    public static final ForgeConfigSpec.IntValue BOMB_COOLDOWN_MS;
    public static final ForgeConfigSpec.BooleanValue USE_ADVANCED_PHYSICS;
    public static final ForgeConfigSpec.IntValue HORIZONTAL_INERTIA;
    public static final ForgeConfigSpec.IntValue VERTICAL_INERTIA;
    public static final ForgeConfigSpec.IntValue THROW_FORCE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ANGULAR_VELOCITY;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ADVANCED_DRAG;
    public static final ForgeConfigSpec.IntValue ANGULAR_MOMENTUM_TRANSFER;

    static {
        BUILDER.comment("Aerial Bombing Configuration").push("general");
        ENABLE_AERIAL_BOMBING = BUILDER
                .comment("If enabled, players can drop TNT while flying with Elytra.")
                .translation("config.aerialbombing.enableAerialBombing")
                .define("enableAerialBombing", true);
        REQUIRE_FLINT_AND_STEEL = BUILDER
                .comment("If enabled, players must hold Flint and Steel in their off-hand.")
                .translation("config.aerialbombing.requireFlintAndSteel")
                .define("requireFlintAndSteel", true);
        BOMB_COOLDOWN_MS = BUILDER
                .comment("The cooldown time in milliseconds between each bomb drop.")
                .translation("config.aerialbombing.bombCooldownMs")
                .defineInRange("bombCooldownMs", 1000, 0, 5000);
        BUILDER.pop();

        BUILDER.push("physics");
        USE_ADVANCED_PHYSICS = BUILDER
                .comment("Enable to use the advanced physics engine with more variables.", "Disable to use the original, simpler physics simulation.")
                .translation("config.aerialbombing.useAdvancedPhysics")
                .define("useAdvancedPhysics", true);

        BUILDER.comment("Advanced Physics Parameters").push("advanced_physics");
        HORIZONTAL_INERTIA = BUILDER
                .comment("Percentage of player's horizontal speed transferred to the bomb.")
                .translation("config.aerialbombing.horizontalInertia")
                .defineInRange("horizontalInertia", 95, 0, 100);
        VERTICAL_INERTIA = BUILDER
                .comment("Percentage of player's vertical speed transferred to the bomb.")
                .translation("config.aerialbombing.verticalInertia")
                .defineInRange("verticalInertia", 85, 0, 100);
        THROW_FORCE = BUILDER
                .comment("Additional force applied in the player's looking direction.")
                .translation("config.aerialbombing.throwForce")
                .defineInRange("throwForce", 20, 0, 100);
        ENABLE_ANGULAR_VELOCITY = BUILDER
                .comment("Simulates bomb rotation by inheriting some of the player's angular velocity.")
                .translation("config.aerialbombing.enableAngularVelocity")
                .define("enableAngularVelocity", true);
        ENABLE_ADVANCED_DRAG = BUILDER
                .comment("Enables a more realistic air resistance model.")
                .translation("config.aerialbombing.enableAdvancedDrag")
                .define("enableAdvancedDrag", true);
        ANGULAR_MOMENTUM_TRANSFER = BUILDER
                .comment("Percentage of player's angular momentum transferred to the bomb.")
                .translation("config.aerialbombing.angularMomentumTransfer")
                .defineInRange("angularMomentumTransfer", 70, 0, 100);
        BUILDER.pop(2);

        SPEC = BUILDER.build();
    }
}
