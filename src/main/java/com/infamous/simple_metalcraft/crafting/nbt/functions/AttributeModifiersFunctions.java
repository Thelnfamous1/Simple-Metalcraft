package com.infamous.simple_metalcraft.crafting.nbt.functions;

import com.infamous.simple_metalcraft.crafting.nbt.NBTFunction;
import com.infamous.simple_metalcraft.crafting.nbt.NBTOperator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

// TODO: Finish implementing this
public class AttributeModifiersFunctions {

    public static final String SLOT_TAG_NAME = "Slot";
    public static final String ATTRIBUTE_NAME_TAG_NAME = "AttributeName";

    private AttributeModifiersFunctions(){
        throw new IllegalStateException("Utility class");
    }

    public static final NBTFunction MERGE_TO_BASE =
            ((baseTag, additiveTag, tagName) -> {
                makeModifierTagsIfNeeded(baseTag, additiveTag);
                combineModifiers(baseTag, additiveTag, false);
                return baseTag;
            });
    public static final NBTFunction APPEND_TO_BASE =
            ((baseTag, additiveTag, tagName) -> {
                makeModifierTagsIfNeeded(baseTag, additiveTag);
                combineModifiers(baseTag, additiveTag, true);
                return baseTag;
            });
    public static final NBTFunction REPLACE_WITH_ADDITIVE =
            ((baseTag, additiveTag, tagName) -> {
                makeModifierTagsIfNeeded(baseTag, additiveTag);

                ListTag additiveModifiers = additiveTag.getList(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, Tag.TAG_COMPOUND);

                baseTag.put(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, additiveModifiers);

                return baseTag;
            });

    private static void makeModifierTagsIfNeeded(CompoundTag baseTag, CompoundTag additiveTag) {
        if(!baseTag.contains(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, Tag.TAG_LIST)){
            baseTag.put(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, new ListTag());
        }
        if(!additiveTag.contains(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, Tag.TAG_LIST)){
            additiveTag.put(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, new ListTag());
        }
    }

    private static void combineModifiers(CompoundTag baseTag, CompoundTag additiveTag, boolean addTogether) {
        ListTag combinedModifierList = new ListTag();
        if (baseTag.getTagType(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME) == Tag.TAG_LIST) {
            ListTag baseModifierList = baseTag.getList(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, Tag.TAG_COMPOUND);
            combinedModifierList.addAll(baseModifierList);
        }
        if (additiveTag.getTagType(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME) == Tag.TAG_LIST) {
            ListTag additiveModifierList = additiveTag.getList(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, Tag.TAG_COMPOUND);
            if(addTogether){
                combinedModifierList.addAll(additiveModifierList);
            } else{ // avoiding duplicates of existing modifiers
                for(Tag tag : additiveModifierList){
                    if(!combinedModifierList.contains(tag)){
                        combinedModifierList.add(tag);
                    }
                }
            }
        }
        if(!combinedModifierList.isEmpty()){
            baseTag.put(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, combinedModifierList);
        }
    }
}
