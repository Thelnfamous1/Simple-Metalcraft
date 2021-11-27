package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SMModEvents;
import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.batch.BatchCookingRecipe;
import com.infamous.simple_metalcraft.crafting.batch.blooming.BloomingRecipe;
import com.infamous.simple_metalcraft.crafting.batch.cementation.CementationRecipe;
import com.infamous.simple_metalcraft.registry.SMItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class SimpleMetalcraftPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SimpleMetalcraft.MOD_ID, SimpleMetalcraft.MOD_ID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeCache recipeCache = cacheRecipes();
        registration.addRecipes(recipeCache.bloomingRecipes, BloomeryRecipeCategory.UID);
        registration.addRecipes(recipeCache.cementationRecipes, CementationFurnaceRecipeCategory.UID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        BloomeryRecipeCategory bloomeryRecipeCategory = new BloomeryRecipeCategory(guiHelper);
        registration.addRecipeCategories(bloomeryRecipeCategory);

        CementationFurnaceRecipeCategory cementationFurnaceRecipeCategory = new CementationFurnaceRecipeCategory(guiHelper);
        registration.addRecipeCategories(cementationFurnaceRecipeCategory);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ItemStack bloomeryCatalyst = new ItemStack(SMItems.BLOOMERY.get());
        ItemStack cementationFurnaceCatalyst = new ItemStack(SMItems.CEMENTATION_FURNACE.get());
        registration.addRecipeCatalyst(bloomeryCatalyst, BloomeryRecipeCategory.UID, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeCatalyst(cementationFurnaceCatalyst, CementationFurnaceRecipeCategory.UID, VanillaRecipeCategoryUid.FUEL);
    }

    public static class RecipeCache {
        private final List<BloomingRecipe> bloomingRecipes = new ArrayList<>();
        private final List<CementationRecipe> cementationRecipes = new ArrayList<>();

        public List<BloomingRecipe> getBloomingRecipes() {
            return bloomingRecipes;
        }
        public List<CementationRecipe> getCementationRecipes() {
            return cementationRecipes;
        }
    }

    public static RecipeCache cacheRecipes() {

        RecipeCache recipeCache = new RecipeCache();
        ClientLevel world = Minecraft.getInstance().level;
        RecipeManager recipeManager = world.getRecipeManager();

        recipeCache.bloomingRecipes.addAll(recipeManager.getAllRecipesFor(SMModEvents.BLOOMING));
        recipeCache.cementationRecipes.addAll(recipeManager.getAllRecipesFor(SMModEvents.CEMENTATION));

        return recipeCache;
    }

}
