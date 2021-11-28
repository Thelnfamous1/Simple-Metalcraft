package com.infamous.simple_metalcraft.crafting.anvil;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class ForgingSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ForgingRecipe> {

    public static final String INGREDIENT = "ingredient";
    public static final String CATALYST = "catalyst";
    public static final String RESULT = "result";
    public static final String EXPERIENCE_COST = "experienceCost";

    public ForgingRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
        Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, INGREDIENT));
        Ingredient catalyst = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, CATALYST));
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, RESULT));
        int experienceCost = Math.max(GsonHelper.getAsInt(jsonObject, EXPERIENCE_COST, 0), 0);
        return new ForgingRecipe(id, ingredient, catalyst, result, experienceCost);
    }

    public ForgingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuf) {
        Ingredient ingredient = Ingredient.fromNetwork(byteBuf);
        Ingredient catalyst = Ingredient.fromNetwork(byteBuf);
        ItemStack result = byteBuf.readItem();
        int experienceCost = byteBuf.readVarInt();
        return new ForgingRecipe(id, ingredient, catalyst, result, experienceCost);
    }

    public void toNetwork(FriendlyByteBuf byteBuf, ForgingRecipe recipe) {
        recipe.ingredient.toNetwork(byteBuf);
        recipe.catalyst.toNetwork(byteBuf);
        byteBuf.writeItem(recipe.result);
        byteBuf.writeVarInt(recipe.experienceCost);
    }
}
