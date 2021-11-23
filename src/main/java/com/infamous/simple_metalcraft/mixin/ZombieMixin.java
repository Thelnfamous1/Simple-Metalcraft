package com.infamous.simple_metalcraft.mixin;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieMixin {

    @Inject(method = "populateDefaultEquipmentSlots",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/entity/monster/Monster;populateDefaultEquipmentSlots(Lnet/minecraft/world/DifficultyInstance;)V",
                    ordinal = 0),
            cancellable = true)
    private void recalculateWeapon(DifficultyInstance difficultyInstance, CallbackInfo ci){
        ci.cancel();
    }
}
