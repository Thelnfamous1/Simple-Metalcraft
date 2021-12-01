package com.infamous.simple_metalcraft.crafting.furnace;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
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
    public static final int LIT_TIME_ID = 0;
    public static final int LIT_DURATION_ID = 1;
    public static final int COOKING_PROGRESS_ID = 2;
    public static final int COOKING_TOTAL_TIME_ID = 3;
    public static final int DEFAULT_COOK_TIME = 200;
    private final boolean batchSmelt;
    private final RecipeType<? extends SMCookingRecipe> customRecipeType;

    @Nullable
    protected SMCookingRecipe currentRecipe;
    protected final NonNullList<ItemStack> failedMatches;

    public AdvancedFurnaceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RecipeType<? extends SMCookingRecipe> recipeType, boolean batchSmelt) {
        super(blockEntityType, blockPos, blockState, recipeType);
        this.batchSmelt = batchSmelt;
        this.items = NonNullList.withSize(this.getNumSlots(), ItemStack.EMPTY);
        this.failedMatches = NonNullList.withSize(this.getNumInputSlots(), ItemStack.EMPTY);
        this.customRecipeType = recipeType;
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

    protected RecipeType<? extends SMCookingRecipe> getCustomRecipeType() {
        return this.customRecipeType;
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
        boolean wasLit = isLit(afbe);
        boolean changedLitState = false;
        if(isLit(afbe)){
            setLitTime(afbe, getLitTime(afbe) - 1);
        }

        ItemStack fuelSlotStack = afbe.items.get(afbe.getFuelSlot());

        if (isLit(afbe)
                || !fuelSlotStack.isEmpty() && !afbe.inputsEmpty()) {
            SMCookingRecipe recipe = afbe.getFastRecipe();
            int maxStackSize = afbe.getMaxStackSize();

            // If furnace is not lit and we can burn the input, consume the fuel and set it to lit
            if (!isLit(afbe)
                    && afbe.canBurn(recipe, afbe.items, maxStackSize)) {
                setLitTime(afbe, afbe.getBurnDuration(fuelSlotStack));
                setLitDuration(afbe, getLitTime(afbe));
                if(isLit(afbe)){
                    changedLitState = true;
                    if (fuelSlotStack.hasContainerItem())
                        afbe.items.set(afbe.getFuelSlot(), fuelSlotStack.getContainerItem());
                    else
                    if (!fuelSlotStack.isEmpty()) {
                        fuelSlotStack.shrink(1);
                        if (fuelSlotStack.isEmpty()) {
                            afbe.items.set(afbe.getFuelSlot(), fuelSlotStack.getContainerItem());
                        }
                    }
                }
            }

            // If the furnace is lit and we can burn the input, update the cooking progress and potentially give output
            if (isLit(afbe)
                    && afbe.canBurn(recipe, afbe.items, maxStackSize)) {
                setCookingProgress(afbe, getCookingProgress(afbe) + 1);
                if(getCookingProgress(afbe) == getCookingTotalTime(afbe)){
                    setCookingProgress(afbe, 0);
                    setCookingTotalTime(afbe, recipe.getCookingTime());
                    int burnCount = afbe.burn(recipe, afbe.items, maxStackSize);
                    for(int i = 0; i < burnCount; i++){
                        afbe.setRecipeUsed(recipe);
                    }

                    changedLitState = true;
                }
            } else {
                setCookingProgress(afbe, 0);
            }
        } else {
            if(isLit(afbe) && getCookingProgress(afbe) > 0){
                setCookingProgress(afbe, Mth.clamp(getCookingProgress(afbe) - 2, 0, getCookingTotalTime(afbe)));
            }
        }

        if (wasLit != isLit(afbe)) {
            changedLitState = true;
            blockState = blockState.setValue(AbstractFurnaceBlock.LIT, isLit(afbe));
            level.setBlock(blockPos, blockState, 3);
        }

        if (changedLitState) {
            setChanged(level, blockPos, blockState);
        }

    }

    private static void setCookingTotalTime(AdvancedFurnaceBlockEntity afbe, int cookingTime) {
        afbe.setFurnaceData(COOKING_TOTAL_TIME_ID, cookingTime);
    }

    private static void setCookingProgress(AdvancedFurnaceBlockEntity afbe, int cookingProgress) {
        afbe.setFurnaceData(COOKING_PROGRESS_ID, cookingProgress);
    }

    private static int getCookingTotalTime(AdvancedFurnaceBlockEntity afbe) {
        return afbe.getFurnaceData(COOKING_TOTAL_TIME_ID);
    }

    private static int getCookingProgress(AdvancedFurnaceBlockEntity afbe) {
        return afbe.getFurnaceData(COOKING_PROGRESS_ID);
    }

    private static void setLitDuration(AdvancedFurnaceBlockEntity afbe, int litDuration) {
        afbe.setFurnaceData(LIT_DURATION_ID, litDuration);
    }

    private static int getLitTime(AdvancedFurnaceBlockEntity afbe) {
        return afbe.getFurnaceData(LIT_TIME_ID);
    }

    private static void setLitTime(AdvancedFurnaceBlockEntity afbe, int litTime) {
        afbe.setFurnaceData(LIT_TIME_ID, litTime);
    }

    private static boolean isLit(AdvancedFurnaceBlockEntity afbe) {
        return getLitTime(afbe) > 0;
    }

    private int getFurnaceData(int id) {
        return this.dataAccess.get(id);
    }

    private void setFurnaceData(int id, int value) {
        this.dataAccess.set(id, value);
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

        if (slotId >= this.getFirstInputSlot() && slotId < this.getFuelSlot() && !stacksMatch) {
            setCookingTotalTime(this, getTotalCookTime(this.level, this.getCustomRecipeType(), this.buildInputContainer()));
            setCookingProgress(this, 0);
            this.setChanged();
        }

    }

    protected static int getTotalCookTime(Level level, RecipeType<? extends SMCookingRecipe> recipeType, Container container) {
        return level.getRecipeManager().getRecipeFor(recipeType, container, level).map(AbstractCookingRecipe::getCookingTime).orElse(DEFAULT_COOK_TIME);
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
