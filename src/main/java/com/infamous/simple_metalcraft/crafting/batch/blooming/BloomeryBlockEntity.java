package com.infamous.simple_metalcraft.crafting.batch.blooming;

import com.infamous.simple_metalcraft.SMModEvents;
import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.batch.BatchFurnaceBlockEntity;
import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class BloomeryBlockEntity extends BatchFurnaceBlockEntity {

    public static final TranslatableComponent BLOOMERY_TITLE = new TranslatableComponent( "container." + SimpleMetalcraft.MOD_ID + ".bloomery");

    public BloomeryBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMBlockEntityTypes.BLOOMERY.get(), blockPos, blockState, SMModEvents.BLOOMING);
    }

    @Override
    protected Component getDefaultName() {
        return BLOOMERY_TITLE;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new BloomeryMenu(containerId, inventory, this, this.dataAccess);
    }
}
