package com.infamous.simple_metalcraft.crafting;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The multiple ingredient handling logic was shamelessly copied from SilentChaos512's "Silent Mechanisms" project
 */
public abstract class MultipleItemRecipe implements Recipe<Container> {
   protected final Map<Ingredient, Integer> ingredients;
   protected final ItemStack result;
   private final RecipeType<?> type;
   private final RecipeSerializer<?> serializer;
   protected final ResourceLocation id;
   public int levelRequirement;
   protected final int xpCost;
   protected final String group;

    public MultipleItemRecipe(RecipeType<?> recipeType, RecipeSerializer<?> recipeSerializer, ResourceLocation resourceLocation, String groupIn, Map<Ingredient, Integer> ingredientsIn, int levelRequirementIn, int xpCostIn, ItemStack resultIn) {
      this.type = recipeType;
      this.serializer = recipeSerializer;
      this.id = resourceLocation;
      this.group = groupIn;
      this.ingredients = ingredientsIn;
      this.levelRequirement = levelRequirementIn;
      this.xpCost = xpCostIn;
      this.result = resultIn;
   }

   @Override
   public RecipeType<?> getType() {
      return this.type;
   }

   @Override
   public RecipeSerializer<?> getSerializer() {
      return this.serializer;
   }

   @Override
   public ResourceLocation getId() {
      return this.id;
   }

   @Override
   public String getGroup() {
      return this.group;
   }

   public Map<Ingredient, Integer> getIngredientMap() {
      return ImmutableMap.copyOf(this.ingredients);
   }

   @Override
   public NonNullList<Ingredient> getIngredients() {
      NonNullList<Ingredient> list = NonNullList.create();
      list.addAll(this.ingredients.keySet());
      return list;
   }

   public int getLevelRequirement(){
      return this.levelRequirement;
   }

   public int getXpCost() {
      return this.xpCost;
   }

   @Override
   public ItemStack getResultItem() {
      return this.result;
   }

   @Override
   public boolean canCraftInDimensions(int i, int i1) {
      return true;
   }

   @Override
   public ItemStack assemble(Container inventory) {
      return this.result.copy();
   }

   @Override
   public boolean matches(Container inventory, Level world) {
      return InventoryHelper.findMatches(ingredients, inventory);
   }

}