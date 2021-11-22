package com.infamous.simple_metalcraft.mixin;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.nbt.NBTOperator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Random;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), ordinal = 1, method = "getEnchantmentCost")
    private static int getEnchantmentValueFromTagForCost(int defaultValue, Random p_44873_, int p_44874_, int p_44875_, ItemStack stack){
        return checkEnchantmentValueTag(defaultValue, stack);
    }

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), ordinal = 1, method = "selectEnchantment")
    private static int getEnchantmentValueFromTagForSelection(int defaultValue, Random p_44910_, ItemStack stack, int p_44912_, boolean p_44913_){
        return checkEnchantmentValueTag(defaultValue, stack);
    }

    private static int checkEnchantmentValueTag(int defaultValue, ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains(NBTOperator.ENCHANTMENT_VALUE_BONUS_TAG_NAME)) {
            return defaultValue + stack.getTag().getInt(NBTOperator.ENCHANTMENT_VALUE_BONUS_TAG_NAME);
        } else{
            return defaultValue;
        }
    }
}
