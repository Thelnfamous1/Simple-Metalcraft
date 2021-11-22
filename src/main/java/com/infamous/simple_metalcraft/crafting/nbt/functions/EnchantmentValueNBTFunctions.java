package com.infamous.simple_metalcraft.crafting.nbt.functions;

import com.infamous.simple_metalcraft.crafting.nbt.NBTFunction;
import com.infamous.simple_metalcraft.crafting.nbt.NBTOperator;
import net.minecraft.nbt.CompoundTag;

public class EnchantmentValueNBTFunctions {

    private EnchantmentValueNBTFunctions(){
        throw new IllegalStateException("Utility class");
    }

    public static final NBTFunction APPEND_TO_BASE
            = (baseTag, additiveTag) -> combineEnchantmentValues(baseTag, additiveTag, true);

    public static final NBTFunction MERGE_TO_BASE
            = (baseTag, additiveTag) -> combineEnchantmentValues(baseTag, additiveTag, false);

    public static final NBTFunction REPLACE_WITH_ADDITIVE
            = (baseTag, additiveTag) -> {
        int additiveEnchantmentValue = additiveTag.getInt(NBTOperator.ENCHANTMENT_VALUE_BONUS_TAG_NAME);
        baseTag.putInt(NBTOperator.ENCHANTMENT_VALUE_BONUS_TAG_NAME, additiveEnchantmentValue);
        return baseTag;
    };

    private static CompoundTag combineEnchantmentValues(CompoundTag baseTag, CompoundTag additiveTag, boolean addTogether) {
        int baseEnchantmentValue = additiveTag.getInt(NBTOperator.ENCHANTMENT_VALUE_BONUS_TAG_NAME);
        int additiveEnchantmentValue = additiveTag.getInt(NBTOperator.ENCHANTMENT_VALUE_BONUS_TAG_NAME);
        int newEnchantmentValue = baseEnchantmentValue;
        if(addTogether){
            newEnchantmentValue += additiveEnchantmentValue;
        } else{
            newEnchantmentValue = Math.max(baseEnchantmentValue, additiveEnchantmentValue);
        }
        baseTag.putInt(NBTOperator.ENCHANTMENT_VALUE_BONUS_TAG_NAME, newEnchantmentValue);
        return baseTag;
    }
}
