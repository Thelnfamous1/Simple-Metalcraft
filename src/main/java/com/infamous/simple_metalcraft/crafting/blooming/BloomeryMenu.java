package com.infamous.simple_metalcraft.crafting.blooming;

import com.google.common.collect.ImmutableList;
import com.infamous.simple_metalcraft.SMModEvents;
import com.infamous.simple_metalcraft.registry.SMMenuTypes;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

import java.util.List;

public class BloomeryMenu extends AbstractFurnaceMenu {

    public BloomeryMenu(int containerId, Inventory inventory) {
        super(SMMenuTypes.BLOOMERY.get(), SMModEvents.BLOOMING, RecipeBookType.FURNACE, containerId, inventory);
    }

    public BloomeryMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
        super(SMMenuTypes.BLOOMERY.get(), SMModEvents.BLOOMING, RecipeBookType.FURNACE, containerId, inventory, container, containerData);
    }

    @Override
    public List<RecipeBookCategories> getRecipeBookCategories() {
        return ImmutableList.of();
    }
}
