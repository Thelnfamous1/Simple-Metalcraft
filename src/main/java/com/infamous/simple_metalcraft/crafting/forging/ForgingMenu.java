package com.infamous.simple_metalcraft.crafting.forging;

import com.infamous.simple_metalcraft.SMModEvents;
import com.infamous.simple_metalcraft.crafting.MetalworkingMenu;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.SMMenuTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

public class ForgingMenu extends MetalworkingMenu {
    public ForgingMenu(int p_40297_, Inventory inventory) {
        super(SMMenuTypes.FORGING_TABLE.get(), p_40297_, inventory);
    }
    public ForgingMenu(int p_40297_, Inventory inventory, ContainerLevelAccess access) {
        super(SMMenuTypes.FORGING_TABLE.get(), p_40297_, inventory, access);
    }

    @Override
    protected SoundEvent getTakeSound() {
        return SoundEvents.ANVIL_USE;
    }

    @Override
    protected Block getBlock() {
        return SMBlocks.FORGING_TABLE.get();
    }

    @Override
    protected RecipeType<ForgingRecipe> getRecipeType() {
        return SMModEvents.FORGING;
    }

    @Override
    public MenuType<?> getType() {
        return SMMenuTypes.FORGING_TABLE.get();
    }
}
