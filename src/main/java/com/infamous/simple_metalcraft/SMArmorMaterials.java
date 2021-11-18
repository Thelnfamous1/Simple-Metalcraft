package com.infamous.simple_metalcraft;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class SMArmorMaterials implements ArmorMaterial {

    /*
        GOLD("gold", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F, () -> Ingredient.of(Items.GOLD_INGOT))
     */

    public static final SMArmorMaterials COPPER =
            new SMArmorMaterials("copper", 10, new int[]{1, 4, 5, 2},
                    20, SoundEvents.ARMOR_EQUIP_GOLD, 0.0F, 0.0F,
                    () -> Ingredient.of(SMTags.INGOTS_COPPER));

    public static final SMArmorMaterials BRONZE =
            new SMArmorMaterials("bronze", 13, new int[]{2, 5, 6, 2},
                    18, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F,
                    () -> Ingredient.of(SMTags.INGOTS_BRONZE));

    /*
        IRON("iron", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> Ingredient.of(Items.IRON_INGOT))
     */


    public static final SMArmorMaterials STEEL =
            new SMArmorMaterials("steel", 25, new int[]{2, 6, 7, 3},
                    8, SoundEvents.ARMOR_EQUIP_DIAMOND, 1.0F, 0.0F,
                    () -> Ingredient.of(SMTags.INGOTS_STEEL));

    /*
        DIAMOND("diamond", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Ingredient.of(Items.DIAMOND))
     */
    /*
        NETHERITE("netherite", 37, new int[]{3, 6, 8, 3}, 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F, () -> Ingredient.of(Items.NETHERITE_INGOT);
     */

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    public SMArmorMaterials(String materialName, int durabilityMultiplier, int[] slotProtections, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = buildName(materialName);
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot slot) {
        return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot) {
        return this.slotProtections[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    private static String buildName(String materialName) {
        return SimpleMetalcraft.MOD_ID + ":" + materialName;
    }
}
