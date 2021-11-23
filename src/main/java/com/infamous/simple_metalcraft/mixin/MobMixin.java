package com.infamous.simple_metalcraft.mixin;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.util.ArmoringHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    private static final Random RANDOM = new Random();
    private static final double TIER_UP_CHANCE = 0.19D;

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "getEquipmentForSlot", cancellable = true)
    private static void recalculateEquipment(EquipmentSlot slot, int defaultArmorIndex, CallbackInfoReturnable<Item> cir){
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
        int armorIndex = RANDOM.nextInt(2); // 0 or 1
        for(int tryCount = 1; tryCount <= 4; tryCount++){ // can go up to 4 or 5
            if(RANDOM.nextFloat() < TIER_UP_CHANCE){
                ++armorIndex;
            }
        }
        Item chosenEquipment = switch (slot) {
            case HEAD -> ArmoringHelper.SORTED_HELMETS[armorIndex];
            case CHEST -> ArmoringHelper.SORTED_CHESTPLATES[armorIndex];
            case LEGS -> ArmoringHelper.SORTED_LEGGINGS[armorIndex];
            case FEET -> ArmoringHelper.SORTED_BOOTS[armorIndex];
            case MAINHAND -> ArmoringHelper.getRandomWeapon(armorIndex);
            default -> null;
        };

        if(chosenEquipment != null){
            SimpleMetalcraft.LOGGER.info("Put {} in slot {}", chosenEquipment, slot);
        }

        cir.setReturnValue(chosenEquipment);
    }
    
}
