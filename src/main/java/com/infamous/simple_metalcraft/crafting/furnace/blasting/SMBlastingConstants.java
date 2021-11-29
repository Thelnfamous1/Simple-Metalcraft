package com.infamous.simple_metalcraft.crafting.furnace.blasting;

import net.minecraft.network.chat.TranslatableComponent;

public class SMBlastingConstants {

    private SMBlastingConstants(){
        throw new IllegalStateException("Utility class");
    }

    public static final TranslatableComponent BLAST_FURNACE_COMPONENT = new TranslatableComponent("container.blast_furnace");

    public static final int SLOT_INPUT_A = 0;
    public static final int SLOT_INPUT_B = 1;
    public static final int SLOT_INPUT_C = 2;
    public static final int NUM_INPUT_SLOTS = SLOT_INPUT_C + 1;

    public static final int CUSTOM_SLOT_FUEL = 3;

    public static final int SLOT_RESULT_A = 4;
    public static final int SLOT_RESULT_B = 5;
    public static final int NUM_OUTPUT_SLOTS = SLOT_RESULT_B - CUSTOM_SLOT_FUEL;

    public static final int NUM_SLOTS = SLOT_RESULT_B + 1;

    protected static final int[] CUSTOM_SLOTS_FOR_UP = new int[]{SLOT_INPUT_A, SLOT_INPUT_B, SLOT_INPUT_C};
    protected static final int[] CUSTOM_SLOTS_FOR_SIDES = new int[]{CUSTOM_SLOT_FUEL};
    protected static final int[] CUSTOM_SLOTS_FOR_DOWN = new int[]{SLOT_RESULT_A, SLOT_RESULT_B, CUSTOM_SLOT_FUEL};

}
