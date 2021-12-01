package com.infamous.simple_metalcraft.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.Tier;

public class TieredShearsItem extends ShearsItem {

    private final Tier tier;

    public TieredShearsItem(Tier tier, Properties properties) {
        // vanilla shears have 238 durability, Iron tier has 250 durability, 238 / 250 = 0.952
        super(properties.defaultDurability((int) (tier.getUses() * 0.952F)));
        this.tier = tier;
    }

    public Tier getTier() {
        return this.tier;
    }

    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repairItem) {
        return this.tier.getRepairIngredient().test(repairItem) || super.isValidRepairItem(toRepair, repairItem);
    }
}
