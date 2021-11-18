package com.infamous.simple_metalcraft.mixin;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.registry.SMItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    private static final Random RANDOM = new Random();
    private static final double TIER_UP_CHANCE = 0.2435758143;

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "getEquipmentForSlot", cancellable = true)
    private static void recalculateEquipment(EquipmentSlot slot, int oldArmorGrade, CallbackInfoReturnable<Item> cir){
        SimpleMetalcraft.LOGGER.info("Recalculating equipment for for mobs that use vanilla armoring logic!");
        /*
            int i = this.random.nextInt(2);
            if (this.random.nextFloat() < 0.095F) { // 9.5% chance to go up a tier
                ++i;
            }

            if (this.random.nextFloat() < 0.095F) { // 9.5% chance to go up a tier
                ++i;
            }

            if (this.random.nextFloat() < 0.095F) { // 9.5% chance to go up a tier
                ++i;
            }
         */
        int newArmorGrade = RANDOM.nextInt(2); // 0 or 1
        for(int tryCount = 1; tryCount <= 5; tryCount++){ // can go up to 5 or 6
            if(RANDOM.nextFloat() < TIER_UP_CHANCE){ // ~24.35758 chance to go up a tier - 5th try equivalent to vanilla's 3rd try
                ++newArmorGrade;
            }
        }
        Item chosenEquipment = null;
        switch(slot) {
            case HEAD:
                if (newArmorGrade == 0) {
                    chosenEquipment = Items.LEATHER_HELMET;
                } else if (newArmorGrade == 1) {
                    chosenEquipment = Items.GOLDEN_HELMET;
                } else if (newArmorGrade == 2) {
                    chosenEquipment = SMItems.COPPER_HELMET.get();
                } else if (newArmorGrade == 3) {
                    chosenEquipment = Items.IRON_HELMET;
                } else if (newArmorGrade == 4) {
                    chosenEquipment = SMItems.BRONZE_HELMET.get();
                } else if (newArmorGrade == 5) {
                    chosenEquipment = SMItems.STEEL_HELMET.get();
                } else if (newArmorGrade == 6) {
                    chosenEquipment = Items.DIAMOND_HELMET;
                }
            case CHEST:
                if (newArmorGrade == 0) {
                    chosenEquipment = Items.LEATHER_CHESTPLATE;
                } else if (newArmorGrade == 1) {
                    chosenEquipment = SMItems.COPPER_CHESTPLATE.get();
                } else if (newArmorGrade == 2) {
                    chosenEquipment = SMItems.COPPER_CHESTPLATE.get();
                } else if (newArmorGrade == 3) {
                    chosenEquipment = Items.IRON_CHESTPLATE;
                } else if (newArmorGrade == 4) {
                    chosenEquipment = SMItems.BRONZE_CHESTPLATE.get();
                } else if (newArmorGrade == 5) {
                    chosenEquipment = SMItems.STEEL_CHESTPLATE.get();
                } else if (newArmorGrade == 6) {
                    chosenEquipment = Items.DIAMOND_CHESTPLATE;
                }
            case LEGS:
                if (newArmorGrade == 0) {
                    chosenEquipment = Items.LEATHER_LEGGINGS;
                } else if (newArmorGrade == 1) {
                    chosenEquipment = SMItems.COPPER_LEGGINGS.get();
                } else if (newArmorGrade == 2) {
                    chosenEquipment = SMItems.COPPER_LEGGINGS.get();
                } else if (newArmorGrade == 3) {
                    chosenEquipment = Items.IRON_LEGGINGS;
                } else if (newArmorGrade == 4) {
                    chosenEquipment = SMItems.BRONZE_LEGGINGS.get();
                } else if (newArmorGrade == 5) {
                    chosenEquipment = SMItems.STEEL_LEGGINGS.get();
                } else if (newArmorGrade == 6) {
                    chosenEquipment = Items.DIAMOND_LEGGINGS;
                }
            case FEET:
                if (newArmorGrade == 0) {
                    chosenEquipment = Items.LEATHER_BOOTS;
                } else if (newArmorGrade == 1) {
                    chosenEquipment = SMItems.COPPER_BOOTS.get();
                } else if (newArmorGrade == 2) {
                    chosenEquipment = SMItems.COPPER_BOOTS.get();
                } else if (newArmorGrade == 3) {
                    chosenEquipment = Items.IRON_BOOTS;
                } else if (newArmorGrade == 4) {
                    chosenEquipment = SMItems.BRONZE_BOOTS.get();
                } else if (newArmorGrade == 5) {
                    chosenEquipment = SMItems.STEEL_BOOTS.get();
                } else if (newArmorGrade == 6) {
                    chosenEquipment = Items.DIAMOND_BOOTS;
                }
        }
        cir.setReturnValue(chosenEquipment);
    }
    
}
