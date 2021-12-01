package com.infamous.simple_metalcraft.client;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.furnace.blasting.SMBlastFurnaceMenu;
import com.infamous.simple_metalcraft.crafting.furnace.smelter.SmelterMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.BlastingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SmelterScreen extends AbstractFurnaceScreen<SmelterMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SimpleMetalcraft.MOD_ID, "textures/gui/container/smelter.png");

    public SmelterScreen(SmelterMenu furnaceMenu, Inventory inventory, Component component) {
        super(furnaceMenu, new BlastingRecipeBookComponent(), inventory, component, TEXTURE);
    }
}
