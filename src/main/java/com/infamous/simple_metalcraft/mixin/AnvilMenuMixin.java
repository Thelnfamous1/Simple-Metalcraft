package com.infamous.simple_metalcraft.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {

    @Shadow @Final private DataSlot cost;

    @ModifyConstant(method = "mayPickup", constant = @Constant())
    private int getMinimumRepairCostThreshold(int defaultThreshold){
        if(this.cost.get() == 0){
            return -1;
        } else return defaultThreshold;
    }
}
