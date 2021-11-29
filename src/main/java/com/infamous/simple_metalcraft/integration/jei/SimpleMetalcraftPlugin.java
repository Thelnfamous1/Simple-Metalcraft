package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.anvil.ForgingRecipe;
import com.infamous.simple_metalcraft.crafting.furnace.blasting.SMBlastingRecipe;
import com.infamous.simple_metalcraft.crafting.furnace.blooming.BloomingRecipe;
import com.infamous.simple_metalcraft.crafting.furnace.cementation.CementationRecipe;
import com.infamous.simple_metalcraft.registry.SMItems;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        registration.addRecipes(recipeCache.bloomingRecipes, BloomingRecipeCategory.UID);
        registration.addRecipes(recipeCache.blastingRecipes, SMBlastingRecipeCategory.UID);
        registration.addRecipes(recipeCache.cementationRecipes, CementationRecipeCategory.UID);
        registration.addRecipes(recipeCache.forgingRecipes, ForgingRecipeCategory.UID);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        this.registerCategory(registration, BloomingRecipeCategory.class, guiHelper);
        this.registerCategory(registration, SMBlastingRecipeCategory.class, guiHelper);
        this.registerCategory(registration, CementationRecipeCategory.class, guiHelper);
        this.registerCategory(registration, ForgingRecipeCategory.class, guiHelper);
    }

    private void registerCategory(IRecipeCategoryRegistration registration, Class<? extends IRecipeCategory<?>> clazz,  IGuiHelper guiHelper){
        try {
            Constructor<? extends IRecipeCategory<?>> constructor = clazz.getConstructor(IGuiHelper.class);
            IRecipeCategory<?> newCategory = constructor.newInstance(guiHelper);
            registration.addRecipeCategories(newCategory);
        } catch (NoSuchMethodException e) {
            this.logReflectedConstructionError(clazz, "find");
        } catch (InvocationTargetException e) {
            this.logReflectedConstructionError(clazz, "invoke");
        } catch (InstantiationException e) {
            this.logReflectedConstructionError(clazz, "instantiate");
        } catch (IllegalAccessException e) {
            this.logReflectedConstructionError(clazz, "access");
        }
    }

    private void logReflectedConstructionError(Class<? extends IRecipeCategory<?>> clazz, String action) {
        SimpleMetalcraft.LOGGER.error("Reflection failure for JEI plugin! Could not " + action + " constructor for {} that takes in {}", clazz, IGuiHelper.class);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        this.addCookingRecipe(registration, SMItems.BLOOMERY, BloomingRecipeCategory.UID);
        this.addCookingRecipe(registration, SMItems.BLAST_FURNACE, SMBlastingRecipeCategory.UID);
        this.addCookingRecipe(registration, SMItems.CEMENTATION_FURNACE, CementationRecipeCategory.UID);

        ItemStack anvilCatalyst = new ItemStack(Items.ANVIL);
        registration.addRecipeCatalyst(anvilCatalyst, ForgingRecipeCategory.UID);
    }

    private void addCookingRecipe(IRecipeCatalystRegistration registration, RegistryObject<Item> furnaceCatalyst, ResourceLocation uid) {
        ItemStack furnaceCatalystStack = new ItemStack(furnaceCatalyst.get());
        registration.addRecipeCatalyst(furnaceCatalystStack, uid, VanillaRecipeCategoryUid.FUEL);
    }

    public static class RecipeCache {
        private final List<BloomingRecipe> bloomingRecipes = new ArrayList<>();
        private final List<SMBlastingRecipe> blastingRecipes = new ArrayList<>();
        private final List<CementationRecipe> cementationRecipes = new ArrayList<>();
        private final List<ForgingRecipe> forgingRecipes = new ArrayList<>();

        public List<BloomingRecipe> getBloomingRecipes() {
            return this.bloomingRecipes;
        }
        public List<SMBlastingRecipe> getBlastingRecipes() {
            return this.blastingRecipes;
        }
        public List<CementationRecipe> getCementationRecipes() {
            return this.cementationRecipes;
        }
        public List<ForgingRecipe> getForgingRecipes() {
            return this.forgingRecipes;
        }
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

}
