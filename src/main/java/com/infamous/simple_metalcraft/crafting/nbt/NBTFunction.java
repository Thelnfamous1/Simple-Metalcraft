package com.infamous.simple_metalcraft.crafting.nbt;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface NBTFunction {
    CompoundTag call(CompoundTag baseTag, CompoundTag additiveTag, String tagName);
}
