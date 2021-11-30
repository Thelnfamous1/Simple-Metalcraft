package com.infamous.simple_metalcraft.integration.jei;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.furnace.cementation.CementationConstants;
import com.infamous.simple_metalcraft.crafting.furnace.cementation.CementationRecipe;
import com.infamous.simple_metalcraft.integration.jei.util.JEIConstants;
import com.infamous.simple_metalcraft.integration.jei.util.SMCookingCategory;
import com.infamous.simple_metalcraft.registry.SMBlocks;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.resources.ResourceLocation;

public class CementationRecipeCategory extends SMCookingCategory<CementationRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(SimpleMetalcraft.MOD_ID, SMRecipes.CEMENTATION_NAME);

    public CementationRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, SMBlocks.CEMENTATION_FURNACE.get(), CementationConstants.CEMENTATION_FURNACE_TITLE, SMRecipes.CEMENTATION_FURNACE_COOKING_TIME);
    }

    @Override
    protected IDrawableStatic createBackground(IGuiHelper guiHelper) {
        return guiHelper.createDrawable(JEIConstants.RECIPE_GUI_CUSTOM, 0, 108, 91, 54);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<CementationRecipe> getRecipeClass() {
        return CementationRecipe.class;
    }

    @Override
    protected int getInputSlotStart() {
        return CementationConstants.SLOT_INPUT_A;
    }

    @Override
    protected int getOutputSlotStart() {
        return CementationConstants.SLOT_RESULT_A;
    }

    @Override
    protected int getNumInputs() {
        return CementationConstants.NUM_INPUT_SLOTS;
    }

    @Override
    protected int getNumOutputs() {
        return CementationConstants.NUM_OUTPUT_SLOTS;
    }
}
