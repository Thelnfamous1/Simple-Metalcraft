package com.infamous.simple_metalcraft.crafting.blooming;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.LinkedHashMap;
import java.util.Map;

public class BloomingSerializer<T extends BloomingRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
   public static final String GROUP_NAME = "group";
   public static final String RESULTS_NAME = "results";
   public static final String RANGE_NAME = "range";
   public static final String MIN_NAME = "min";
   public static final String MAX_NAME = "max";
   public static final String WEIGHT_NAME = "weight";
   public static final String EXPERIENCE_NAME = "experience";
   public static final String COOKINGTIME_NAME = "cookingtime";
   public static final String RESULT_NAME = "result";
   private final int defaultCookingTime;
   private final BloomingFactory<T> factory;

   public BloomingSerializer(BloomingFactory<T> factory, int defaultCookingTime) {
      this.defaultCookingTime = defaultCookingTime;
      this.factory = factory;
   }

   @Override
   public T fromJson(ResourceLocation location, JsonObject jsonObject) {
      String group = GsonHelper.getAsString(jsonObject, GROUP_NAME, "");
      JsonElement ingredientElement = (JsonElement)(GsonHelper.isArrayNode(jsonObject, "ingredient") ? GsonHelper.getAsJsonArray(jsonObject, "ingredient") : GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
      Ingredient ingredient = Ingredient.fromJson(ingredientElement);

      /*ItemStack result;
      //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
      if (!jsonObject.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
      if (jsonObject.get("result").isJsonObject()) result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
      else {
      String resultLocation = GsonHelper.getAsString(jsonObject, "result");
      ResourceLocation resultId = new ResourceLocation(resultLocation);
      result = new ItemStack(ForgeRegistries.ITEMS.getValue(resultId));
      }
       */

      Map<ItemStack, Integer> weightedResults = new LinkedHashMap<>();
      Map<ItemStack, UniformInt> ranges = new LinkedHashMap<>();
      GsonHelper.getAsJsonArray(jsonObject, RESULTS_NAME).forEach(element -> {
         JsonObject elementAsObject = element.getAsJsonObject();
         ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(elementAsObject, RESULT_NAME));
         if(elementAsObject.has(RANGE_NAME)){
            JsonObject rangeObject = elementAsObject.getAsJsonObject(RANGE_NAME);
            int min = GsonHelper.getAsInt(rangeObject, MIN_NAME, result.getCount());
            int max = GsonHelper.getAsInt(rangeObject, MAX_NAME, result.getCount());
            if(max < min){
               throw new JsonSyntaxException("Max must be at least min when defining a range for a blooming recipe, min: " + min + ", max: " + max + ", recipe: " + location);
            } else{
               ranges.put(result, UniformInt.of(min, max));
            }
         }
         int weight = GsonHelper.getAsInt(elementAsObject, WEIGHT_NAME, 1);
         weightedResults.put(result, weight);
      });

      float experience = GsonHelper.getAsFloat(jsonObject, EXPERIENCE_NAME, 0.0F);
      int cookingTime = GsonHelper.getAsInt(jsonObject, COOKINGTIME_NAME, this.defaultCookingTime);
      return this.factory.create(location, group, ingredient, weightedResults, ranges, experience, cookingTime);
   }

   @Override
   public T fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf) {
      String group = byteBuf.readUtf(); // group
      Ingredient ingredient = Ingredient.fromNetwork(byteBuf); // ingredient

      //ItemStack result = byteBuf.readItem();
      Map<ItemStack, Integer> weightedResults = this.readWeightedResults(byteBuf);
      Map<ItemStack, UniformInt> ranges = this.readRanges(byteBuf);

      float experience = byteBuf.readFloat(); // xp reward
      int cookingTime = byteBuf.readVarInt(); // cook time
      return this.factory.create(location, group, ingredient, weightedResults, ranges, experience, cookingTime);
   }

   private Map<ItemStack, UniformInt> readRanges(FriendlyByteBuf byteBuf) {
      Map<ItemStack, UniformInt> ranges = new LinkedHashMap<>();
      int rangesCount = byteBuf.readByte(); // ranges length
      for (int i = 0; i < rangesCount; ++i) { // ranges
         ItemStack result = byteBuf.readItem();
         int min = byteBuf.readByte();
         int max = byteBuf.readByte();
         ranges.put(result, UniformInt.of(min, max));
      }
      return ranges;
   }

   private Map<ItemStack, Integer> readWeightedResults(FriendlyByteBuf byteBuf) {
      Map<ItemStack, Integer> weightedResults = new LinkedHashMap<>();
      int weightedResultsCount = byteBuf.readByte(); // weightedResults length
      for (int i = 0; i < weightedResultsCount; ++i) { // weightedResults
         ItemStack result = byteBuf.readItem();
         int weight = byteBuf.readByte();
         weightedResults.put(result, weight);
      }
      return weightedResults;
   }

   @Override
   public void toNetwork(FriendlyByteBuf byteBuf, T recipe) {
      byteBuf.writeUtf(recipe.getGroup()); // group
      recipe.getIngredient().toNetwork(byteBuf); // ingredient

      //byteBuf.writeItem(recipe.getResultItem());
      this.writeWeightedResults(byteBuf, recipe);
      this.writeRanges(byteBuf, recipe);

      byteBuf.writeFloat(recipe.getExperience()); // xp reward
      byteBuf.writeVarInt(recipe.getCookingTime()); // cook time
   }

   private void writeRanges(FriendlyByteBuf byteBuf, T recipe) {
      byteBuf.writeByte(recipe.getRanges().size()); // ranges length
      recipe.getRanges().forEach((stack, uniformInt) -> { // ranges
         byteBuf.writeItem(stack);
         byteBuf.writeByte(uniformInt.getMinValue());
         byteBuf.writeByte(uniformInt.getMaxValue());
      });
   }

   private void writeWeightedResults(FriendlyByteBuf byteBuf, T recipe) {
      byteBuf.writeByte(recipe.getWeightedResults().size()); // weighted results length
      recipe.getWeightedResults().forEach((stack, weight) -> { // weighted results
         byteBuf.writeItem(stack);
         byteBuf.writeByte(weight);
      });
   }

   public interface BloomingFactory<T extends BloomingRecipe> {
      T create(ResourceLocation id, String group, Ingredient ingredient, Map<ItemStack, Integer> weightedResults, Map<ItemStack, UniformInt> ranges, float experience, int cookingTime);
   }
}
