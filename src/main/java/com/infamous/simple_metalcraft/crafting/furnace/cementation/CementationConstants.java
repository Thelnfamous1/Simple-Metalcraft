package com.infamous.simple_metalcraft.crafting.furnace.cementation;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import net.minecraft.network.chat.TranslatableComponent;

public class CementationConstants {

    private CementationConstants(){
        throw new IllegalStateException("Utility class");
    }

    public static final TranslatableComponent CEMENTATION_FURNACE_TITLE = new TranslatableComponent( "container." + SimpleMetalcraft.MOD_ID + ".cementation_furnace");
    public static final int SLOT_INPUT_A = 0;
    public static final int SLOT_INPUT_B = 1;
    public static final int NUM_INPUT_SLOTS = SLOT_INPUT_B + 1;

    public static final int CUSTOM_SLOT_FUEL = 2;

    public static final int SLOT_RESULT_A = 3;
    public static final int NUM_OUTPUT_SLOTS = SLOT_RESULT_A - CUSTOM_SLOT_FUEL;

    public static final int NUM_SLOTS = SLOT_RESULT_A + 1;

    protected static final int[] CUSTOM_SLOTS_FOR_UP = new int[]{SLOT_INPUT_A, SLOT_INPUT_B};
    protected static final int[] CUSTOM_SLOTS_FOR_SIDES = new int[]{CUSTOM_SLOT_FUEL};
    protected static final int[] CUSTOM_SLOTS_FOR_DOWN = new int[]{SLOT_RESULT_A, SLOT_RESULT_A, CUSTOM_SLOT_FUEL};
}
