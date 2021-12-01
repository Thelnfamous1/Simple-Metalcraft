package com.infamous.simple_metalcraft.crafting.furnace.smelter;

import com.infamous.simple_metalcraft.registry.SMMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;

public class SmelterMenu extends AbstractFurnaceMenu {

    public SmelterMenu(int menuId, Inventory inventory) {
        super(SMMenuTypes.SMELTER.get(), RecipeType.BLASTING, RecipeBookType.BLAST_FURNACE, menuId, inventory);
    }

    public SmelterMenu(int menuId, Inventory inventory, Container container, ContainerData containerData) {
        super(SMMenuTypes.SMELTER.get(), RecipeType.BLASTING, RecipeBookType.BLAST_FURNACE, menuId, inventory, container, containerData);
    }
}
