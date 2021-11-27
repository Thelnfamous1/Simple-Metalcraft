package com.infamous.simple_metalcraft.crafting.batch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class BatchCookingSerializer<T extends AbstractCookingRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
   public static final String GROUP = "group";
   public static final String RESULT = "result";
   public static final String EXPERIENCE = "experience";
   public static final String COOKINGTIME = "cookingtime";
   public static final String INGREDIENT = "ingredient";
   private final int defaultCookingTime;
   private final BatchCookingMaker<T> factory;

   public BatchCookingSerializer(BatchCookingMaker<T> maker, int defaultCookingTime) {
      this.defaultCookingTime = defaultCookingTime;
      this.factory = maker;
   }

   public T fromJson(ResourceLocation location, JsonObject jsonObject) {
      String group = GsonHelper.getAsString(jsonObject, GROUP, "");
      Ingredient ingredient = this.getIngredient(jsonObject);
      ItemStack result = this.getResult(jsonObject);
      float experience = GsonHelper.getAsFloat(jsonObject, EXPERIENCE, 0.0F);
      int cookingtime = GsonHelper.getAsInt(jsonObject, COOKINGTIME, this.defaultCookingTime);
      return this.factory.create(location, group, ingredient, result, experience, cookingtime);
   }

   private Ingredient getIngredient(JsonObject jsonObject) {
      JsonElement ingredientElement = GsonHelper.isArrayNode(jsonObject, INGREDIENT) ? GsonHelper.getAsJsonArray(jsonObject, INGREDIENT) : GsonHelper.getAsJsonObject(jsonObject, INGREDIENT);
      return Ingredient.fromJson(ingredientElement);
   }

   private ItemStack getResult(JsonObject jsonObject) {
      //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
      if (!jsonObject.has(RESULT)) throw new JsonSyntaxException("Missing " + RESULT + ", expected to find a string or object");
      ItemStack itemstack;
      if (jsonObject.get(RESULT).isJsonObject()) itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, RESULT));
      else {
      String result = GsonHelper.getAsString(jsonObject, RESULT);
      ResourceLocation resourcelocation = new ResourceLocation(result);
         Item item = ForgeRegistries.ITEMS.getValue(resourcelocation);
         if(item == null) throw new IllegalStateException("Item: " + result + " does not exist");
         itemstack = new ItemStack(item);
      }
      return itemstack;
   }

   public T fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf) {
      String group = byteBuf.readUtf();
      Ingredient ingredient = Ingredient.fromNetwork(byteBuf);
      ItemStack result = byteBuf.readItem();
      float experience = byteBuf.readFloat();
      int cookingtime = byteBuf.readVarInt();
      return this.factory.create(location, group, ingredient, result, experience, cookingtime);
   }

   public void toNetwork(FriendlyByteBuf byteBuf, T recipe) {
      byteBuf.writeUtf(recipe.getGroup());
      this.getIngredient(recipe).toNetwork(byteBuf);
      byteBuf.writeItem(recipe.getResultItem());
      byteBuf.writeFloat(recipe.getExperience());
      byteBuf.writeVarInt(recipe.getCookingTime());
   }

   private Ingredient getIngredient(T recipe) {
      return recipe.getIngredients().get(0);
   }

   public interface BatchCookingMaker<T extends AbstractCookingRecipe> {
      T create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookingtime);
   }
}