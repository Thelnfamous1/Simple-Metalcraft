package com.infamous.simple_metalcraft.crafting.furnace;

import com.infamous.simple_metalcraft.mixin.AbstractFurnaceBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public abstract class AdvancedFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private final boolean batchSmelt;

    @Nullable
    protected SMCookingRecipe currentRecipe;
    protected final NonNullList<ItemStack> failedMatches;

    public AdvancedFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RecipeType<? extends SMCookingRecipe> recipeType, boolean batchSmelt) {
        super(blockEntityType, blockPos, blockState, recipeType);
        this.batchSmelt = batchSmelt;
        this.items = NonNullList.withSize(this.getNumSlots(), ItemStack.EMPTY);
        this.failedMatches = NonNullList.withSize(this.getNumInputSlots(), ItemStack.EMPTY);
    }

    public static AbstractFurnaceBlockEntityAccessor castToAccessor(AdvancedFurnaceBlockEntity afbe){
        return (AbstractFurnaceBlockEntityAccessor) afbe;
    }

    // Using similar logic as FastFurnace's recipe querying optimization
    @Nullable
    protected SMCookingRecipe getFastRecipe() {
        NonNullList<ItemStack> inputs = this.getInputsAsList();
        if (this.inputsEmpty() || this.failedMatchesSameAs(inputs)){ // O(n) operation
            return null;
        }
        if (this.currentRecipe == null || !this.currentRecipe.matches(this.buildInputContainer(), this.level)) {
            SMCookingRecipe recipe = this.level.getRecipeManager()
                    .getRecipeFor(this.getCustomRecipeType(), this, this.level)
                    .orElse(null);
            this.failedMatches.clear(); // retains the size of the list, with ItemStack.EMPTY being placed at each index
            if (recipe == null) {
                this.buildFailedMatches(inputs);
            }
            this.currentRecipe = recipe;
        }
        return this.currentRecipe;
    }

    // Have to cast twice because recipeType field is private in AbstractFurnaceBlockEntity
    protected RecipeType<SMCookingRecipe> getCustomRecipeType() {
        return (RecipeType<SMCookingRecipe>) castToAccessor(this).getRecipeType();
    }

    private void buildFailedMatches(NonNullList<ItemStack> inputs) {
        int size = Math.min(this.failedMatches.size(), inputs.size()); // they should always be the same size, but we will try to dummy-proof
        for(int i = 0; i < size; i++){
            this.failedMatches.set(i, inputs.get(i));
        }
    }

    private boolean failedMatchesSameAs(NonNullList<ItemStack> inputs) {
        boolean matches = true;
        int size = Math.min(this.failedMatches.size(), inputs.size()); // they should always be the same size, but we will try to dummy-proof
        for(int i = 0; i < size; i++){
            matches &= ItemStack.isSameItemSameTags(this.failedMatches.get(i), inputs.get(i));
        }
        return matches;
    }

    protected Container buildInputContainer() {
        return new SimpleContainer(this.getInputsAsList().toArray(ItemStack[]::new));
    }

    protected Container buildOutputContainer() {
        return new SimpleContainer(this.getOutputsAsList().toArray(ItemStack[]::new));
    }

    public static void advancedServerTick(Level level, BlockPos blockPos, BlockState blockState, AdvancedFurnaceBlockEntity afbe) {
        AbstractFurnaceBlockEntityAccessor accessor = castToAccessor(afbe);
        boolean wasLit = accessor.callIsLit();
        boolean changedLitState = false;
        if (accessor.callIsLit()) {
            accessor.setLitTime(accessor.getLitTime() - 1);
        }

        ItemStack fuelSlotStack = afbe.items.get(afbe.getFuelSlot());

        if (accessor.callIsLit() || !fuelSlotStack.isEmpty() && !afbe.inputsEmpty()) {
            SMCookingRecipe recipe = afbe.getFastRecipe();
            int maxStackSize = afbe.getMaxStackSize();

            // If furnace is not lit and we can burn the input, consume the fuel and set it to lit
            if (!accessor.callIsLit() && afbe.canBurn(recipe, afbe.items, maxStackSize)) {
                accessor.setLitTime(afbe.getBurnDuration(fuelSlotStack));
                accessor.setLitDuration(accessor.getLitTime());
                if (accessor.callIsLit()) {
                    changedLitState = true;
                    if (fuelSlotStack.hasContainerItem())
                        afbe.items.set(afbe.getFuelSlot(), fuelSlotStack.getContainerItem());
                    else
                    if (!fuelSlotStack.isEmpty()) {
                        fuelSlotStack.shrink(afbe.getFuelSlot());
                        if (fuelSlotStack.isEmpty()) {
                            afbe.items.set(afbe.getFuelSlot(), fuelSlotStack.getContainerItem());
                        }
                    }
                }
            }

            // If the furnace is lit and we can burn the input, update the cooking progress and potentially give output
            if (accessor.callIsLit() && afbe.canBurn(recipe, afbe.items, maxStackSize)) {
                accessor.setCookingProgress(accessor.getCookingProgress() + 1);
                if (accessor.getCookingProgress() == accessor.getCookingTotalTime()) {
                    accessor.setCookingProgress(0);
                    //noinspection ConstantConditions
                    accessor.setCookingTotalTime(recipe.getCookingTime());
                    int burnCount = afbe.burn(recipe, afbe.items, maxStackSize);
                    for(int i = 0; i < burnCount; i++){
                        afbe.setRecipeUsed(recipe);
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

    protected boolean inputsEmpty(){
        return this.getInputsAsList().stream().allMatch(ItemStack::isEmpty);
    }

    protected abstract NonNullList<ItemStack> getInputsAsList();

    protected abstract NonNullList<ItemStack> getOutputsAsList();

    protected boolean canBurn(@Nullable SMCookingRecipe recipe, NonNullList<ItemStack> furnaceStacks, int maxStackSize) {
        if (!this.inputsEmpty() && recipe != null) {
            int batchSize = 1;
            if(this.batchSmelt){
                batchSize = recipe.getBatchSize(this.buildInputContainer());
            }
            boolean resultCheck = true;
            for(int i = 0; i < this.getOutputsAsList().size(); i++){
                int resultSlotId = this.getFuelSlot() + i + 1;
                resultCheck &= this.checkResultSlot(recipe, furnaceStacks, maxStackSize, i, resultSlotId, batchSize);
            }
            return resultCheck;
        } else {
            return false;
        }
    }

    private boolean checkResultSlot(SMCookingRecipe recipe, NonNullList<ItemStack> furnaceStacks, int maxStackSize, int resultIndex, int resultSlotId, int batchSize) {
        ItemStack testResultStack = recipe.assemble(resultIndex);
        if (testResultStack.isEmpty()) {
            return false;
        } else {
            testResultStack.setCount(testResultStack.getCount() * batchSize); // create batch result
            ItemStack resultSlotStack = furnaceStacks.get(resultSlotId);
            if (resultSlotStack.isEmpty()) {
                return true;
            } else if (!resultSlotStack.sameItem(testResultStack)) {
                return false;
            } else if (resultSlotStack.getCount() + testResultStack.getCount() <= maxStackSize && resultSlotStack.getCount() + testResultStack.getCount() <= resultSlotStack.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                return true;
            } else {
                return resultSlotStack.getCount() + testResultStack.getCount() <= testResultStack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
            }
        }
    }

    protected int burn(@Nullable SMCookingRecipe recipe, NonNullList<ItemStack> furnaceStacks, int maxStackSize) {
        if (recipe != null && this.canBurn(recipe, furnaceStacks, maxStackSize)) {
            int batchSize = 1;
            if(this.batchSmelt){
                batchSize = recipe.getBatchSize(this.buildInputContainer());
            }
            for(int i = 0; i < this.getOutputsAsList().size(); i++){
                int resultSlotId = this.getFuelSlot() + i + 1;
                this.updateResultSlot(recipe, furnaceStacks, i, resultSlotId, batchSize);
            }

            // TODO: Make "sponge smelting" more generalized
            ItemStack fuelSlotStack = furnaceStacks.get(this.getFuelSlot());
            if (this.getInputsAsList().stream().anyMatch(s -> s.is(Blocks.WET_SPONGE.asItem()))
                    && !fuelSlotStack.isEmpty()
                    && fuelSlotStack.is(Items.BUCKET)) {
                furnaceStacks.set(this.getFuelSlot(), new ItemStack(Items.WATER_BUCKET));
            }

            recipe.consumeInputs(this.buildInputContainer(), batchSize);
            return batchSize;
        } else {
            return 0;
        }
    }

    private void updateResultSlot(SMCookingRecipe recipe, NonNullList<ItemStack> furnaceStacks, int resultIndex, int resultSlotId, int batchSize) {
        ItemStack resultStack = recipe.assemble(resultIndex).copy();
        resultStack.setCount(resultStack.getCount() * batchSize); // create batch result
        ItemStack resultSlotStack = furnaceStacks.get(resultSlotId);
        if (resultSlotStack.isEmpty()) {
            furnaceStacks.set(resultSlotId, resultStack.copy());
        } else if (resultSlotStack.is(resultStack.getItem())) {
            resultSlotStack.grow(resultStack.getCount());
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return this.getDownSlots();
        } else {
            return direction == Direction.UP ? this.getUpSlots() : this.getSideSlots();
        }
    }

    protected abstract int[] getSideSlots();

    protected abstract int[] getUpSlots();

    protected abstract int[] getDownSlots();

    @Override
    public boolean canTakeItemThroughFace(int slotId, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && slotId == this.getFuelSlot()) {
            return stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET);
        } else {
            return true;
        }
    }

    protected abstract int getFuelSlot();

    @Override
    public void setItem(int slotId, ItemStack stack) {
        ItemStack stackInSlot = this.items.get(slotId);
        boolean stacksMatch = !stack.isEmpty() && stack.sameItem(stackInSlot) && ItemStack.tagMatches(stack, stackInSlot);
        this.items.set(slotId, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (slotId >= getFirstInputSlot() && slotId < this.getFuelSlot() && !stacksMatch) {
            AbstractFurnaceBlockEntityAccessor accessor = castToAccessor(this);
            accessor.setCookingTotalTime(accessor.callGetTotalCookTime(this.level, this.getCustomRecipeType(), this.buildInputContainer()));
            accessor.setCookingProgress(0);
            this.setChanged();
        }

    }

    protected abstract int getFirstInputSlot();

    @Override
    public boolean canPlaceItem(int slotId, ItemStack stack) {
        if (slotId > this.getFuelSlot() && slotId < this.getNumSlots()) {
            return false;
        } else if (slotId != this.getFuelSlot()) {
            return true;
        } else {
            ItemStack fuelSlotStack = this.items.get(this.getFuelSlot());
            return ForgeHooks.getBurnTime(stack, this.getCustomRecipeType()) > 0 || stack.is(Items.BUCKET) && !fuelSlotStack.is(Items.BUCKET);
        }
    }

    protected abstract int getNumSlots();

    protected abstract int getNumInputSlots();
}
