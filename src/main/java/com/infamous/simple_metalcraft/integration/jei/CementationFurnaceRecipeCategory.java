package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.batch.BatchCookingRecipe;
import com.infamous.simple_metalcraft.crafting.batch.blooming.BloomeryBlockEntity;
import com.infamous.simple_metalcraft.crafting.batch.cementation.CementationFurnaceBlockEntity;
import com.infamous.simple_metalcraft.crafting.batch.cementation.CementationRecipe;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;

public class CementationFurnaceRecipeCategory extends AbstractCookingCategory<CementationRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(SimpleMetalcraft.MOD_ID, SMRecipes.CEMENTATION_NAME);

    public CementationFurnaceRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, SMBlocks.CEMENTATION_FURNACE.get(), CementationFurnaceBlockEntity.CEMENTATION_FURNACE_TITLE, SMRecipes.CEMENTATION_FURNACE_COOKING_TIME);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<CementationRecipe> getRecipeClass() {
        return CementationRecipe.class;
    }
}
