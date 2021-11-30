package com.infamous.simple_metalcraft.integration.jei.util;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.anvil.ForgingRecipe;
import com.infamous.simple_metalcraft.crafting.furnace.blasting.SMBlastingRecipe;
import com.infamous.simple_metalcraft.crafting.furnace.blooming.BloomingRecipe;
import com.infamous.simple_metalcraft.crafting.furnace.cementation.CementationRecipe;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JEIRegistryHelper {

    private JEIRegistryHelper(){
        throw new IllegalStateException("Utility class");
    }

    public static void registerCategory(IRecipeCategoryRegistration registration, Class<? extends IRecipeCategory<?>> clazz, IGuiHelper guiHelper){
        try {
            Constructor<? extends IRecipeCategory<?>> constructor = clazz.getConstructor(IGuiHelper.class);
            IRecipeCategory<?> newCategory = constructor.newInstance(guiHelper);
            registration.addRecipeCategories(newCategory);
        } catch (NoSuchMethodException e) {
            logReflectedConstructionError(clazz, "find");
        } catch (InvocationTargetException e) {
            logReflectedConstructionError(clazz, "invoke");
        } catch (InstantiationException e) {
            logReflectedConstructionError(clazz, "instantiate");
        } catch (IllegalAccessException e) {
            logReflectedConstructionError(clazz, "access");
        }
    }

    private static void logReflectedConstructionError(Class<? extends IRecipeCategory<?>> clazz, String action) {
        SimpleMetalcraft.LOGGER.error("Reflection failure for JEI plugin! Could not " + action + " constructor for {} that takes in {}", clazz, IGuiHelper.class);
    }

    public static RecipeCache cacheRecipes() {

        RecipeCache recipeCache = new RecipeCache();
        ClientLevel world = Minecraft.getInstance().level;
        RecipeManager recipeManager = world.getRecipeManager();

        recipeCache.bloomingRecipes.addAll(recipeManager.getAllRecipesFor(SMRecipes.Types.BLOOMING));
        recipeCache.blastingRecipes.addAll(recipeManager.getAllRecipesFor(SMRecipes.Types.BLASTING));
        recipeCache.cementationRecipes.addAll(recipeManager.getAllRecipesFor(SMRecipes.Types.CEMENTATION));
        recipeCache.forgingRecipes.addAll(recipeManager.getAllRecipesFor(SMRecipes.Types.FORGING));

        return recipeCache;
    }

    public static void registerCookingCatalyst(IRecipeCatalystRegistration registration, ItemStack furnaceCatalystStack, ResourceLocation uid) {
        registration.addRecipeCatalyst(furnaceCatalystStack, uid, VanillaRecipeCategoryUid.FUEL);
    }

    public static void registerCatalyst(IRecipeCatalystRegistration registration, ItemStack recipeCatalyst, ResourceLocation uid) {
        registration.addRecipeCatalyst(recipeCatalyst, uid);
    }

    public static class RecipeCache {
        private final List<BloomingRecipe> bloomingRecipes = new ArrayList<>();
        private final List<SMBlastingRecipe> blastingRecipes = new ArrayList<>();
        private final List<CementationRecipe> cementationRecipes = new ArrayList<>();
        private final List<ForgingRecipe> forgingRecipes = new ArrayList<>();

        public List<BloomingRecipe> getBloomingRecipes() {
            return Collections.unmodifiableList(this.bloomingRecipes);
        }
        public List<SMBlastingRecipe> getBlastingRecipes() {
            return Collections.unmodifiableList(this.blastingRecipes);
        }
        public List<CementationRecipe> getCementationRecipes() {
            return Collections.unmodifiableList(this.cementationRecipes);
        }
        public List<ForgingRecipe> getForgingRecipes() {
            return Collections.unmodifiableList(this.forgingRecipes);
        }
    }
}
