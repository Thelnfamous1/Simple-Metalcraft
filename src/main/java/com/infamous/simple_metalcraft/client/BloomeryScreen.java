package com.infamous.simple_metalcraft.client;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.blooming.BloomeryMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BloomeryScreen extends SMFurnaceScreen<BloomeryMenu> {
   private static final ResourceLocation TEXTURE = new ResourceLocation(SimpleMetalcraft.MOD_ID, "textures/gui/container/bloomery.png");

   public BloomeryScreen(BloomeryMenu menu, Inventory inventory, Component component) {
      super(menu, inventory, component, TEXTURE);
   }
}