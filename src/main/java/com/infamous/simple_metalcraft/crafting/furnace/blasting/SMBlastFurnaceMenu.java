package com.infamous.simple_metalcraft.crafting.furnace.blasting;

import com.infamous.simple_metalcraft.crafting.furnace.AdvancedFurnaceMenu;
import com.infamous.simple_metalcraft.registry.SMMenuTypes;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

public class SMBlastFurnaceMenu extends AdvancedFurnaceMenu {

    public SMBlastFurnaceMenu(int menuId, Inventory inventory) {
        super(SMMenuTypes.BLAST_FURNACE.get(), SMRecipes.Types.BLASTING, menuId, inventory,
                SMBlastingConstants.NUM_SLOTS,
                SMBlastingConstants.NUM_INPUT_SLOTS,
                SMBlastingConstants.NUM_OUTPUT_SLOTS
        );
    }

    public SMBlastFurnaceMenu(int menuId, Inventory inventory, Container container, ContainerData containerData) {
        super(SMMenuTypes.BLAST_FURNACE.get(), SMRecipes.Types.BLASTING, menuId, inventory, container, containerData,
                SMBlastingConstants.NUM_SLOTS,
                SMBlastingConstants.NUM_INPUT_SLOTS,
                SMBlastingConstants.NUM_OUTPUT_SLOTS
        );
    }

    @Override
    protected int getFirstIngredientSlot() {
        return SMBlastingConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getInvSlotStart() {
        return SMBlastingConstants.SLOT_RESULT_B + 1;
    }

    @Override
    protected int getFuelSlot() {
        return SMBlastingConstants.CUSTOM_SLOT_FUEL;
    }

    @Override
    protected int getFirstResultSlot() {
        return SMBlastingConstants.SLOT_RESULT_A;
    }

    @Override
    protected SimpleContainer buildInputContainer() {
        return new SimpleContainer(
                this.slots.get(SMBlastingConstants.SLOT_INPUT_A).getItem().copy(),
                this.slots.get(SMBlastingConstants.SLOT_INPUT_B).getItem().copy(),
                this.slots.get(SMBlastingConstants.SLOT_INPUT_C).getItem().copy()
        );
    }
}
