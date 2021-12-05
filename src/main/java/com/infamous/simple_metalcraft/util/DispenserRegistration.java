package com.infamous.simple_metalcraft.util;

import com.infamous.simple_metalcraft.crafting.BoneMealDispenseItemBehavior;
import com.infamous.simple_metalcraft.registry.SMItems;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.world.level.block.DispenserBlock;

public class DispenserRegistration {

    private DispenserRegistration(){
        throw new IllegalStateException("Utility class");
    }

    public static void registerDispenserBehavior() {
        DispenserBlock.registerBehavior(SMItems.STONE_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.COPPER_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.BRONZE_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.IRON_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.METEORIC_IRON_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.STEEL_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.DIAMOND_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.NETHERITE_SHEARS.get(), new ShearsDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.IRON_SLAG.get(), new BoneMealDispenseItemBehavior());
        DispenserBlock.registerBehavior(SMItems.DROSS.get(), new BoneMealDispenseItemBehavior());
    }
}
