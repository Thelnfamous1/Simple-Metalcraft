package com.infamous.simple_metalcraft.crafting.blasting;

import com.infamous.simple_metalcraft.registry.SMMenuTypes;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

public class SMBlastFurnaceMenu extends AdvancedFurnaceMenu {

    public SMBlastFurnaceMenu(int menuId, Inventory inventory) {
        super(SMMenuTypes.BLAST_FURNACE.get(), SMRecipes.Types.BLASTING, menuId, inventory);
    }

    public SMBlastFurnaceMenu(int menuId, Inventory inventory, Container container, ContainerData containerData) {
        super(SMMenuTypes.BLAST_FURNACE.get(), SMRecipes.Types.BLASTING, menuId, inventory, container, containerData);
    }
}
