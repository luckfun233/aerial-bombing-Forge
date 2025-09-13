package com.aerial.bombing.physics;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * 高级动量计算器 (Forge Version)
 */
public class AdvancedMomentumCalculator {

    private static final double GRAVITY = 0.08;
    private static final double AIR_DENSITY = 1.225;
    private static final double DRAG_COEFFICIENT = 0.47;
    private static final double BOMB_MASS = 5.0;
    private static final double BOMB_RADIUS = 0.25;

    public static BombMotionState calculateAdvancedMomentum(Player player, AdvancedPhysicsConfig config) {
        Vec3 playerLinearVelocity = player.getDeltaMovement();
        Vec3 playerAngularVelocity = estimateAngularVelocity(player);
        Vec3 dropOffset = calculateDropOffset(player);
        Vec3 dropPosition = player.position().add(0, player.getEyeHeight(player.getPose()), 0).add(dropOffset);
        Vec3 tangentialVelocity = calculateTangentialVelocity(playerAngularVelocity, dropOffset);
        Vec3 initialVelocity = playerLinearVelocity.add(tangentialVelocity);
        initialVelocity = applyInertiaCoefficients(initialVelocity, config);
        Vec3 throwForce = calculateThrowForce(player, config);
        initialVelocity = initialVelocity.add(throwForce);

        BombMotionState motionState = new BombMotionState();
        motionState.position = dropPosition;
        motionState.velocity = initialVelocity;
        motionState.angularVelocity = playerAngularVelocity.scale(0.3);

        return motionState;
    }

    private static Vec3 estimateAngularVelocity(Player player) {
        double yawRate = 0;
        double pitchRate = 0;
        double rollRate = 0;

        Vec3 moveDirection = new Vec3(player.zza, 0, player.xxa);
        if (moveDirection.lengthSqr() > 0.01) {
            rollRate = moveDirection.length() * 0.5;
        }

        return new Vec3(pitchRate, yawRate, rollRate);
    }

    private static Vec3 calculateTangentialVelocity(Vec3 angularVelocity, Vec3 positionVector) {
        return angularVelocity.cross(positionVector);
    }

    private static Vec3 calculateDropOffset(Player player) {
        Vec3 lookVec = player.getLookAngle();
        Vec3 forward = new Vec3(lookVec.x(), 0, lookVec.z()).normalize();
        Vec3 offset = forward.scale(1.5).add(0, -1.0, 0);

        Vec3 velocity = player.getDeltaMovement();
        double speed = velocity.length();

        if (speed > 0.5) {
            offset = offset.add(forward.scale(speed * 0.2));
        }

        return offset;
    }

    private static Vec3 applyInertiaCoefficients(Vec3 velocity, AdvancedPhysicsConfig config) {
        double horizontalInertia = config.horizontalInertia / 100.0;
        double verticalInertia = config.verticalInertia / 100.0;

        return new Vec3(
                velocity.x() * horizontalInertia,
                velocity.y() * verticalInertia,
                velocity.z() * horizontalInertia
        );
    }

    private static Vec3 calculateThrowForce(Player player, AdvancedPhysicsConfig config) {
        double throwForce = config.throwForce / 100.0 * 0.8;
        Vec3 lookVec = player.getLookAngle();
        Vec3 throwDirection = new Vec3(lookVec.x(), lookVec.y() - 0.2, lookVec.z()).normalize();

        return throwDirection.scale(throwForce);
    }

    public static Vec3 calculateAirResistance(Vec3 velocity) {
        double speed = velocity.length();
        if (speed < 0.001) {
            return Vec3.ZERO;
        }

        double frontalArea = Math.PI * BOMB_RADIUS * BOMB_RADIUS;
        double dragMagnitude = 0.5 * AIR_DENSITY * speed * speed * DRAG_COEFFICIENT * frontalArea;
        Vec3 dragDirection = velocity.normalize().scale(-1);
        Vec3 dragForce = dragDirection.scale(dragMagnitude);

        return dragForce.scale(1.0 / BOMB_MASS);
    }

    public static BombMotionState updateMotion(BombMotionState motionState, double deltaTime) {
        Vec3 gravityAcceleration = new Vec3(0, -GRAVITY, 0);
        Vec3 dragAcceleration = calculateAirResistance(motionState.velocity);
        Vec3 totalAcceleration = gravityAcceleration.add(dragAcceleration);
        Vec3 newVelocity = motionState.velocity.add(totalAcceleration.scale(deltaTime));
        Vec3 newPosition = motionState.position.add(motionState.velocity.scale(deltaTime));
        Vec3 newAngularVelocity = motionState.angularVelocity.scale(0.95);

        BombMotionState newState = new BombMotionState();
        newState.position = newPosition;
        newState.velocity = newVelocity;
        newState.angularVelocity = newAngularVelocity;

        return newState;
    }

    public static Vec3 predictImpactPoint(Player player, AdvancedPhysicsConfig config, double maxTime, double timeStep) {
        BombMotionState motionState = calculateAdvancedMomentum(player, config);

        double time = 0;
        while (time < maxTime) {
            if (motionState.position.y() <= player.level().getMinBuildHeight()) {
                return motionState.position;
            }
            motionState = updateMotion(motionState, timeStep);
            time += timeStep;
        }

        return motionState.position;
    }
}
