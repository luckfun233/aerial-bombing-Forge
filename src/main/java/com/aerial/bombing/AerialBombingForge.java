package com.aerial.bombing;

import com.aerial.bombing.capability.OwnerProvider;
import com.aerial.bombing.config.ModConfigs;
import com.aerial.bombing.network.PacketHandler;
import com.aerial.bombing.util.AerialBombingManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(AerialBombingForge.MOD_ID)
public class AerialBombingForge {
    public static final String MOD_ID = "aerialbombing";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public AerialBombingForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册通用设置事件
        modEventBus.addListener(this::commonSetup);
        // 注册能力
        modEventBus.addListener(OwnerProvider::register);

        // 修正：将配置类型从 SERVER 更改为 COMMON
        // 这将使“配置”按钮可用，并会在 .minecraft/config/ 文件夹中生成文件
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.SPEC, "aerialbombing-common.toml");

        // 将事件监听器注册到Forge的事件总线上
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addGenericListener(net.minecraft.world.entity.Entity.class, OwnerProvider::onAttachCapabilities);

        // 初始化我们的单例管理器
        AerialBombingManager.getInstance().initialize();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // commonSetup现在只负责需要延迟执行的任务，比如网络注册
        event.enqueueWork(() -> {
            LOGGER.info("Setting up Aerial Bombing networking...");
            PacketHandler.register();
            LOGGER.info("Networking setup complete.");
        });
    }
}
