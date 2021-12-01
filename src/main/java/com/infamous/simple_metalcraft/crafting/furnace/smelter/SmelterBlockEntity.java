package com.infamous.simple_metalcraft.crafting.furnace.smelter;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SmelterBlockEntity extends AbstractFurnaceBlockEntity {
    public SmelterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMBlockEntityTypes.SMELTER.get(), blockPos, blockState, RecipeType.BLASTING);
    }

    protected Component getDefaultName() {
        return new TranslatableComponent("container." + SimpleMetalcraft.MOD_ID + ".smelter");
    }

    protected int getBurnDuration(ItemStack stack) {
        return super.getBurnDuration(stack) / 2;
    }

    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new SmelterMenu(containerId, inventory, this, this.dataAccess);
    }
}
