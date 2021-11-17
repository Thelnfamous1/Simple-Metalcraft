package com.infamous.simple_metalcraft;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class SMTiers {

    private SMTiers(){}

    /*
        STONE(1, 131, 4.0F, 1.0F, 5, () -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS))
     */

    // We want Copper to be slightly stronger than Stone
    public static final ForgeTier COPPER =
            new ForgeTier(1, 188, 5.0F, 1.5F, 18,
                    SMTags.NEEDS_COPPER_TOOL, () -> Ingredient.of(SMTags.INGOTS_COPPER));

    /*
        IRON(2, 250, 6.0F, 2.0F, 14, () -> Ingredient.of(Items.IRON_INGOT))
     */

    // We want Bronze to be slightly stronger than Iron
    public static final ForgeTier BRONZE =
            new ForgeTier(2, 375, 6.5F, 2.0F, 16,
                    SMTags.NEEDS_BRONZE_TOOL, () -> Ingredient.of(SMTags.INGOTS_BRONZE));

    // We want Steel to be between Iron and Diamond
    public static final ForgeTier STEEL =
            new ForgeTier(3, 500, 7.0F, 2.5F, 8,
                    SMTags.NEEDS_STEEL_TOOL, () -> Ingredient.of(SMTags.INGOTS_STEEL));


    // We want Mithril to be slightly weaker than Diamond
    public static final ForgeTier MITHRIL =
            new ForgeTier(3, 1000, 7.5F, 2.5F, 14,
                    SMTags.NEEDS_MITHRIL_TOOL, () -> Ingredient.of(SMTags.INGOTS_MITHRIL));


    /*
        DIAMOND(3, 1561, 8.0F, 3.0F, 10, () -> Ingredient.of(Items.DIAMOND))
     */

    // We want Adamantine to be slightly weaker than Netherite
    public static final ForgeTier ADAMANTINE =
            new ForgeTier(4, 1750, 8.5F, 3.5F, 12,
                    SMTags.NEEDS_ADAMANTINE_TOOL, () -> Ingredient.of(SMTags.INGOTS_ADAMANTINE));

    /*
    NETHERITE(4, 2031, 9.0F, 4.0F, 15, () -> return Ingredient.of(Items.NETHERITE_INGOT);
     */

}
