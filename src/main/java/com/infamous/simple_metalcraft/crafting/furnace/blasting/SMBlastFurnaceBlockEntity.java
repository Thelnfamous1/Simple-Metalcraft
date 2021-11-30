package com.infamous.simple_metalcraft.crafting.furnace.blasting;

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

public class SMBlastFurnaceBlockEntity extends AdvancedFurnaceBlockEntity {

    public SMBlastFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMBlockEntityTypes.BLAST_FURNACE.get(), blockPos, blockState, SMRecipes.Types.BLASTING, false);
    }

    @Override
    protected Component getDefaultName() {
        return SMBlastingConstants.BLAST_FURNACE_COMPONENT;
    }

    @Override
    protected int getBurnDuration(ItemStack stack) {
        return super.getBurnDuration(stack) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new SMBlastFurnaceMenu(id, inventory, this, this.dataAccess);
    }

    @Override
    protected NonNullList<ItemStack> getInputsAsList(){
        return NonNullList.of(
                ItemStack.EMPTY, // default value
                this.items.get(SMBlastingConstants.SLOT_INPUT_A),
                this.items.get(SMBlastingConstants.SLOT_INPUT_B),
                this.items.get(SMBlastingConstants.SLOT_INPUT_C)
        );
    }

    @Override
    protected NonNullList<ItemStack> getOutputsAsList(){
        return NonNullList.of(
                ItemStack.EMPTY, // default value
                this.items.get(SMBlastingConstants.SLOT_RESULT_A),
                this.items.get(SMBlastingConstants.SLOT_RESULT_B)
        );
    }

    @Override
    protected int[] getSideSlots() {
        return SMBlastingConstants.CUSTOM_SLOTS_FOR_SIDES;
    }

    @Override
    protected int[] getUpSlots() {
        return SMBlastingConstants.CUSTOM_SLOTS_FOR_UP;
    }

    @Override
    protected int[] getDownSlots() {
        return SMBlastingConstants.CUSTOM_SLOTS_FOR_DOWN;
    }

    @Override
    protected int getFuelSlot() {
        return SMBlastingConstants.CUSTOM_SLOT_FUEL;
    }

    @Override
    protected int getFirstInputSlot() {
        return SMBlastingConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getNumSlots() {
        return SMBlastingConstants.NUM_SLOTS;
    }

    @Override
    protected int getNumInputSlots() {
        return SMBlastingConstants.NUM_INPUT_SLOTS;
    }

}
