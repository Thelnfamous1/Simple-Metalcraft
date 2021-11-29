package com.infamous.simple_metalcraft.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SMCookingSerializer<T extends SMCookingRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
    public static final String GROUP = "group";
    public static final String RESULTS = "results";
    public static final String EXPERIENCE = "experience";
    public static final String COOKINGTIME = "cookingtime";
    public static final String INGREDIENTS = "ingredients";
    public static final String VALUES = "values";
    public static final String COUNT = "count";
    private final int defaultCookingTime;
    private final SMCookingMaker<T> factory;

    public SMCookingSerializer(SMCookingMaker<T> maker, int defaultCookingTime) {
        this.defaultCookingTime = defaultCookingTime;
        this.factory = maker;
    }

    public T fromJson(ResourceLocation location, JsonObject jsonObject) {
        String group = GsonHelper.getAsString(jsonObject, GROUP, "");
        Map<Ingredient, Integer> ingredients = this.getIngredients(jsonObject);
        List<ItemStack> results = this.getResults(jsonObject);
        float experience = GsonHelper.getAsFloat(jsonObject, EXPERIENCE, 0.0F);
        int cookingtime = GsonHelper.getAsInt(jsonObject, COOKINGTIME, this.defaultCookingTime);
        return this.factory.create(location, group, ingredients, results, experience, cookingtime);
    }

    private Map<Ingredient, Integer> getIngredients(JsonObject jsonObject) {
        Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
        JsonArray ingredientArray = GsonHelper.getAsJsonArray(jsonObject, INGREDIENTS);
        ingredientArray.forEach(jsonElement -> {
            Ingredient ingredient = this.getIngredient(jsonElement);
            int count = GsonHelper.getAsInt(jsonElement.getAsJsonObject(), COUNT, 1);
            ingredients.put(ingredient, count);
        });

        return ingredients;
    }

    private Ingredient getIngredient(JsonElement ingredientElement) {
        if (ingredientElement.isJsonObject()) {
            JsonObject ingredientObject = ingredientElement.getAsJsonObject();
            if(ingredientObject.has(VALUES)){
                JsonElement valuesElement = ingredientObject.get(VALUES);
                return Ingredient.fromJson(valuesElement);
            }
        }
        // default
        return Ingredient.fromJson(ingredientElement);
    }

    private List<ItemStack> getResults(JsonObject jsonObject) {
        List<ItemStack> results = new ArrayList<>();
        JsonArray resultsArray = GsonHelper.getAsJsonArray(jsonObject, RESULTS);
        resultsArray.forEach(jsonElement -> results.add(ShapedRecipe.itemStackFromJson(jsonElement.getAsJsonObject())));
        return results;
    }

    public T fromNetwork(ResourceLocation location, FriendlyByteBuf byteBuf) {
        String group = byteBuf.readUtf();
        Map<Ingredient, Integer> ingredients = this.readIngredientsFromNetwork(byteBuf);
        List<ItemStack> results = this.readResultsFromNetwork(byteBuf);
        float experience = byteBuf.readFloat();
        int cookingtime = byteBuf.readVarInt();
        return this.factory.create(location, group, ingredients, results, experience, cookingtime);
    }

    public void toNetwork(FriendlyByteBuf byteBuf, T recipe) {
        byteBuf.writeUtf(recipe.getGroup());
        this.sendIngredientsToNetwork(recipe.getIngredientsMap(), byteBuf);
        this.sendResultsToNetwork(recipe.getResults(), byteBuf);
        byteBuf.writeFloat(recipe.getExperience());
        byteBuf.writeVarInt(recipe.getCookingTime());
    }

    private void sendIngredientsToNetwork(Map<Ingredient, Integer> ingredients, FriendlyByteBuf byteBuf){
        int mapSize = ingredients.size();
        byteBuf.writeVarInt(mapSize);
        for(Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()){
            entry.getKey().toNetwork(byteBuf);
            byteBuf.writeVarInt(entry.getValue());
        }
    }

    private void sendResultsToNetwork(List<ItemStack> results, FriendlyByteBuf byteBuf){
        int listSize = results.size();
        byteBuf.writeVarInt(listSize);
        for(ItemStack stack : results){
            byteBuf.writeItem(stack);
        }
    }

    private Map<Ingredient, Integer> readIngredientsFromNetwork(FriendlyByteBuf byteBuf){
        Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
        int mapSize = byteBuf.readVarInt();
        for(int i = 0; i < mapSize; i++){
            ingredients.put(Ingredient.fromNetwork(byteBuf), byteBuf.readVarInt());
        }
        return ingredients;
    }

    private List<ItemStack> readResultsFromNetwork(FriendlyByteBuf byteBuf){
        List<ItemStack> results = new ArrayList<>();
        int listSize = byteBuf.readVarInt();
        for(int i = 0; i < listSize; i++){
            results.add(byteBuf.readItem());
        }
        return results;
    }


    public interface SMCookingMaker<T extends SMCookingRecipe> {
        T create(ResourceLocation id, String group, Map<Ingredient, Integer> ingredients, List<ItemStack> results, float experience, int cookingtime);
    }
}
