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
    private static void recalculateEquipment(EquipmentSlot slot, int armorGrade, CallbackInfoReturnable<Item> cir){
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
        armorGrade = RANDOM.nextInt(2); // 0 or 1
        for(int tryCount = 1; tryCount <= 5; tryCount++){ // can go up to 5 or 6
            if(RANDOM.nextInt() < TIER_UP_CHANCE){ // ~24.35758 chance to go up a tier - 5th try equivalent to vanilla's 3rd try
                ++armorGrade;
            }
        }
        Item chosenEquipment = null;
        switch(slot) {
            case HEAD:
                if (armorGrade == 0) {
                    chosenEquipment = Items.LEATHER_HELMET;
                } else if (armorGrade == 1) {
                    chosenEquipment = SMItems.COPPER_HELMET.get();
                } else if (armorGrade == 2) {
                    chosenEquipment = Items.IRON_HELMET;
                } else if (armorGrade == 3) {
                    chosenEquipment = SMItems.BRONZE_HELMET.get();
                } else if (armorGrade == 4) {
                    chosenEquipment = SMItems.STEEL_HELMET.get();
                } else if (armorGrade == 5) {
                    chosenEquipment = SMItems.MITHRIL_HELMET.get();
                } else if (armorGrade == 6) {
                    chosenEquipment = SMItems.ADAMANTINE_HELMET.get();
                }
            case CHEST:
                if (armorGrade == 0) {
                    chosenEquipment = Items.LEATHER_CHESTPLATE;
                } else if (armorGrade == 1) {
                    chosenEquipment = SMItems.COPPER_CHESTPLATE.get();
                } else if (armorGrade == 2) {
                    chosenEquipment = Items.IRON_CHESTPLATE;
                } else if (armorGrade == 3) {
                    chosenEquipment = SMItems.BRONZE_CHESTPLATE.get();
                } else if (armorGrade == 4) {
                    chosenEquipment = SMItems.STEEL_CHESTPLATE.get();
                } else if (armorGrade == 5) {
                    chosenEquipment = SMItems.MITHRIL_CHESTPLATE.get();
                } else if (armorGrade == 6) {
                    chosenEquipment = SMItems.ADAMANTINE_CHESTPLATE.get();
                }
            case LEGS:
                if (armorGrade == 0) {
                    chosenEquipment = Items.LEATHER_LEGGINGS;
                } else if (armorGrade == 1) {
                    chosenEquipment = SMItems.COPPER_LEGGINGS.get();
                } else if (armorGrade == 2) {
                    chosenEquipment = Items.IRON_LEGGINGS;
                } else if (armorGrade == 3) {
                    chosenEquipment = SMItems.BRONZE_LEGGINGS.get();
                } else if (armorGrade == 4) {
                    chosenEquipment = SMItems.STEEL_LEGGINGS.get();
                } else if (armorGrade == 5) {
                    chosenEquipment = SMItems.MITHRIL_LEGGINGS.get();
                } else if (armorGrade == 6) {
                    chosenEquipment = SMItems.ADAMANTINE_LEGGINGS.get();
                }
            case FEET:
                if (armorGrade == 0) {
                    chosenEquipment = Items.LEATHER_BOOTS;
                } else if (armorGrade == 1) {
                    chosenEquipment = SMItems.COPPER_BOOTS.get();
                } else if (armorGrade == 2) {
                    chosenEquipment = Items.IRON_BOOTS;
                } else if (armorGrade == 3) {
                    chosenEquipment = SMItems.BRONZE_BOOTS.get();
                } else if (armorGrade == 4) {
                    chosenEquipment = SMItems.STEEL_BOOTS.get();
                } else if (armorGrade == 5) {
                    chosenEquipment = SMItems.MITHRIL_BOOTS.get();
                } else if (armorGrade == 6) {
                    chosenEquipment = SMItems.ADAMANTINE_BOOTS.get();
                }
        }
        cir.setReturnValue(chosenEquipment);
    }
    
}
