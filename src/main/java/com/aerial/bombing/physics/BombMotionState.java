package com.aerial.bombing.physics;

import net.minecraft.world.phys.Vec3;

/**
 * 炸弹运动状态数据类 (Forge Version)
 */
public class BombMotionState {
    public Vec3 position;      // 位置
    public Vec3 velocity;      // 线速度
    public Vec3 angularVelocity; // 角速度

    public BombMotionState() {
        this.position = Vec3.ZERO;
        this.velocity = Vec3.ZERO;
        this.angularVelocity = Vec3.ZERO;
    }

    public BombMotionState(Vec3 position, Vec3 velocity, Vec3 angularVelocity) {
        this.position = position;
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
    }
}
