package com.infamous.simple_metalcraft.crafting;

import com.google.common.collect.Lists;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public abstract class MetalworkingMenu extends AbstractContainerMenu {
   public static final int INPUT_SLOT_1 = 0;
   public static final int INPUT_SLOT_2 = 1;
   public static final int INPUT_SLOT_3 = 2;
   public static final int INPUT_SLOT_4 = 3;
   public static final int RESULT_SLOT = 4;
   private static final int INV_SLOT_START = RESULT_SLOT + 1;
   private static final int INV_SLOT_END = INV_SLOT_START + 27;
   private static final int HOTBAR_SLOT_START = INV_SLOT_END;
   private static final int HOTBAR_SLOT_END = HOTBAR_SLOT_START + 9;
   public static final int SLOT_1_X = 11;
   public static final int SLOT_1_Y = 25;
   public static final int SLOT_SEPARATION = 18;
   public static final int RESULT_SLOT_X = 143;
   public static final int RESULT_SLOT_Y = 33;
   private final ContainerLevelAccess access;
   private final DataSlot selectedRecipeIndex = DataSlot.standalone();
   private final DataSlot levelRequirement = DataSlot.standalone();
   private final DataSlot xpCost = DataSlot.standalone();
   private final Level level;
   private List<? extends MetalworkingRecipe> recipes = Lists.newArrayList();
   //private ItemStack input = ItemStack.EMPTY;
   long lastSoundTime;
   final Slot inputSlot1;
   final Slot inputSlot2;
   final Slot inputSlot3;
   final Slot inputSlot4;
   final Slot resultSlot;
   Runnable slotUpdateListener = () -> {
   };
   public final Container inputContainer = new SimpleContainer(RESULT_SLOT) {
      public void setChanged() {
         super.setChanged();
         MetalworkingMenu.this.slotsChanged(this);
         MetalworkingMenu.this.slotUpdateListener.run();
      }
   };
   final ResultContainer resultContainer = new ResultContainer();

   public MetalworkingMenu(MenuType<?> menuType, int containerId, Inventory inventory) {
      this(menuType, containerId, inventory, ContainerLevelAccess.NULL);
   }

   public MetalworkingMenu(MenuType<?> menuType, int containerId, Inventory inventory, final ContainerLevelAccess access) {
      super(menuType, containerId);
      this.access = access;
      this.level = inventory.player.level;
      this.inputSlot1 = this.addSlot(new Slot(this.inputContainer, INPUT_SLOT_1, SLOT_1_X, SLOT_1_Y));
      this.inputSlot2 = this.addSlot(new Slot(this.inputContainer, INPUT_SLOT_2, SLOT_1_X + SLOT_SEPARATION, SLOT_1_Y));
      this.inputSlot3 = this.addSlot(new Slot(this.inputContainer, INPUT_SLOT_3, SLOT_1_X, SLOT_1_Y + SLOT_SEPARATION));
      this.inputSlot4 = this.addSlot(new Slot(this.inputContainer, INPUT_SLOT_4, SLOT_1_X + SLOT_SEPARATION, SLOT_1_Y + SLOT_SEPARATION));
      this.resultSlot = this.addSlot(new MetalworkingResultSlot(this, this.resultContainer, MetalworkingMenu.RESULT_SLOT, RESULT_SLOT_X, RESULT_SLOT_Y));

      for(int inventoryRow = 0; inventoryRow < 3; ++inventoryRow) {
         for(int inventoryColumn = 0; inventoryColumn < 9; ++inventoryColumn) {
            this.addSlot(new Slot(inventory, inventoryColumn + inventoryRow * 9 + 9, 8 + inventoryColumn * 18, 84 + inventoryRow * 18));
         }
      }

      for(int hotbarSlotId = 0; hotbarSlotId < 9; ++hotbarSlotId) {
         this.addSlot(new Slot(inventory, hotbarSlotId, 8 + hotbarSlotId * 18, 142));
      }

      this.addDataSlot(this.selectedRecipeIndex);
      this.addDataSlot(this.levelRequirement);
      this.addDataSlot(this.xpCost);
   }

   protected boolean mayPickup(Player player, boolean slotHasItem){
      return player.getAbilities().instabuild
              || (this.meetsXPCostRequirement(player) && this.meetsLevelRequirement(player));
   }

   public boolean meetsXPCostRequirement(Player player) {
      return player.experienceLevel >= this.xpCost.get()
              && this.xpCost.get() >= 0;
   }

   public boolean meetsLevelRequirement(Player player) {
      return player.experienceLevel >= this.levelRequirement.get()
              && this.levelRequirement.get() >= 0;
   }

   protected abstract SoundEvent getTakeSound();

   public int getSelectedRecipeIndex() {
      return this.selectedRecipeIndex.get();
   }

   public List<? extends MetalworkingRecipe> getRecipes() {
      return this.recipes;
   }

   public int getNumRecipes() {
      return this.recipes.size();
   }

   public boolean hasInputItem() {
      return !this.inputContainer.isEmpty() && !this.recipes.isEmpty();
   }

   @Override
   public boolean stillValid(Player player) {
      return stillValid(this.access, player, this.getBlock());
   }

   protected abstract Block getBlock();

   @Override
   public boolean clickMenuButton(Player player, int index) {
      if (this.isValidRecipeIndex(index)) {
         this.selectedRecipeIndex.set(index);
         this.setupResultSlot();
      }

      return true;
   }

   private boolean isValidRecipeIndex(int index) {
      return index >= 0 && index < this.recipes.size();
   }

   @Override
   public void slotsChanged(Container container) {
      this.setupRecipeList(container);
   }

   private void setupRecipeList(Container container) {
      this.recipes.clear();
      this.selectedRecipeIndex.set(-1);
      this.resultSlot.set(ItemStack.EMPTY);
      this.levelRequirement.set(0);
      this.xpCost.set(0);
      this.recipes = this.level.getRecipeManager().getRecipesFor(this.getRecipeType(), container, this.level);
   }

   void setupResultSlot() {
      if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
         MetalworkingRecipe metalworkingRecipe = this.recipes.get(this.selectedRecipeIndex.get());
         this.resultContainer.setRecipeUsed(metalworkingRecipe);
         this.resultSlot.set(metalworkingRecipe.assemble(this.inputContainer));
         this.levelRequirement.set(metalworkingRecipe.levelRequirement);
         this.xpCost.set(metalworkingRecipe.xpCost);
      } else {
         this.resultSlot.set(ItemStack.EMPTY);
         this.levelRequirement.set(0);
         this.xpCost.set(0);
      }

      this.broadcastChanges();
   }

   public void registerUpdateListener(Runnable runnable) {
      this.slotUpdateListener = runnable;
   }

   @Override
   public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
      return slot.container != this.resultContainer && super.canTakeItemForPickAll(itemStack, slot);
   }

   @Override
   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack currentStackCopy = ItemStack.EMPTY;
      Slot slot = this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack stackToMove = slot.getItem();
         Item currentItem = stackToMove.getItem();
         currentStackCopy = stackToMove.copy();
         if (index == RESULT_SLOT) {
            currentItem.onCraftedBy(stackToMove, player.level, player);
            if (!this.moveItemStackTo(stackToMove, INV_SLOT_START, HOTBAR_SLOT_END, true)) {
               return ItemStack.EMPTY;
            }

            slot.onQuickCraft(stackToMove, currentStackCopy);
         } else if (index >= INPUT_SLOT_1 && index < INV_SLOT_START) {
            if (!this.moveItemStackTo(stackToMove, INV_SLOT_START, HOTBAR_SLOT_END, false)) {
               return ItemStack.EMPTY;
            }
         } else {
            SimpleContainer testInput = this.buildTestInput(stackToMove);
            if (this.level.getRecipeManager().getRecipeFor(this.getRecipeType(), testInput, this.level).isPresent()) {
               if (!this.moveItemStackTo(stackToMove, INPUT_SLOT_1, RESULT_SLOT, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
               if (!this.moveItemStackTo(stackToMove, HOTBAR_SLOT_START, HOTBAR_SLOT_END, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (index >= INV_SLOT_END && index < HOTBAR_SLOT_END && !this.moveItemStackTo(stackToMove, INV_SLOT_START, INV_SLOT_END, false)) {
               return ItemStack.EMPTY;
            }
         }

         if (stackToMove.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         }

         slot.setChanged();
         if (stackToMove.getCount() == currentStackCopy.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(player, stackToMove);
         this.broadcastChanges();
      }

      return currentStackCopy;
   }

   private SimpleContainer buildTestInput(ItemStack currentStack) {
      SimpleContainer testContainer = new SimpleContainer(this.inputContainer.getContainerSize());
      for(int i = 0; i < this.inputContainer.getContainerSize(); i++){
         testContainer.addItem(this.inputContainer.getItem(i));
      }
      testContainer.addItem(currentStack);
      return testContainer;
   }

   protected abstract RecipeType<? extends MetalworkingRecipe> getRecipeType();

   @Override
   public void removed(Player player) {
      super.removed(player);
      this.resultContainer.removeItemNoUpdate(1);
      this.access.execute((p_40313_, p_40314_) -> {
         this.clearContainer(player, this.inputContainer);
      });
   }

   public int getXpCost() {
      return this.xpCost.get();
   }

   public int getLevelRequirement(){
      return this.levelRequirement.get();
   }

   protected void setXpCost(int xpCost){
      this.xpCost.set(xpCost);
   }

   protected void setLevelRequirement(int levelRequirement){
      this.levelRequirement.set(levelRequirement);
   }

   protected ContainerLevelAccess getAccess() {
      return this.access;
   }
}