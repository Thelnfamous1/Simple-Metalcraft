package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.anvil.ForgingRecipe;
import com.infamous.simple_metalcraft.crafting.anvil.ForgingSerializer;
import com.infamous.simple_metalcraft.crafting.batch.BatchCookingSerializer;
import com.infamous.simple_metalcraft.crafting.batch.BatchCookingRecipe;
import com.infamous.simple_metalcraft.crafting.batch.blooming.BloomingRecipe;
import com.infamous.simple_metalcraft.crafting.batch.cementation.CementationRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMRecipes {

    public static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleMetalcraft.MOD_ID);


    public static final int BLOOMERY_COOKING_TIME = 200;
    public static final String BLOOMING_NAME = "blooming";
    public static RegistryObject<RecipeSerializer<BloomingRecipe>> BLOOMING = RECIPE_SERIALIZERS.register(
            BLOOMING_NAME, () -> new BatchCookingSerializer<>(BloomingRecipe::new, BLOOMERY_COOKING_TIME));

    public static final int CEMENTATION_FURNACE_COOKING_TIME = 200;
    public static final String CEMENTATION_NAME = "cementation";
    public static RegistryObject<RecipeSerializer<CementationRecipe>> CEMENTATION = RECIPE_SERIALIZERS.register(
            CEMENTATION_NAME, () -> new BatchCookingSerializer<>(CementationRecipe::new, CEMENTATION_FURNACE_COOKING_TIME));

    public static final String FORGING_NAME = "forging";
    public static RegistryObject<RecipeSerializer<ForgingRecipe>> FORGING = RECIPE_SERIALIZERS.register(
            FORGING_NAME, ForgingSerializer::new);

    public static class Types{
        public static RecipeType<BloomingRecipe> BLOOMING;
        public static RecipeType<CementationRecipe> CEMENTATION;
        public static RecipeType<ForgingRecipe> FORGING;
    }

}
