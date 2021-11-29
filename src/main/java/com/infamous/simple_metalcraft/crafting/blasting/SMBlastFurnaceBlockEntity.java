package com.infamous.simple_metalcraft.crafting.blasting;

import com.infamous.simple_metalcraft.crafting.SMCookingRecipe;
import com.infamous.simple_metalcraft.mixin.AbstractFurnaceBlockEntityAccessor;
import com.infamous.simple_metalcraft.registry.SMBlockEntityTypes;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public class SMBlastFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    protected static final int SLOT_INPUT_A = 0;
    protected static final int SLOT_INPUT_B = 1;
    protected static final int SLOT_INPUT_C = 2;
    protected static final int CUSTOM_SLOT_FUEL = 3;
    protected static final int SLOT_RESULT_A = 4;
    protected static final int SLOT_RESULT_B = 5;
    protected static final int NUM_SLOTS = SLOT_RESULT_B + 1;
    private static final int[] CUSTOM_SLOTS_FOR_UP = new int[]{SLOT_INPUT_A, SLOT_INPUT_B, SLOT_INPUT_C};
    private static final int[] CUSTOM_SLOTS_FOR_DOWN = new int[]{SLOT_RESULT_A, SLOT_RESULT_B, CUSTOM_SLOT_FUEL};
    private static final int[] CUSTOM_SLOTS_FOR_SIDES = new int[]{CUSTOM_SLOT_FUEL};
    public static final TranslatableComponent BLAST_FURNACE_COMPONENT = new TranslatableComponent("container.blast_furnace");

    public SMBlastFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SMBlockEntityTypes.BLAST_FURNACE.get(), blockPos, blockState, SMRecipes.Types.BLASTING);
        this.items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
    }

    public static void blastServerTick(Level level, BlockPos blockPos, BlockState blockState, SMBlastFurnaceBlockEntity bfbe) {
        AbstractFurnaceBlockEntityAccessor accessor = castToAccessor(bfbe);
        boolean wasLit = accessor.callIsLit();
        boolean changedLitState = false;
        if (accessor.callIsLit()) {
            accessor.setLitTime(accessor.getLitTime() - 1);
        }

        ItemStack fuelSlotStack = bfbe.items.get(CUSTOM_SLOT_FUEL);

        if (accessor.callIsLit() || !fuelSlotStack.isEmpty() && !bfbe.inputsEmpty()) {
            SMCookingRecipe recipe = level.getRecipeManager().getRecipeFor((RecipeType<SMCookingRecipe>)accessor.getRecipeType(), bfbe.buildInputContainer(), level).orElse(null);
            int maxStackSize = bfbe.getMaxStackSize();

            // If blast furnace is not lit and we can burn the input, consume the fuel and set it to lit
            if (!accessor.callIsLit() && bfbe.canBurn(recipe, bfbe.items, maxStackSize)) {
                accessor.setLitTime(bfbe.getBurnDuration(fuelSlotStack));
                accessor.setLitDuration(accessor.getLitTime());
                if (accessor.callIsLit()) {
                    changedLitState = true;
                    if (fuelSlotStack.hasContainerItem())
                        bfbe.items.set(CUSTOM_SLOT_FUEL, fuelSlotStack.getContainerItem());
                    else
                    if (!fuelSlotStack.isEmpty()) {
                        fuelSlotStack.shrink(CUSTOM_SLOT_FUEL);
                        if (fuelSlotStack.isEmpty()) {
                            bfbe.items.set(CUSTOM_SLOT_FUEL, fuelSlotStack.getContainerItem());
                        }
                    }
                }
            }

            // If the blast furnace is lit and we can burn the input, update the cooking progress and potentially give output
            if (accessor.callIsLit() && bfbe.canBurn(recipe, bfbe.items, maxStackSize)) {
                accessor.setCookingProgress(accessor.getCookingProgress() + 1);
                if (accessor.getCookingProgress() == accessor.getCookingTotalTime()) {
                    accessor.setCookingProgress(0);
                    //noinspection ConstantConditions
                    accessor.setCookingTotalTime(recipe.getCookingTime());
                    if(bfbe.burn(recipe, bfbe.items, maxStackSize)){
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

    protected Container buildInputContainer() {
        return new SimpleContainer(this.getInputsAsList().toArray(ItemStack[]::new));
    }

    protected boolean inputsEmpty() {
        return this.items.get(SLOT_INPUT_A).isEmpty()
                && this.items.get(SLOT_INPUT_B).isEmpty()
                && this.items.get(SLOT_INPUT_C).isEmpty();
    }

    protected NonNullList<ItemStack> getInputsAsList(){
        return NonNullList.of(
                ItemStack.EMPTY, // default value
                this.items.get(SLOT_INPUT_A),
                this.items.get(SLOT_INPUT_B),
                this.items.get(SLOT_INPUT_C)
        );
    }

    protected NonNullList<ItemStack> getOutputsAsList(){
        return NonNullList.of(
                ItemStack.EMPTY, // default value
                this.items.get(SLOT_RESULT_A),
                this.items.get(SLOT_RESULT_B)
        );
    }

    protected boolean canBurn(@Nullable SMCookingRecipe recipe, NonNullList<ItemStack> furnaceStacks, int maxStackSize) {
        if (!this.inputsEmpty() && recipe != null) {
            boolean resultACheck = this.checkResultSlot(recipe, furnaceStacks, maxStackSize, 0, SLOT_RESULT_A);
            boolean resultBCheck = this.checkResultSlot(recipe, furnaceStacks, maxStackSize, 1, SLOT_RESULT_B);
            return resultACheck && resultBCheck;
        } else {
            return false;
        }
    }

    private boolean checkResultSlot(SMCookingRecipe recipe, NonNullList<ItemStack> furnaceStacks, int maxStackSize, int resultIndex, int resultSlotId) {
        ItemStack testResultStack = recipe.assemble(resultIndex);
        if (testResultStack.isEmpty()) {
            return false;
        } else {
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

    protected boolean burn(@Nullable SMCookingRecipe recipe, NonNullList<ItemStack> furnaceStacks, int maxStackSize) {
        if (recipe != null && this.canBurn(recipe, furnaceStacks, maxStackSize)) {
            this.updateResultSlot(recipe, furnaceStacks, 0, SLOT_RESULT_A);
            this.updateResultSlot(recipe, furnaceStacks, 1, SLOT_RESULT_B);

            // TODO: Make "sponge smelting" more generalized
            if (this.getInputsAsList().stream().anyMatch(s -> s.is(Blocks.WET_SPONGE.asItem()))
                    && !furnaceStacks.get(CUSTOM_SLOT_FUEL).isEmpty()
                    && furnaceStacks.get(CUSTOM_SLOT_FUEL).is(Items.BUCKET)) {
                furnaceStacks.set(CUSTOM_SLOT_FUEL, new ItemStack(Items.WATER_BUCKET));
            }

            recipe.consumeInputs(this.buildInputContainer());
            return true;
        } else {
            return false;
        }
    }

    private void updateResultSlot(SMCookingRecipe recipe, NonNullList<ItemStack> furnaceStacks, int resultIndex, int resultSlotId) {
        ItemStack resultStack = recipe.assemble(resultIndex);
        ItemStack resultSlotStack = furnaceStacks.get(resultSlotId);
        if (resultSlotStack.isEmpty()) {
            furnaceStacks.set(resultSlotId, resultStack.copy());
        } else if (resultSlotStack.is(resultStack.getItem())) {
            resultSlotStack.grow(resultStack.getCount());
        }
    }

    @Override
    protected Component getDefaultName() {
        return BLAST_FURNACE_COMPONENT;
    }

    @Override
    protected int getBurnDuration(ItemStack stack) {
        return super.getBurnDuration(stack) / 2;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new SMBlastFurnaceMenu(id, inventory, this, this.dataAccess);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return CUSTOM_SLOTS_FOR_DOWN;
        } else {
            return direction == Direction.UP ? CUSTOM_SLOTS_FOR_UP : CUSTOM_SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int slotId, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && slotId == CUSTOM_SLOT_FUEL) {
            return stack.is(Items.WATER_BUCKET) || stack.is(Items.BUCKET);
        } else {
            return true;
        }
    }

    @Override
    public void setItem(int slotId, ItemStack stack) {
        ItemStack stackInSlot = this.items.get(slotId);
        boolean stacksMatch = !stack.isEmpty() && stack.sameItem(stackInSlot) && ItemStack.tagMatches(stack, stackInSlot);
        this.items.set(slotId, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (slotId >= SLOT_INPUT_A && slotId < CUSTOM_SLOT_FUEL && !stacksMatch) {
            AbstractFurnaceBlockEntityAccessor accessor = castToAccessor(this);
            accessor.setCookingTotalTime(accessor.callGetTotalCookTime(this.level, accessor.getRecipeType(), this.buildInputContainer()));
            accessor.setCookingProgress(0);
            this.setChanged();
        }

    }

    @Override
    public boolean canPlaceItem(int slotId, ItemStack stack) {
        if (slotId > CUSTOM_SLOT_FUEL && slotId < NUM_SLOTS) {
            return false;
        } else if (slotId != CUSTOM_SLOT_FUEL) {
            return true;
        } else {
            ItemStack fuelSlotStack = this.items.get(CUSTOM_SLOT_FUEL);
            AbstractFurnaceBlockEntityAccessor accessor = castToAccessor(this);
            return ForgeHooks.getBurnTime(stack, accessor.getRecipeType()) > 0 || stack.is(Items.BUCKET) && !fuelSlotStack.is(Items.BUCKET);
        }
    }

    public static AbstractFurnaceBlockEntityAccessor castToAccessor(SMBlastFurnaceBlockEntity blastFurnaceBlockEntity){
        return (AbstractFurnaceBlockEntityAccessor) blastFurnaceBlockEntity;
    }
}
