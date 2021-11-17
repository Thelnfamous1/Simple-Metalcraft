package com.infamous.simple_metalcraft.crafting.nbt.functions;

import com.infamous.simple_metalcraft.crafting.nbt.NBTFunction;
import com.infamous.simple_metalcraft.crafting.nbt.NBTOperator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class EnchantmentNBTFunctions {

    private EnchantmentNBTFunctions(){
        throw new IllegalStateException("Utility class");
    }

    public static final NBTFunction APPEND_TO_BASE_ENCHANTMENTS
            = (baseTag, additiveTag) ->
            combineEnchantmentTags(baseTag, additiveTag, true);

    public static final NBTFunction MERGE_TO_BASE_ENCHANTMENTS
            = (baseTag, additiveTag) ->
            combineEnchantmentTags(baseTag, additiveTag, false);

    public static final NBTFunction REPLACE_WITH_ADDITIVE_ENCHANTMENTS
            = (baseTag, additiveTag) ->
            {
                ListTag additiveEnchantsTag = getEnchantments(additiveTag);
                setEnchantments(baseTag, additiveEnchantsTag);
                return baseTag;
            };

    private static CompoundTag combineEnchantmentTags(CompoundTag baseTag, CompoundTag additiveTag, boolean addTogether) {
        ListTag baseEnchantsTag = getEnchantments(baseTag);
        Map<Enchantment, Integer> baseEnchants = EnchantmentHelper.deserializeEnchantments(baseEnchantsTag);
        ListTag additiveEnchantsTag = getEnchantments(additiveTag);
        Map<Enchantment, Integer> additiveEnchants = EnchantmentHelper.deserializeEnchantments(additiveEnchantsTag);
        ListTag mergedEnchantsTag = combineEnchantments(baseEnchants, additiveEnchants, addTogether);
        setEnchantments(baseTag, mergedEnchantsTag);
        return baseTag;
    }

    private static ListTag getEnchantments(CompoundTag baseTag) {
        return baseTag.getList(NBTOperator.ENCHANTMENTS_TAG_NAME, 10);
    }

    private static void setEnchantments(CompoundTag baseTag, ListTag enchantmentsTag){
        baseTag.put(NBTOperator.ENCHANTMENTS_TAG_NAME, enchantmentsTag);
    }

    public static ListTag combineEnchantments(Map<Enchantment, Integer> baseEnchants, Map<Enchantment, Integer> additiveEnchants, boolean addTogether) {
        ListTag enchantmentsTag = new ListTag();

        for(Map.Entry<Enchantment, Integer> entry : baseEnchants.entrySet()){
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int lvl = entry.getValue();
                if(additiveEnchants.containsKey(enchantment)){
                    int additiveLvl = additiveEnchants.get(enchantment);
                    /*
                        Two options here: Append, or merge.
                        If we are appending, then enchantment levels of the same enchantment
                        get added together.
                            ~ Can end up past the max enchantment level if the appending level is positive.
                        If we are merging, then if the enchantment levels are equal,
                        the resulting level is incremented by 1, otherwise the maximum of
                        the two levels is used.
                            ~ Merging uses the same logic as the Anvil.
                     */
                    if(addTogether){
                        lvl += additiveLvl;
                    } else{
                        if(lvl == additiveLvl){
                            lvl += 1;
                        } else{
                            lvl = Math.max(lvl, additiveLvl);
                        }
                    }
                }
                enchantmentsTag.add(storeEnchantment(enchantment, lvl));
            }
        }

        for(Map.Entry<Enchantment, Integer> entry : additiveEnchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment != null) {
                int lvl = entry.getValue();
                if (!baseEnchants.containsKey(enchantment)) {
                    enchantmentsTag.add(storeEnchantment(enchantment, lvl));
                }
            }
        }
        return enchantmentsTag;
    }

    private static CompoundTag storeEnchantment(Enchantment enchantment, int lvl) {
        return EnchantmentHelper.storeEnchantment(EnchantmentHelper.getEnchantmentId(enchantment), lvl);
    }
}
