package com.infamous.simple_metalcraft.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;

public class MultipleItemSerializer<T extends MultipleItemRecipe> extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
    final Factory<T> factory;

    public MultipleItemSerializer(Factory<T> recipeFactory) {
        this.factory = recipeFactory;
    }

    private static Ingredient deserializeIngredient(JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject json = element.getAsJsonObject();
            if (json.has("value"))
                return Ingredient.fromJson(json.get("value"));
            if (json.has("values"))
                return Ingredient.fromJson(json.get("values"));
        }
        return Ingredient.fromJson(element);
    }

    @Override
    public T fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        String group = GsonHelper.getAsString(jsonObject, "group", "");
        Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
        GsonHelper.getAsJsonArray(jsonObject, "ingredients").forEach(element -> {
            Ingredient ingredient = deserializeIngredient(element);
            int count = GsonHelper.getAsInt(element.getAsJsonObject(), "count", 1);
            ingredients.put(ingredient, count);
        });

        int levelRequirement = GsonHelper.getAsInt(jsonObject, "levelRequirement");
        int xpCost = GsonHelper.getAsInt(jsonObject, "xpCost");

        String result = GsonHelper.getAsString(jsonObject, "result");
        int count = GsonHelper.getAsInt(jsonObject, "count");
        ItemStack resultStack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(result)), count);
        return this.factory.create(resourceLocation, group, ingredients, levelRequirement, xpCost, resultStack);
    }

    @Override
    public T fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf packetBuffer) {
        String group = packetBuffer.readUtf(); // group
        Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
        int ingredientCount = packetBuffer.readByte(); // ingredients size
        for (int i = 0; i < ingredientCount; ++i) { // ingredients
            Ingredient ingredient = Ingredient.fromNetwork(packetBuffer);
            int count = packetBuffer.readByte();
            ingredients.put(ingredient, count);
        }
        int levelRequirement = packetBuffer.readVarInt(); // level
        int xpCost = packetBuffer.readVarInt(); // xp cost
        ItemStack result = packetBuffer.readItem(); // result
        return this.factory.create(resourceLocation, group, ingredients, levelRequirement, xpCost, result);
    }

    @Override
    public void toNetwork(FriendlyByteBuf packetBuffer, T recipe) {
        packetBuffer.writeUtf(recipe.getGroup()); // group

        packetBuffer.writeByte(recipe.getIngredientMap().size()); // ingredients size
        recipe.getIngredientMap().forEach((ingredient, count) -> { // ingredients
            ingredient.toNetwork(packetBuffer);
            packetBuffer.writeByte(count);
        });
        packetBuffer.writeVarInt(recipe.getLevelRequirement()); // level
        packetBuffer.writeVarInt(recipe.getXpCost()); // xp cost
        packetBuffer.writeItem(recipe.getResultItem()); // result
    }

    public interface Factory<T2 extends MultipleItemRecipe> {
        T2 create(ResourceLocation resourceLocation, String group, Map<Ingredient, Integer> ingredient, int levelRequirement, int xpCost, ItemStack resultStack);
    }
}
