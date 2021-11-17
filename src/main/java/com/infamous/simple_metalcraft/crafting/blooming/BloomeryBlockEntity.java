package com.infamous.simple_metalcraft.crafting.blooming;

import com.infamous.simple_metalcraft.SMModEvents;
import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.mixin.AbstractFurnaceBlockEntityAccessor;
import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Optional;

public class BloomeryBlockEntity extends AbstractFurnaceBlockEntity {
   @Nullable
   private BloomingRecipe currentRecipe;
   private ItemStack assembledResult = ItemStack.EMPTY;

   public static final TranslatableComponent BLOOMERY_TITLE = new TranslatableComponent( "container." + SimpleMetalcraft.MOD_ID + ".bloomery");

   public BloomeryBlockEntity(BlockPos blockPos, BlockState blockState) {
   super(SMBlockEntityTypes.BLOOMERY.get(), blockPos, blockState, SMModEvents.BLOOMING);
   }

   protected Component getDefaultName() {
      return BLOOMERY_TITLE;
   }

   protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
      return new BloomeryMenu(containerId, inventory, this, this.dataAccess);
   }

   @Override
   public void load(CompoundTag tagIn) {
      super.load(tagIn);
      if(tagIn.contains("currentRecipe") && tagIn.contains("assembledResult", 10) && this.level != null){
         Optional<? extends Recipe<?>> optionalRecipe = this.level.getRecipeManager().byKey(new ResourceLocation(tagIn.getString("currentRecipe")));
         if(optionalRecipe.isPresent()){
            Recipe<?> currentRecipe = optionalRecipe.get();
            if(currentRecipe instanceof BloomingRecipe){
               this.currentRecipe = (BloomingRecipe) currentRecipe;
               this.assembledResult = ItemStack.of(tagIn.getCompound("assembledResult"));
            }
         }
      }
   }

   @Override
   public CompoundTag save(CompoundTag tagIn) {
      CompoundTag saveTag = super.save(tagIn);
      if(this.currentRecipe != null && !this.assembledResult.isEmpty()){
         saveTag.putString("currentRecipe", this.currentRecipe.getId().toString());
         CompoundTag savedItem = this.assembledResult.save(new CompoundTag());
         saveTag.put("assembledResult", savedItem);
      }
      return saveTag;
   }

   protected static void bloomeryServerTick(Level level, BlockPos blockPos, BlockState blockState, BloomeryBlockEntity bloomeryEntity) {
      AbstractFurnaceBlockEntityAccessor accessor = castToAccessor(bloomeryEntity);
      boolean wasLit = accessor.callIsLit();
      boolean changedLitState = false;
      if (accessor.callIsLit()) {
         accessor.setLitTime(accessor.getLitTime() - 1);
      }

      ItemStack fuelSlotStack = bloomeryEntity.items.get(SLOT_FUEL);

      if (accessor.callIsLit() || !fuelSlotStack.isEmpty() && !bloomeryEntity.items.get(SLOT_INPUT).isEmpty()) {
         BloomingRecipe recipe = level.getRecipeManager().getRecipeFor((RecipeType<BloomingRecipe>)accessor.getRecipeType(), bloomeryEntity, level).orElse(null);
         updateBloomeryRecipe(bloomeryEntity, recipe);
         int maxStackSize = bloomeryEntity.getMaxStackSize();

         // If bloomery not lit and we can bloom the input, consume the fuel and set it to lit
         if (!accessor.callIsLit() && bloomeryEntity.canBloom(bloomeryEntity.items, maxStackSize)) {
            accessor.setLitTime(bloomeryEntity.getBurnDuration(fuelSlotStack));
            accessor.setLitDuration(accessor.getLitTime());
            if (accessor.callIsLit()) {
               changedLitState = true;
               if (fuelSlotStack.hasContainerItem())
                  bloomeryEntity.items.set(SLOT_FUEL, fuelSlotStack.getContainerItem());
               else
               if (!fuelSlotStack.isEmpty()) {
                  fuelSlotStack.shrink(SLOT_FUEL);
                  if (fuelSlotStack.isEmpty()) {
                     bloomeryEntity.items.set(SLOT_FUEL, fuelSlotStack.getContainerItem());
                  }
               }
            }
         }

         // If the bloomery is lit and we can bloom the input, update the cooking progress and potentially give output
         if (accessor.callIsLit() && bloomeryEntity.canBloom(bloomeryEntity.items, maxStackSize)) {
            accessor.setCookingProgress(accessor.getCookingProgress() + 1);
            if (accessor.getCookingProgress() == accessor.getCookingTotalTime()) {
               accessor.setCookingProgress(0);
               accessor.setCookingTotalTime(bloomeryEntity.currentRecipe.getCookingTime());
               if (bloomeryEntity.bloom(bloomeryEntity.items, maxStackSize)) {
                  bloomeryEntity.setRecipeUsed(bloomeryEntity.currentRecipe);
                  resetBloomeryRecipe(bloomeryEntity);
               }

               changedLitState = true;
            }
         } else {
            accessor.setCookingProgress(0);
            resetBloomeryRecipe(bloomeryEntity);
         }
      } else {
         resetBloomeryRecipe(bloomeryEntity);
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

   private static void updateBloomeryRecipe(BloomeryBlockEntity bloomeryEntity, @Nullable BloomingRecipe recipe) {
      if(bloomeryEntity.currentRecipe == null && recipe != null){
         bloomeryEntity.currentRecipe = recipe;
         bloomeryEntity.assembledResult = recipe.assembleRandomResult();
      }
   }

   private static void resetBloomeryRecipe(BloomeryBlockEntity bloomeryEntity) {
      bloomeryEntity.currentRecipe = null;
      bloomeryEntity.assembledResult = ItemStack.EMPTY;
   }

   private static AbstractFurnaceBlockEntityAccessor castToAccessor(BloomeryBlockEntity blockEntity){
      return (AbstractFurnaceBlockEntityAccessor) blockEntity;
   }

   private boolean canBloom(NonNullList<ItemStack> stacks, int maxStackSize) {
      if (!stacks.get(SLOT_INPUT).isEmpty() && this.currentRecipe != null) {
         ItemStack assembledRecipeResult = this.assembledResult;
         if (assembledRecipeResult.isEmpty()) {
            return false;
         } else {
            ItemStack resultSlotStack = stacks.get(SLOT_RESULT);
            if (resultSlotStack.isEmpty()) {
               return true;
            } else if (!resultSlotStack.sameItem(assembledRecipeResult)) {
               return false;
            } else if (resultSlotStack.getCount() + assembledRecipeResult.getCount() <= maxStackSize && resultSlotStack.getCount() + assembledRecipeResult.getCount() <= resultSlotStack.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
               return true;
            } else {
               return resultSlotStack.getCount() + assembledRecipeResult.getCount() <= assembledRecipeResult.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
            }
         }
      } else {
         return false;
      }
   }

   private boolean bloom(NonNullList<ItemStack> stacks, int maxStackSize) {
      if (this.currentRecipe != null && this.canBloom(stacks, maxStackSize)) {
         ItemStack inputSlotStack = stacks.get(SLOT_INPUT);
         ItemStack assembledRecipeResult = this.assembledResult;
         ItemStack resultSlotStack = stacks.get(SLOT_RESULT);
         if (resultSlotStack.isEmpty()) {
            stacks.set(SLOT_RESULT, assembledRecipeResult.copy());
         } else if (resultSlotStack.is(assembledRecipeResult.getItem())) {
            resultSlotStack.grow(assembledRecipeResult.getCount());
         }

         if (inputSlotStack.is(Blocks.WET_SPONGE.asItem()) && !stacks.get(SLOT_FUEL).isEmpty() && stacks.get(SLOT_FUEL).is(Items.BUCKET)) {
            stacks.set(SLOT_FUEL, new ItemStack(Items.WATER_BUCKET));
         }

         inputSlotStack.shrink(1);
         return true;
      } else {
         return false;
      }
   }

}