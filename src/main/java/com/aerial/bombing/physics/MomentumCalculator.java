package com.aerial.bombing.physics;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * 高精度动量计算器 (Forge Version)
 */
public class MomentumCalculator {

    /**
     * 计算高精度的真实投弹动量
     */
    public static Vec3 calculateRealisticMomentum(Player player) {
        Vec3 playerVelocity = player.getDeltaMovement();
        Vec3 lookVector = player.getLookAngle();

        double verticalSpeed = playerVelocity.y();

        double initialX = playerVelocity.x();
        double initialZ = playerVelocity.z();

        // 俯仰角 (弧度)
        double pitch = Math.asin(-lookVector.y());

        double pitchFactor = -Math.sin(pitch) * 0.3;
        double initialY = verticalSpeed + pitchFactor;

        double dragEffect = 0.98;
        initialX *= dragEffect;
        initialZ *= dragEffect;

        return new Vec3(initialX, initialY, initialZ);
    }

    /**
     * 计算高精度的投弹位置
     */
    public static Vec3 calculateDropPosition(Player player) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVector = player.getLookAngle();
        Vec3 playerVelocity = player.getDeltaMovement();

        Vec3 forward = new Vec3(lookVector.x(), 0, lookVector.z()).normalize();
        Vec3 offset = forward.scale(1.5).add(0, -1.2, 0);

        double horizontalSpeed = Math.sqrt(playerVelocity.x() * playerVelocity.x() + playerVelocity.z() * playerVelocity.z());

        if (horizontalSpeed > 0.5) {
            offset = offset.add(forward.scale(0.5));
        }

        if (Math.abs(playerVelocity.y()) > 0.3) {
            if (playerVelocity.y() > 0) {
                offset = offset.add(0, -0.3, 0);
            } else {
                offset = offset.add(0, 0.2, 0);
            }
        }

        double pitch = Math.asin(-lookVector.y());
        if (Math.abs(pitch) > 0.3) {
            double pitchAdjust = Math.sin(pitch) * 0.5;
            offset = offset.add(0, -pitchAdjust, 0);
        }

        return eyePos.add(offset);
    }

    /**
     * 计算预测命中点 (仅供显示参考)
     */
    public static Vec3 predictImpactPoint(Player player, double gravity, int fuseTicks) {
        Vec3 initialPosition = calculateDropPosition(player);
        Vec3 initialVelocity = calculateRealisticMomentum(player);

        double flightTime = fuseTicks / 20.0;

        double x = initialPosition.x() + initialVelocity.x() * flightTime;
        double z = initialPosition.z() + initialVelocity.z() * flightTime;
        double y = initialPosition.y() + initialVelocity.y() * flightTime - 0.5 * gravity * flightTime * flightTime;

        return new Vec3(x, y, z);
    }
}
