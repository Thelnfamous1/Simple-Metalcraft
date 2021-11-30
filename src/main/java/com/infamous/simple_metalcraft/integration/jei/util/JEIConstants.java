package com.infamous.simple_metalcraft.integration.jei.util;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import mezz.jei.api.constants.ModIds;
import net.minecraft.resources.ResourceLocation;

/**
 * Mostly copied directly from JEI source code, with modifications for this implementation
 * @author mezz and Thelnfamous1
 */
public class JEIConstants {

    private JEIConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TEXTURE_GUI_PATH = "textures/gui/";
    public static final String TEXTURE_GUI_VANILLA = TEXTURE_GUI_PATH + "gui_vanilla.png";
    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(ModIds.JEI_ID, TEXTURE_GUI_VANILLA);

    public static final String TEXTURE_GUI_CUSTOM = TEXTURE_GUI_PATH + "jei.png";
    public static final ResourceLocation RECIPE_GUI_CUSTOM = new ResourceLocation(SimpleMetalcraft.MOD_ID, TEXTURE_GUI_CUSTOM);

}
