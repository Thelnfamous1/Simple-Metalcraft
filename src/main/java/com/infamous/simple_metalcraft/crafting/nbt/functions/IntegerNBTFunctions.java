package com.infamous.simple_metalcraft.crafting.nbt.functions;

import com.infamous.simple_metalcraft.crafting.nbt.NBTFunction;
import net.minecraft.nbt.CompoundTag;

public class IntegerNBTFunctions {

    private IntegerNBTFunctions(){
        throw new IllegalStateException("Utility class");
    }

    public static final NBTFunction APPEND_TO_BASE
            = (baseTag, additiveTag, tagName) -> combineIntValues(baseTag, additiveTag, tagName, true);

    public static final NBTFunction MERGE_TO_BASE
            = (baseTag, additiveTag, tagName) -> combineIntValues(baseTag, additiveTag, tagName, false);

    public static final NBTFunction REPLACE_WITH_ADDITIVE
            = (baseTag, additiveTag, tagName) -> {
        int additiveValue = additiveTag.getInt(tagName);
        baseTag.putInt(tagName, additiveValue);
        return baseTag;
    };

    private static CompoundTag combineIntValues(CompoundTag baseTag, CompoundTag additiveTag, String tagName, boolean addTogether) {
        int baseValue = additiveTag.getInt(tagName);
        int additiveValue = additiveTag.getInt(tagName);
        int newValue = baseValue;
        if(addTogether){
            newValue += additiveValue;
        } else{
            newValue = Math.max(baseValue, additiveValue);
        }
        baseTag.putInt(tagName, newValue);
        return baseTag;
    }
}
