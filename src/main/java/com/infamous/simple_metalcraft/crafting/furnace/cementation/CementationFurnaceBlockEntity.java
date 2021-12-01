package com.infamous.simple_metalcraft.crafting.furnace.cementation;

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

public class CementationFurnaceBlockEntity extends AdvancedFurnaceBlockEntity {

    public CementationFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMBlockEntityTypes.CEMENTATION_FURNACE.get(), blockPos, blockState, SMRecipes.Types.CEMENTATION, false);
    }

    @Override
    protected Component getDefaultName() {
        return CementationConstants.CEMENTATION_FURNACE_TITLE;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CementationFurnaceMenu(containerId, inventory, this, this.dataAccess);
    }

    @Override
    protected NonNullList<ItemStack> getInputsAsList() {
        return NonNullList.of(
                ItemStack.EMPTY,
                this.items.get(CementationConstants.SLOT_INPUT_A),
                this.items.get(CementationConstants.SLOT_INPUT_B)
        );
    }

    @Override
    protected NonNullList<ItemStack> getOutputsAsList() {
        return NonNullList.of(
                ItemStack.EMPTY,
                this.items.get(CementationConstants.SLOT_RESULT_A)
        );
    }

    @Override
    protected int[] getSideSlots() {
        return CementationConstants.CUSTOM_SLOTS_FOR_SIDES;
    }

    @Override
    protected int[] getUpSlots() {
        return CementationConstants.CUSTOM_SLOTS_FOR_UP;
    }

    @Override
    protected int[] getDownSlots() {
        return CementationConstants.CUSTOM_SLOTS_FOR_DOWN;
    }

    @Override
    protected int getFuelSlot() {
        return CementationConstants.CUSTOM_SLOT_FUEL;
    }

    @Override
    protected int getFirstInputSlot() {
        return CementationConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getNumSlots() {
        return CementationConstants.NUM_SLOTS;
    }

    @Override
    protected int getNumInputSlots() {
        return CementationConstants.NUM_INPUT_SLOTS;
    }
}
