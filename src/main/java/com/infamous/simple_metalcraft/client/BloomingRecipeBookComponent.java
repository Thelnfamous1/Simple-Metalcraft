package com.infamous.simple_metalcraft.client;

import java.util.Set;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BloomingRecipeBookComponent extends AbstractFurnaceRecipeBookComponent {
   private static final Component FILTER_NAME = new TranslatableComponent(SimpleMetalcraft.MOD_ID + "." + "gui.recipebook.toggleRecipes.bloomable");

   protected Component getRecipeFilterName() {
      return FILTER_NAME;
   }

   protected Set<Item> getFuelItems() {
      return AbstractFurnaceBlockEntity.getFuel().keySet();
   }
}