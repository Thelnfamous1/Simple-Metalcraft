package com.infamous.simple_metalcraft.crafting.anvil;

import com.infamous.simple_metalcraft.registry.SMMenuTypes;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class SMAnvilMenu extends AnvilMenu {
    public static final int INGREDIENT_SLOT = 0;
    public static final int CATALYST_SLOT = 1;
    public static final int RESULT_SLOT = 2;
    public static final int MAX_COST = 40;
    public static final int ANVIL_DESTROY_EVENT_ID = 1029;
    public static final int ANVIL_USE_EVENT_ID = 1030;
    private final MenuType<?> customMenuType;

    public SMAnvilMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public SMAnvilMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(containerId, inventory, access);
        this.customMenuType = SMMenuTypes.ANVIL.get();
    }

    @Override
    public MenuType<?> getType() {
        if (this.customMenuType == null) {
            throw new UnsupportedOperationException("Unable to construct this menu by type");
        } else {
            return this.customMenuType;
        }
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            player.giveExperienceLevels(-this.getCost());
        }

        float breakChance = ForgeHooks.onAnvilRepair(player, stack, SMAnvilMenu.this.inputSlots.getItem(INGREDIENT_SLOT), SMAnvilMenu.this.inputSlots.getItem(CATALYST_SLOT));

        this.inputSlots.setItem(INGREDIENT_SLOT, ItemStack.EMPTY);
        if (this.repairItemCountCost > 0) {
            ItemStack catalyst = this.inputSlots.getItem(CATALYST_SLOT);
            if (!catalyst.isEmpty() && catalyst.getCount() > this.repairItemCountCost) {
                catalyst.shrink(this.repairItemCountCost);
                this.inputSlots.setItem(CATALYST_SLOT, catalyst);
            } else {
                this.inputSlots.setItem(CATALYST_SLOT, ItemStack.EMPTY);
            }
        } else {
            this.inputSlots.setItem(CATALYST_SLOT, ItemStack.EMPTY);
        }

        this.setMaximumCost(0);
        this.access.execute((level, blockPos) -> {
            BlockState blockstate = level.getBlockState(blockPos);
            if (!player.getAbilities().instabuild && blockstate.is(BlockTags.ANVIL) && player.getRandom().nextFloat() < breakChance) {
                BlockState damageResult = TieredAnvilBlock.tieredDamage(blockstate); // only modification from super method
                if (damageResult == null) {
                    level.removeBlock(blockPos, false);
                    level.levelEvent(ANVIL_DESTROY_EVENT_ID, blockPos, 0);
                } else {
                    level.setBlock(blockPos, damageResult, 2);
                    level.levelEvent(ANVIL_USE_EVENT_ID, blockPos, 0);
                }
            } else {
                level.levelEvent(ANVIL_USE_EVENT_ID, blockPos, 0);
            }

        });
    }

    @Override
    protected boolean mayPickup(Player player, boolean b) {
        return (player.getAbilities().instabuild || player.experienceLevel >= this.getCost()); // allow any cost value instead of only those greater than 0
    }

    public ContainerLevelAccess getAccess(){
        return this.access;
    }

    @Override
    public void clicked(int i, int i1, ClickType clickType, Player player) {
        try{
            super.clicked(i, i1, clickType, player);
        } catch (ReportedException e){
            CrashReport report = e.getReport();
            CrashReportCategory clickInfoCategory = report.addCategory("Custom click info");
            clickInfoCategory.setDetail("Custom Menu Type", () -> this.customMenuType != null ? String.valueOf(ForgeRegistries.CONTAINERS.getKey(this.customMenuType)) : "<no type>");
            throw e;
        }
    }
}
