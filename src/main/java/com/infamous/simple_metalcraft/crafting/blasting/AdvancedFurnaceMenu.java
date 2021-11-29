package com.infamous.simple_metalcraft.crafting.blasting;

import com.infamous.simple_metalcraft.crafting.CookingMenu;
import com.infamous.simple_metalcraft.crafting.SMCookingRecipe;
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
    public static final int INGREDIENT_A_SLOT = 0;
    public static final int INGREDIENT_B_SLOT = 1;
    public static final int INGREDIENT_C_SLOT = 2;
    public static final int FUEL_SLOT = 3;
    public static final int RESULT_A_SLOT = 4;
    public static final int RESULT_B_SLOT = 5;

    public static final int SLOT_COUNT = RESULT_B_SLOT + 1;
    private static final int INV_SLOT_START = SLOT_COUNT;
    private static final int INV_SLOT_END = INV_SLOT_START + 27;
    private static final int USE_ROW_SLOT_START = INV_SLOT_END;
    private static final int USE_ROW_SLOT_END = INV_SLOT_END + 9;

    public static final int PLAYER_INVENTORY_HEIGHT = 3;
    public static final int PLAYER_INVENTORY_WIDTH = 9;
    public static final int PLAYER_HOTBAR_WIDTH = PLAYER_INVENTORY_WIDTH;
    private final Container container;
    private final ContainerData data;
    protected final Level level;
    private final RecipeType<? extends SMCookingRecipe> recipeType;

    protected AdvancedFurnaceMenu(MenuType<?> menuType, RecipeType<? extends SMCookingRecipe> recipeType, int menuId, Inventory inventory) {
        this(menuType, recipeType, menuId, inventory, new SimpleContainer(SLOT_COUNT), new SimpleContainerData(4));
    }

    protected AdvancedFurnaceMenu(MenuType<?> menuType, RecipeType<? extends SMCookingRecipe> recipeType, int menuId, Inventory inventory, Container container, ContainerData containerData) {
        super(menuType, menuId);
        this.recipeType = recipeType;
        checkContainerSize(container, SLOT_COUNT);
        checkContainerDataCount(containerData, 4);
        this.container = container;
        this.data = containerData;
        this.level = inventory.player.level;

        this.buildIngredientSlots(container);
        this.buildFuelSlots(container);
        this.buildResultSlots(inventory, container);
        this.buildPlayerInventorySlots(inventory);

        this.addDataSlots(containerData);
    }

    // SLOT CONSTRUCTION START

    protected void buildIngredientSlots(Container container) {
        this.addSlot(new Slot(container, INGREDIENT_A_SLOT, 56 - 18, 17));
        this.addSlot(new Slot(container, INGREDIENT_B_SLOT, 56, 17));
        this.addSlot(new Slot(container, INGREDIENT_C_SLOT, 56 + 18, 17));
    }

    protected void buildFuelSlots(Container container) {
        this.addSlot(new BlastFurnaceFuelSlot(this, container, FUEL_SLOT, 56, 53));
    }

    protected void buildResultSlots(Inventory inventory, Container container) {
        this.addSlot(new FurnaceResultSlot(inventory.player, container, RESULT_A_SLOT, 116, 35));
        this.addSlot(new FurnaceByproductSlot(inventory.player, container, RESULT_B_SLOT, 116 + 26, 35));
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
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            slotStackCopy = slotStack.copy();
            if (slotId >= RESULT_A_SLOT && slotId < INV_SLOT_START) { // result slots
                if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, slotStackCopy);
            } else if (slotId >= INV_SLOT_START) { // inventory slots
                if (this.canSmelt(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, INGREDIENT_A_SLOT, FUEL_SLOT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, FUEL_SLOT, RESULT_A_SLOT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotId < INV_SLOT_END) {
                    if (!this.moveItemStackTo(slotStack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotId < USE_ROW_SLOT_END && !this.moveItemStackTo(slotStack, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, INV_SLOT_START, USE_ROW_SLOT_END, false)) { // input and fuel slots
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

    protected SimpleContainer buildInputContainer() {
        return new SimpleContainer(
                this.slots.get(INGREDIENT_A_SLOT).getItem().copy(),
                this.slots.get(INGREDIENT_B_SLOT).getItem().copy(),
                this.slots.get(INGREDIENT_C_SLOT).getItem().copy()
        );
    }

    protected boolean isFuel(ItemStack stack) {
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
