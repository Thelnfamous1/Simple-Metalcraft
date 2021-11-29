package com.infamous.simple_metalcraft.crafting.furnace;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AdvancedFurnaceFuelSlot extends Slot {
   private final AdvancedFurnaceMenu menu;

   public AdvancedFurnaceFuelSlot(AdvancedFurnaceMenu blastBurnaceMenu, Container container, int slotId, int slotX, int slotY) {
      super(container, slotId, slotX, slotY);
      this.menu = blastBurnaceMenu;
   }

   public boolean mayPlace(ItemStack stack) {
      return this.menu.isFuel(stack) || isBucket(stack);
   }

   public int getMaxStackSize(ItemStack stack) {
      return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
   }

   public static boolean isBucket(ItemStack stack) {
      return stack.is(Items.BUCKET);
   }
}