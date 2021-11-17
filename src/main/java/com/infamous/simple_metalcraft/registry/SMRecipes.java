package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.MultipleItemSerializer;
import com.infamous.simple_metalcraft.crafting.blooming.BloomingRecipe;
import com.infamous.simple_metalcraft.crafting.blooming.BloomingSerializer;
import com.infamous.simple_metalcraft.crafting.casting.CastingRecipe;
import com.infamous.simple_metalcraft.crafting.forging.ForgingRecipe;
import com.infamous.simple_metalcraft.crafting.upgrading.NBTUpgradingSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMRecipes {

    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleMetalcraft.MOD_ID);

    public static RegistryObject<RecipeSerializer<?>> CASTING = RECIPE_SERIALIZERS.register(
            "casting", () -> new MultipleItemSerializer<>(CastingRecipe::new));

    public static RegistryObject<RecipeSerializer<?>> FORGING = RECIPE_SERIALIZERS.register(
            "forging", () -> new MultipleItemSerializer<>(ForgingRecipe::new));

    public static RegistryObject<RecipeSerializer<?>> BLOOMING = RECIPE_SERIALIZERS.register(
            "blooming", () -> new BloomingSerializer<>(BloomingRecipe::new, 200));

    public static RegistryObject<RecipeSerializer<?>> UPGRADING = RECIPE_SERIALIZERS.register(
            "upgrading", NBTUpgradingSerializer::new);

}
