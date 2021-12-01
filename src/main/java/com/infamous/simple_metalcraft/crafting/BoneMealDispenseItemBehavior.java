package com.infamous.simple_metalcraft.crafting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class BoneMealDispenseItemBehavior extends OptionalDispenseItemBehavior {

    protected ItemStack execute(BlockSource blockSource, ItemStack stack) {
        this.setSuccess(true);
        Level level = blockSource.getLevel();
        BlockPos blockpos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
        if (!BoneMealItem.growCrop(stack, level, blockpos) && !BoneMealItem.growWaterPlant(stack, level, blockpos, (Direction)null)) {
            this.setSuccess(false);
        } else if (!level.isClientSide) {
            level.levelEvent(1505, blockpos, 0);
        }

        return stack;
    }
}
