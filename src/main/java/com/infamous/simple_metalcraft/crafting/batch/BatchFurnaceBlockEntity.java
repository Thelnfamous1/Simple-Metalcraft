package com.infamous.simple_metalcraft.crafting.batch;

import com.infamous.simple_metalcraft.mixin.AbstractFurnaceBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class BatchFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

   protected BatchFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RecipeType<? extends BatchCookingRecipe> recipeType) {
      super(blockEntityType, blockPos, blockState, recipeType);
   }

   public static void batchServerTick(Level level, BlockPos blockPos, BlockState blockState, BatchFurnaceBlockEntity bfbe) {
      AbstractFurnaceBlockEntityAccessor accessor = castToAccessor(bfbe);
      boolean wasLit = accessor.callIsLit();
      boolean changedLitState = false;
      if (accessor.callIsLit()) {
         accessor.setLitTime(accessor.getLitTime() - 1);
      }

      ItemStack fuelSlotStack = bfbe.items.get(SLOT_FUEL);

      if (accessor.callIsLit() || !fuelSlotStack.isEmpty() && !bfbe.items.get(SLOT_INPUT).isEmpty()) {
         BatchCookingRecipe recipe = level.getRecipeManager().getRecipeFor((RecipeType<BatchCookingRecipe>)accessor.getRecipeType(), bfbe, level).orElse(null);
         int maxStackSize = bfbe.getMaxStackSize();

         // If batch furnace is not lit and we can batch burn the input, consume the fuel and set it to lit
         if (!accessor.callIsLit() && bfbe.canBurnBatch(recipe, bfbe.items, maxStackSize)) {
            accessor.setLitTime(bfbe.getBurnDuration(fuelSlotStack));
            accessor.setLitDuration(accessor.getLitTime());
            if (accessor.callIsLit()) {
               changedLitState = true;
               if (fuelSlotStack.hasContainerItem())
                  bfbe.items.set(SLOT_FUEL, fuelSlotStack.getContainerItem());
               else
               if (!fuelSlotStack.isEmpty()) {
                  fuelSlotStack.shrink(SLOT_FUEL);
                  if (fuelSlotStack.isEmpty()) {
                     bfbe.items.set(SLOT_FUEL, fuelSlotStack.getContainerItem());
                  }
               }
            }
         }

         // If the batch furnace is lit and we can batch burn the input, update the cooking progress and potentially give output
         if (accessor.callIsLit() && bfbe.canBurnBatch(recipe, bfbe.items, maxStackSize)) {
            accessor.setCookingProgress(accessor.getCookingProgress() + 1);
            if (accessor.getCookingProgress() == accessor.getCookingTotalTime()) {
               accessor.setCookingProgress(0);
               accessor.setCookingTotalTime(recipe.getCookingTime());
               int batchCount = bfbe.burnBatch(recipe, bfbe.items, maxStackSize);
               for(int i = 0; i < batchCount; i++){
                  bfbe.setRecipeUsed(recipe);
               }

               changedLitState = true;
            }
         } else {
            accessor.setCookingProgress(0);
         }
      } else {
         if (!accessor.callIsLit() && accessor.getCookingProgress() > 0) {
            accessor.setCookingProgress(Mth.clamp(accessor.getCookingProgress() - 2, 0, accessor.getCookingTotalTime()));
         }
      }

      if (wasLit != accessor.callIsLit()) {
         changedLitState = true;
         blockState = blockState.setValue(AbstractFurnaceBlock.LIT, Boolean.valueOf(accessor.callIsLit()));
         level.setBlock(blockPos, blockState, 3);
      }

      if (changedLitState) {
         setChanged(level, blockPos, blockState);
      }

   }

   private static AbstractFurnaceBlockEntityAccessor castToAccessor(BatchFurnaceBlockEntity blockEntity){
      return (AbstractFurnaceBlockEntityAccessor) blockEntity;
   }

   protected boolean canBurnBatch(@Nullable BatchCookingRecipe recipe, NonNullList<ItemStack> stacks, int maxStackSize) {
      ItemStack inputSlotStack = stacks.get(SLOT_INPUT);
      if (!inputSlotStack.isEmpty() && recipe != null) {
         ItemStack testRecipeResult = recipe.assemble(this).copy();
         if (testRecipeResult.isEmpty()) {
            return false;
         } else {
            testRecipeResult.setCount(testRecipeResult.getCount() * inputSlotStack.getCount()); // create batch result
            ItemStack resultSlotStack = stacks.get(SLOT_RESULT);
            if (resultSlotStack.isEmpty()) {
               return true;
            } else if (!resultSlotStack.sameItem(testRecipeResult)) {
               return false;
            } else if (resultSlotStack.getCount() + testRecipeResult.getCount() <= maxStackSize && resultSlotStack.getCount() + testRecipeResult.getCount() <= resultSlotStack.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
               return true;
            } else {
               return resultSlotStack.getCount() + testRecipeResult.getCount() <= testRecipeResult.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
            }
         }
      } else {
         return false;
      }
   }

   protected int burnBatch(@Nullable BatchCookingRecipe recipe, NonNullList<ItemStack> stacks, int maxStackSize) {
      if (recipe != null && this.canBurnBatch(recipe, stacks, maxStackSize)) {
         ItemStack inputSlotStack = stacks.get(SLOT_INPUT);
         ItemStack recipeResult = recipe.assemble(this).copy();
         int inputCount = inputSlotStack.getCount();
         recipeResult.setCount(recipeResult.getCount() * inputCount); // create batch result
         ItemStack resultSlotStack = stacks.get(SLOT_RESULT);
         if (resultSlotStack.isEmpty()) {
            stacks.set(SLOT_RESULT, recipeResult.copy());
         } else if (resultSlotStack.sameItem(recipeResult)) {
            resultSlotStack.grow(recipeResult.getCount());
         }

         //TODO: Keep this or delete it?
         if (inputSlotStack.is(Blocks.WET_SPONGE.asItem()) && !stacks.get(SLOT_FUEL).isEmpty() && stacks.get(SLOT_FUEL).is(Items.BUCKET)) {
            stacks.set(SLOT_FUEL, new ItemStack(Items.WATER_BUCKET));
         }

         inputSlotStack.shrink(inputCount); // consume batch
         return inputCount;
      } else {
         return 0;
      }
   }
}