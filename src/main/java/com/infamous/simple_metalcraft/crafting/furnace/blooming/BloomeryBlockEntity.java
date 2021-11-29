package com.infamous.simple_metalcraft.crafting.furnace.blooming;

import com.infamous.simple_metalcraft.crafting.furnace.AdvancedFurnaceBlockEntity;
import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BloomeryBlockEntity extends AdvancedFurnaceBlockEntity {

    public BloomeryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMBlockEntityTypes.BLOOMERY.get(), blockPos, blockState, SMRecipes.Types.BLOOMING, true);
    }

    @Override
    protected Component getDefaultName() {
        return BloomingConstants.BLOOMERY_TITLE;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new BloomeryMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    protected NonNullList<ItemStack> getInputsAsList() {
        return NonNullList.of(
                ItemStack.EMPTY,
                this.items.get(BloomingConstants.SLOT_INPUT_A),
                this.items.get(BloomingConstants.SLOT_INPUT_B)
        );
    }

    @Override
    protected NonNullList<ItemStack> getOutputsAsList() {
        return NonNullList.of(
                ItemStack.EMPTY,
                this.items.get(BloomingConstants.SLOT_RESULT_A),
                this.items.get(BloomingConstants.SLOT_RESULT_B)
        );
    }

    @Override
    protected int[] getSideSlots() {
        return BloomingConstants.CUSTOM_SLOTS_FOR_SIDES;
    }

    @Override
    protected int[] getUpSlots() {
        return BloomingConstants.CUSTOM_SLOTS_FOR_UP;
    }

    @Override
    protected int[] getDownSlots() {
        return BloomingConstants.CUSTOM_SLOTS_FOR_DOWN;
    }

    @Override
    protected int getFuelSlot() {
        return BloomingConstants.CUSTOM_SLOT_FUEL;
    }

    @Override
    protected int getFirstInputSlot() {
        return BloomingConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getNumSlots() {
        return BloomingConstants.NUM_SLOTS;
    }
}
