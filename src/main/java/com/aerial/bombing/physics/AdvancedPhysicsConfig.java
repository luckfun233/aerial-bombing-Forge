package com.aerial.bombing.physics;

import com.aerial.bombing.config.ConfigHolder; // <-- 导入新的持有者类

// 这个类现在从 ConfigHolder 获取数据
public class AdvancedPhysicsConfig {
    public int horizontalInertia;
    public int verticalInertia;
    public int throwForce;
    public boolean enableAngularVelocity;
    public boolean enableAdvancedDrag;
    public int angularMomentumTransfer;

    public static AdvancedPhysicsConfig createFromConfig() {
        AdvancedPhysicsConfig config = new AdvancedPhysicsConfig();
        // 从 ConfigHolder 读取值
        config.horizontalInertia = ConfigHolder.horizontalInertia;
        config.verticalInertia = ConfigHolder.verticalInertia;
        config.throwForce = ConfigHolder.throwForce;
        config.enableAngularVelocity = ConfigHolder.enableAngularVelocity;
        config.enableAdvancedDrag = ConfigHolder.enableAdvancedDrag;
        config.angularMomentumTransfer = ConfigHolder.angularMomentumTransfer;
        return config;
    }
}
