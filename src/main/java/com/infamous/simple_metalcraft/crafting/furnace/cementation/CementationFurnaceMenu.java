package com.infamous.simple_metalcraft.crafting.furnace.cementation;

import com.infamous.simple_metalcraft.crafting.furnace.AdvancedFurnaceMenu;
import com.infamous.simple_metalcraft.registry.SMMenuTypes;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

public class CementationFurnaceMenu extends AdvancedFurnaceMenu {

    public CementationFurnaceMenu(int containerId, Inventory inventory) {
        super(SMMenuTypes.CEMENTATION_FURNACE.get(), SMRecipes.Types.CEMENTATION, containerId, inventory,
                CementationConstants.NUM_SLOTS,
                CementationConstants.NUM_INPUT_SLOTS,
                CementationConstants.NUM_OUTPUT_SLOTS
        );
    }

    public CementationFurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
        super(SMMenuTypes.CEMENTATION_FURNACE.get(), SMRecipes.Types.CEMENTATION, containerId, inventory, container, containerData,
                CementationConstants.NUM_SLOTS,
                CementationConstants.NUM_INPUT_SLOTS,
                CementationConstants.NUM_OUTPUT_SLOTS
        );
    }

    @Override
    protected int getFirstIngredientSlot() {
        return CementationConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getInvSlotStart() {
        return CementationConstants.SLOT_RESULT_A + 1;
    }

    @Override
    protected int getFuelSlot() {
        return CementationConstants.CUSTOM_SLOT_FUEL;
    }

    @Override
    protected int getFirstResultSlot() {
        return CementationConstants.SLOT_RESULT_A;
    }

    @Override
    protected SimpleContainer buildInputContainer() {
        return new SimpleContainer(
                this.slots.get(CementationConstants.SLOT_INPUT_A).getItem().copy(),
                this.slots.get(CementationConstants.SLOT_INPUT_B).getItem().copy()
        );
    }
}
