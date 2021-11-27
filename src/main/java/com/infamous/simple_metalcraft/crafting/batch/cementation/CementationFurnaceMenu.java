package com.infamous.simple_metalcraft.crafting.batch.cementation;

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

public class CementationFurnaceMenu extends AbstractFurnaceMenu {

    public CementationFurnaceMenu(int containerId, Inventory inventory) {
        super(SMMenuTypes.CEMENTATION_FURNACE.get(), SMModEvents.CEMENTATION, RecipeBookType.FURNACE, containerId, inventory);
    }

    public CementationFurnaceMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
        super(SMMenuTypes.CEMENTATION_FURNACE.get(), SMModEvents.CEMENTATION, RecipeBookType.FURNACE, containerId, inventory, container, containerData);
    }

    @Override
    public List<RecipeBookCategories> getRecipeBookCategories() {
        return ImmutableList.of();
    }
}
