package com.aerial.bombing.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.TntBlock;

public class TntValidator {
    /**
     * 智能检查物品是否为有效的TNT类型 (Forge 1.20.1 Version)
     * 优先检查方块类型，然后回退到ID名称检查，兼容性更强且误判率更低。
     *
     * @param stack 要检查的物品堆
     * @return 如果是有效的TNT返回true，否则返回false
     */
    public static boolean isValidTnt(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        Item item = stack.getItem();

        // **首要检查：基于方块类型**
        // 这是最可靠的方式，适用于原版和大多数规范的模组TNT。
        if (item instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof TntBlock) {
                return true; // 绝对是TNT方块
            }
        }

        // **后备检查：基于物品ID**
        // 适用于那些没有对应方块、直接生成实体的模组TNT物品。
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null) {
            return false; // 未注册的物品
        }

        String path = id.getPath().toLowerCase();

        // 排除矿车
        if (path.contains("minecart")) {
            return false;
        }

        // 检查是否包含"tnt"关键字
        return path.contains("tnt");
    }
}
