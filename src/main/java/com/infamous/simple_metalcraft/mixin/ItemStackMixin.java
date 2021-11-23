package com.infamous.simple_metalcraft.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    private static final String MAX_DAMAGE_BONUS_TAG_NAME = "MaxDamageBonus";

    @Shadow @Nullable public abstract CompoundTag getTag();

    @Inject(at = @At("RETURN"), method = "getMaxDamage", cancellable = true)
    private void checkMaxDamageBonus(CallbackInfoReturnable<Integer> cir){
        if(this.getTag() != null && this.getTag().contains(MAX_DAMAGE_BONUS_TAG_NAME)){
            cir.setReturnValue(cir.getReturnValue() + this.getTag().getInt(MAX_DAMAGE_BONUS_TAG_NAME));
        }
    }

}
