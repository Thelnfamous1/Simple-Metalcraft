package com.infamous.simple_metalcraft.crafting.furnace.blooming;

import com.infamous.simple_metalcraft.crafting.furnace.AdvancedFurnaceMenu;
import com.infamous.simple_metalcraft.registry.SMMenuTypes;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

public class BloomeryMenu extends AdvancedFurnaceMenu {

    public BloomeryMenu(int containerId, Inventory inventory) {
        super(SMMenuTypes.BLOOMERY.get(), SMRecipes.Types.BLOOMING, containerId, inventory,
                BloomingConstants.NUM_SLOTS,
                BloomingConstants.NUM_INPUT_SLOTS,
                BloomingConstants.NUM_OUTPUT_SLOTS
        );
    }

    public BloomeryMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
        super(SMMenuTypes.BLOOMERY.get(), SMRecipes.Types.BLOOMING, containerId, inventory, container, containerData,
                BloomingConstants.NUM_SLOTS,
                BloomingConstants.NUM_INPUT_SLOTS,
                BloomingConstants.NUM_OUTPUT_SLOTS
        );
    }

    @Override
    protected int getFirstIngredientSlot() {
        return BloomingConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getInvSlotStart() {
        return BloomingConstants.SLOT_RESULT_B + 1;
    }

    @Override
    protected int getFuelSlot() {
        return BloomingConstants.CUSTOM_SLOT_FUEL;
    }

    @Override
    protected int getFirstResultSlot() {
        return BloomingConstants.SLOT_RESULT_A;
    }

    @Override
    protected SimpleContainer buildInputContainer() {
        return new SimpleContainer(
                this.slots.get(BloomingConstants.SLOT_INPUT_A).getItem().copy(),
                this.slots.get(BloomingConstants.SLOT_INPUT_B).getItem().copy()
        );
    }
}
