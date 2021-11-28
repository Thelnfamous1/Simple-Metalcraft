package com.infamous.simple_metalcraft.integration.jei.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

/**
 * Copied directly from JEI source code - it's not a part of the API, and I see no need to reinvent
 * a perfectly working wheel
 * @author mezz
 */
public class JEIErrorUtil {

    private JEIErrorUtil() {
        throw new IllegalStateException("Utility class");
    }

    @SuppressWarnings("ConstantConditions")
    public static String getItemStackInfo(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return "null";
        }
        Item item = itemStack.getItem();
        final String itemName;
        ResourceLocation registryName = item.getRegistryName();
        if (registryName != null) {
            itemName = registryName.toString();
        } else if (item instanceof BlockItem) {
            final String blockName;
            Block block = ((BlockItem) item).getBlock();
            if (block == null) {
                blockName = "null";
            } else {
                ResourceLocation blockRegistryName = block.getRegistryName();
                if (blockRegistryName != null) {
                    blockName = blockRegistryName.toString();
                } else {
                    blockName = block.getClass().getName();
                }
            }
            itemName = "BlockItem(" + blockName + ")";
        } else {
            itemName = item.getClass().getName();
        }

        CompoundTag nbt = itemStack.getTag();
        if (nbt != null) {
            return itemStack + " " + itemName + " nbt:" + nbt;
        }
        return itemStack + " " + itemName;
    }
}
