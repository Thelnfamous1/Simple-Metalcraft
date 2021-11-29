package com.infamous.simple_metalcraft.crafting.furnace;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.fmllegacy.hooks.BasicEventHooks;

public class FurnaceByproductSlot extends Slot {
   private final Player player;
   private int removeCount;

   public FurnaceByproductSlot(Player player, Container container, int slotId, int slotX, int slotY) {
      super(container, slotId, slotX, slotY);
      this.player = player;
   }

   public boolean mayPlace(ItemStack p_39553_) {
      return false;
   }

   public ItemStack remove(int i) {
      if (this.hasItem()) {
         this.removeCount += Math.min(i, this.getItem().getCount());
      }

      return super.remove(i);
   }

   public void onTake(Player player, ItemStack stack) {
      this.checkTakeAchievements(stack);
      super.onTake(player, stack);
   }

   protected void onQuickCraft(ItemStack stack, int i) {
      this.removeCount += i;
      this.checkTakeAchievements(stack);
   }

   protected void checkTakeAchievements(ItemStack stack) {
      stack.onCraftedBy(this.player.level, this.player, this.removeCount);

      /*
      if (this.player instanceof ServerPlayer && this.container instanceof AbstractFurnaceBlockEntity) {
         ((AbstractFurnaceBlockEntity)this.container).awardUsedRecipesAndPopExperience((ServerPlayer)this.player);
      }
       */

      this.removeCount = 0;
      BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
   }
}