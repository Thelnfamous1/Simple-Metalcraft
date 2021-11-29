package com.infamous.simple_metalcraft.client;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.furnace.cementation.CementationFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CementationFurnaceScreen extends SMFurnaceScreen<CementationFurnaceMenu> {
   private static final ResourceLocation TEXTURE = new ResourceLocation(SimpleMetalcraft.MOD_ID, "textures/gui/container/cementation_furnace.png");

   public CementationFurnaceScreen(CementationFurnaceMenu menu, Inventory inventory, Component component) {
      super(menu, inventory, component, TEXTURE);
   }
}