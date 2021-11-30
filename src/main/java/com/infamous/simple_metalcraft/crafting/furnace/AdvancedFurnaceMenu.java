package com.infamous.simple_metalcraft.crafting.furnace;

import com.infamous.simple_metalcraft.crafting.CookingMenu;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

public abstract class AdvancedFurnaceMenu extends AbstractContainerMenu implements CookingMenu {
    public static final int PLAYER_INVENTORY_HEIGHT = 3;
    public static final int PLAYER_INVENTORY_WIDTH = 9;
    public static final int PLAYER_HOTBAR_WIDTH = PLAYER_INVENTORY_WIDTH;
    public static final int DATA_COUNT = 4;
    private final Container container;
    private final ContainerData data;
    protected final Level level;
    private final RecipeType<? extends SMCookingRecipe> recipeType;

    protected AdvancedFurnaceMenu(MenuType<?> menuType, RecipeType<? extends SMCookingRecipe> recipeType, int menuId, Inventory inventory, int numSlots, int numIngredientSlots, int numResultSlots) {
        this(menuType, recipeType, menuId, inventory, new SimpleContainer(numSlots), new SimpleContainerData(DATA_COUNT), numSlots, numIngredientSlots, numResultSlots);
    }

    protected AdvancedFurnaceMenu(MenuType<?> menuType, RecipeType<? extends SMCookingRecipe> recipeType, int menuId, Inventory inventory, Container container, ContainerData containerData, int numSlots, int numIngredientSlots, int numResultSlots) {
        super(menuType, menuId);
        this.recipeType = recipeType;
        checkContainerSize(container, numSlots);
        checkContainerDataCount(containerData, DATA_COUNT);
        this.container = container;
        this.data = containerData;
        this.level = inventory.player.level;

        this.buildIngredientSlots(container, numIngredientSlots);
        this.buildFuelSlots(container);
        this.buildResultSlots(inventory, container, numResultSlots);
        this.buildPlayerInventorySlots(inventory);

        this.addDataSlots(containerData);
    }

    // SLOT CONSTRUCTION START

    protected void buildIngredientSlots(Container container, int numIngredientSlots) {
        int leftOffset = 9 * (numIngredientSlots - 1);
        for(int i = 0; i < numIngredientSlots; i++){
            int rightOffset = 18 * i;
            this.addSlot(new Slot(container, this.getFirstIngredientSlot() + i, 56 - leftOffset + rightOffset, 17));
        }
    }

    protected void buildFuelSlots(Container container) {
        this.addSlot(new AdvancedFurnaceFuelSlot(this, container, this.getFuelSlot(), 56, 53));
    }

    protected void buildResultSlots(Inventory inventory, Container container, int numResultSlots) {
        this.addSlot(new FurnaceResultSlot(inventory.player, container, this.getFirstResultSlot(), 116, 35));
        for(int i = 1; i < numResultSlots; i++){
            this.addSlot(new FurnaceByproductSlot(inventory.player, container, this.getFirstResultSlot() + i, 116 + (26 * i), 35));
        }
    }

    protected void buildPlayerInventorySlots(Inventory inventory) {
        for(int i = 0; i < PLAYER_INVENTORY_HEIGHT; ++i) {
            for(int j = 0; j < PLAYER_INVENTORY_WIDTH; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < PLAYER_HOTBAR_WIDTH; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }

    // SLOT CONSTRUCTION END

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotId) {
        ItemStack slotStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotId);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            slotStackCopy = slotStack.copy();
            if (slotId >= this.getFirstResultSlot() && slotId < this.getInvSlotStart()) { // result slots
                if (!this.moveItemStackTo(slotStack, this.getInvSlotStart(), this.getUseRowSlotEnd(), true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, slotStackCopy);
            } else if (slotId >= this.getInvSlotStart()) { // inventory slots
                if (this.canSmelt(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, this.getFirstIngredientSlot(), this.getFuelSlot(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, this.getFuelSlot(), this.getFirstResultSlot(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotId < this.getInvSlotEnd()) {
                    if (!this.moveItemStackTo(slotStack, this.getUseRowSlotStart(), this.getUseRowSlotEnd(), false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotId < this.getUseRowSlotEnd() && !this.moveItemStackTo(slotStack, this.getInvSlotStart(), getInvSlotEnd(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, this.getInvSlotStart(), this.getUseRowSlotEnd(), false)) { // input and fuel slots
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == slotStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return slotStackCopy;
    }

    protected abstract int getFuelSlot();

    protected abstract int getFirstIngredientSlot();

    protected abstract int getInvSlotStart();

    protected abstract int getFirstResultSlot();

    private int getUseRowSlotEnd() {
        return this.getUseRowSlotStart() + PLAYER_INVENTORY_WIDTH;
    }

    private int getUseRowSlotStart() {
        return this.getInvSlotEnd();
    }

    private int getInvSlotEnd() {
        return this.getInvSlotStart() + (PLAYER_INVENTORY_WIDTH * PLAYER_INVENTORY_HEIGHT);
    }

    protected boolean canSmelt(ItemStack stack) {
        ItemStack copy = stack.copy();
        SimpleContainer inputContainer = this.buildInputContainer();
        if(inputContainer.canAddItem(copy)){
            return false;
        } else{
            inputContainer.addItem(copy);
        }
        return this.level.getRecipeManager().getRecipeFor(this.recipeType, inputContainer, this.level).isPresent();
    }

    protected abstract SimpleContainer buildInputContainer();

    public boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, this.recipeType) > 0;
    }

    @Override
    public int getBurnProgress() {
        int cookingProgress = this.data.get(2);
        int cookingTotalTime = this.data.get(3);
        return cookingTotalTime != 0 && cookingProgress != 0 ? cookingProgress * 24 / cookingTotalTime : 0;
    }

    @Override
    public int getLitProgress() {
        int litDuration = this.data.get(1);
        if (litDuration == 0) {
            litDuration = 200;
        }

        return this.data.get(0) * 13 / litDuration;
    }

    @Override
    public boolean isLit() {
        return this.data.get(0) > 0;
    }
}
