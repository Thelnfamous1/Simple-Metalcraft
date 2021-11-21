package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.MetalworkingSerializer;
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

    public static final String CASTING_NAME = "casting";
    public static RegistryObject<RecipeSerializer<?>> CASTING = RECIPE_SERIALIZERS.register(
            CASTING_NAME, () -> new MetalworkingSerializer<>(CastingRecipe::new));

    public static final String FORGING_NAME = "forging";
    public static RegistryObject<RecipeSerializer<?>> FORGING = RECIPE_SERIALIZERS.register(
            FORGING_NAME, () -> new MetalworkingSerializer<>(ForgingRecipe::new));

    public static final int BLOOMERY_COOKING_TIME = 200;
    public static final String BLOOMING_NAME = "blooming";
    public static RegistryObject<RecipeSerializer<?>> BLOOMING = RECIPE_SERIALIZERS.register(
            BLOOMING_NAME, () -> new BloomingSerializer<>(BloomingRecipe::new, BLOOMERY_COOKING_TIME));

    public static RegistryObject<RecipeSerializer<?>> UPGRADING = RECIPE_SERIALIZERS.register(
            "upgrading", NBTUpgradingSerializer::new);

}
