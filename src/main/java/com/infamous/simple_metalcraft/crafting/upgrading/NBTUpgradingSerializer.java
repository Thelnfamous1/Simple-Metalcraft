package com.infamous.simple_metalcraft.crafting.upgrading;

import com.google.gson.JsonObject;
import com.infamous.simple_metalcraft.crafting.nbt.NBTOperatorRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class NBTUpgradingSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<NBTUpgradeRecipe> {

    @Override
    public NBTUpgradeRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
        // These first 4 lines should line up with what UpgradeRecipe.Serializer#fromJson does
        Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "base"));
        Ingredient addition = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "addition"));
        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
        NBTUpgradeRecipe recipe = new NBTUpgradeRecipe(id, base, addition, result);
        NBTOperatorRecipe.fromJson(jsonObject, recipe);
        return recipe;
    }

    @Override
    public NBTUpgradeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf byteBuf) {
        // These first 4 lines should line up with what UpgradeRecipe.Serializer#fromNetwork does
        Ingredient base = Ingredient.fromNetwork(byteBuf);
        Ingredient addition = Ingredient.fromNetwork(byteBuf);
        ItemStack result = byteBuf.readItem();
        NBTUpgradeRecipe recipe = new NBTUpgradeRecipe(id, base, addition, result);
        NBTOperatorRecipe.fromNetwork(byteBuf, recipe);
        return recipe;
    }

    @Override
    public void toNetwork(FriendlyByteBuf byteBuf, NBTUpgradeRecipe recipe) {
        // These first 3 lines should line up with what UpgradeRecipe.Serializer#toNetwork does
        recipe.getBase().toNetwork(byteBuf);
        recipe.getAddition().toNetwork(byteBuf);
        byteBuf.writeItem(recipe.getResultItem());
        NBTOperatorRecipe.toNetwork(byteBuf, recipe);
    }
}
