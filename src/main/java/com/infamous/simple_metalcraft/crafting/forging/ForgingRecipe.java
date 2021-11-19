package com.infamous.simple_metalcraft.crafting.forging;

import com.infamous.simple_metalcraft.SMModEvents;
import com.infamous.simple_metalcraft.crafting.MetalworkingRecipe;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;

public class ForgingRecipe extends MetalworkingRecipe {
   public ForgingRecipe(ResourceLocation resourceLocation, String group, Map<Ingredient, Integer> ingredients, int levelRequirement, int xpCost, ItemStack resultStack) {
      super(SMModEvents.FORGING, SMRecipes.FORGING.get(), resourceLocation, group, ingredients, levelRequirement, xpCost, resultStack);
   }

   public ItemStack getToastSymbol() {
      return new ItemStack(SMBlocks.FORGING_TABLE.get());
   }
}