package com.infamous.simple_metalcraft.crafting.batch.cementation;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.batch.BatchFurnaceBlockEntity;
import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class CementationFurnaceBlockEntity extends BatchFurnaceBlockEntity {

    public static final TranslatableComponent CEMENTATION_FURNACE_TITLE = new TranslatableComponent( "container." + SimpleMetalcraft.MOD_ID + ".cementation_furnace");

    public CementationFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMBlockEntityTypes.CEMENTATION_FURNACE.get(), blockPos, blockState, SMRecipes.Types.CEMENTATION);
    }

    @Override
    protected Component getDefaultName() {
        return CEMENTATION_FURNACE_TITLE;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CementationFurnaceMenu(containerId, inventory, this, this.dataAccess);
    }
}
