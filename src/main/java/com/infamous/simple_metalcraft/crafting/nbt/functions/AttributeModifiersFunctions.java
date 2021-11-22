package com.infamous.simple_metalcraft.crafting.nbt.functions;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.infamous.simple_metalcraft.crafting.nbt.NBTFunction;
import com.infamous.simple_metalcraft.crafting.nbt.NBTOperator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

// TODO: Finish implementing this
public class AttributeModifiersFunctions {

    public static final String SLOT_TAG_NAME = "Slot";
    public static final String ATTRIBUTE_NAME_TAG_NAME = "AttributeName";

    private AttributeModifiersFunctions(){
        throw new IllegalStateException("Utility class");
    }

    public static final NBTFunction MERGE_TO_BASE =
            ((baseTag, additiveTag) -> {



                return baseTag;
            });
    public static final NBTFunction APPEND_TO_BASE =
            ((baseTag, additiveTag) -> {
                return baseTag;
            });
    public static final NBTFunction REPLACE_WITH_ADDITIVE =
            ((baseTag, additiveTag) -> {



                return baseTag;
            });

    private static Multimap<Attribute, AttributeModifier> getAttributesForSlot(CompoundTag tag, EquipmentSlot slot){
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        if (tag.contains(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, 9)) {
            ListTag modifiersTag = tag.getList(NBTOperator.ATTRIBUTE_MODIFIERS_TAG_NAME, 10);

            for(int i = 0; i < modifiersTag.size(); ++i) {
                CompoundTag modifierTag = modifiersTag.getCompound(i);
                if (!modifierTag.contains(SLOT_TAG_NAME, 8) || modifierTag.getString(SLOT_TAG_NAME).equals(slot.getName())) {
                    Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(modifierTag.getString(ATTRIBUTE_NAME_TAG_NAME)));
                    if (attribute != null) {
                        AttributeModifier modifier = AttributeModifier.load(modifierTag);
                        if (modifier != null && modifier.getId().getLeastSignificantBits() != 0L && modifier.getId().getMostSignificantBits() != 0L) {
                            multimap.put(attribute, modifier);
                        }
                    }
                }
            }
        }
        return multimap;
    }
}
