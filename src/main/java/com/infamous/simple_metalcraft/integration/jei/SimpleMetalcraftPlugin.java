package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.integration.jei.util.JEIRegistryHelper;
import com.infamous.simple_metalcraft.registry.SMItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@JeiPlugin
public class SimpleMetalcraftPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(SimpleMetalcraft.MOD_ID, SimpleMetalcraft.MOD_ID);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        JEIRegistryHelper.RecipeCache recipeCache = JEIRegistryHelper.cacheRecipes();
        registration.addRecipes(recipeCache.getBloomingRecipes(), BloomingRecipeCategory.UID);
        registration.addRecipes(recipeCache.getBlastingRecipes(), SMBlastingRecipeCategory.UID);
        registration.addRecipes(recipeCache.getCementationRecipes(), CementationRecipeCategory.UID);
        registration.addRecipes(recipeCache.getForgingRecipes(), ForgingRecipeCategory.UID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        JEIRegistryHelper.registerCategory(registration, BloomingRecipeCategory.class, guiHelper);
        JEIRegistryHelper.registerCategory(registration, SMBlastingRecipeCategory.class, guiHelper);
        JEIRegistryHelper.registerCategory(registration, CementationRecipeCategory.class, guiHelper);
        JEIRegistryHelper.registerCategory(registration, ForgingRecipeCategory.class, guiHelper);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        JEIRegistryHelper.registerCookingCatalyst(registration, new ItemStack(SMItems.BLOOMERY.get()), BloomingRecipeCategory.UID);
        JEIRegistryHelper.registerCookingCatalyst(registration, new ItemStack(SMItems.BLAST_FURNACE.get()), SMBlastingRecipeCategory.UID);
        JEIRegistryHelper.registerCookingCatalyst(registration, new ItemStack(SMItems.CEMENTATION_FURNACE.get()), CementationRecipeCategory.UID);
        JEIRegistryHelper.registerCatalyst(registration, new ItemStack(Items.ANVIL), ForgingRecipeCategory.UID);
    }

}
