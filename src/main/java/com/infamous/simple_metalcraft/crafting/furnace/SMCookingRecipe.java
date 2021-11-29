package com.infamous.simple_metalcraft.crafting.furnace;

import com.infamous.simple_metalcraft.crafting.furnace.AdvancedCookingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public abstract class SMCookingRecipe extends AbstractCookingRecipe implements AdvancedCookingRecipe {

   private final Map<Ingredient, Integer> ingredients;
   private final List<ItemStack> results;

   public SMCookingRecipe(RecipeType<?> recipeType, ResourceLocation id, String group, Map<Ingredient, Integer> ingredients, List<ItemStack> results, float experience, int cookingTime) {
      super(recipeType, id, group, ingredients.entrySet().iterator().next().getKey(), results.get(0), experience, cookingTime);
      this.ingredients = ingredients;
      this.results = results;
   }

   @Override
   public boolean matches(Container container, Level level) {
      return this.findMatches(container);
   }

   @Override
   public Map<Ingredient, Integer> getIngredientsMap(){
      return this.ingredients;
    }

   @Override
   public List<ItemStack> getResults() {
      return this.results;
   }

}