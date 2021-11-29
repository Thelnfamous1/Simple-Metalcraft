package com.infamous.simple_metalcraft.client;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.blasting.SMBlastFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SMBlastFurnaceScreen extends SMFurnaceScreen<SMBlastFurnaceMenu>{
    private static final ResourceLocation TEXTURE = new ResourceLocation(SimpleMetalcraft.MOD_ID, "textures/gui/container/blast_furnace.png");

    public SMBlastFurnaceScreen(SMBlastFurnaceMenu furnaceMenu, Inventory inventory, Component component) {
        super(furnaceMenu, inventory, component, TEXTURE);
    }
}
