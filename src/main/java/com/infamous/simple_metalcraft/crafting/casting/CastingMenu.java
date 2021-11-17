package com.infamous.simple_metalcraft.crafting.casting;

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

public class CastingMenu extends MetalworkingMenu {
    public CastingMenu(int p_40297_, Inventory inventory) {
        super(SMMenuTypes.CASTING_TABLE.get(), p_40297_, inventory);
    }
    public CastingMenu(int p_40297_, Inventory inventory, ContainerLevelAccess access) {
        super(SMMenuTypes.CASTING_TABLE.get(), p_40297_, inventory, access);
    }

    @Override
    protected SoundEvent getTakeSound() {
        return SoundEvents.BUCKET_FILL_LAVA;
    }

    @Override
    protected Block getBlock() {
        return SMBlocks.CASTING_TABLE.get();
    }

    @Override
    protected RecipeType<CastingRecipe> getRecipeType() {
        return SMModEvents.CASTING;
    }

    @Override
    public MenuType<?> getType() {
        return SMMenuTypes.CASTING_TABLE.get();
    }
}
