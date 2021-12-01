package com.infamous.simple_metalcraft.client;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.anvil.ForgingRecipe;
import com.infamous.simple_metalcraft.crafting.anvil.SMAnvilMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Optional;

public class SMAnvilScreen extends AnvilScreen {

    public static final int RED = 16736352;
    public static final int GREEN = 8453920;
    public static final String ENCHANTMENT_COST_LOCALIZATION = "container.repair.cost";
    private static final Component TOO_EXPENSIVE_TEXT = new TranslatableComponent("container.repair.expensive");
    private static final String REPAIR_COST_LOCALIZATION = "container." + SimpleMetalcraft.MOD_ID + ".repair.cost";
    public static final String FORGING_COST_LOCALIZATION = "container." + SimpleMetalcraft.MOD_ID + ".forging.cost";
    @Nullable
    private ForgingRecipe cachedForgingRecipe;

    public SMAnvilScreen(AnvilMenu anvilMenu, Inventory inventory, Component component) {
        super(anvilMenu, inventory, component);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int i1, int i2) {
        RenderSystem.disableBlend();
        this.baseRenderLabels(poseStack, i1, i2);
        int cost = this.menu.getCost();
        if (cost > 0) {
            int textColor = GREEN;
            Component component;
            if (cost >= SMAnvilMenu.MAX_COST && !this.minecraft.player.getAbilities().instabuild) {
                component = TOO_EXPENSIVE_TEXT;
                textColor = RED;
            } else if (!this.menu.getSlot(SMAnvilMenu.RESULT_SLOT).hasItem()) {
                component = null;
            } else {
                component = this.getCostText(cost);
                if (!this.menu.getSlot(SMAnvilMenu.RESULT_SLOT).mayPickup(this.minecraft.player)) {
                    textColor = RED;
                }
            }

            if (component != null) {
                int k = this.imageWidth - 8 - this.font.width(component) - 2;
                float l = 69.0F;
                fill(poseStack, k - 2, 67, this.imageWidth - 8, 79, 1325400064);
                this.font.drawShadow(poseStack, component, (float)k, l, textColor);
            }
        }

    }

    protected void baseRenderLabels(PoseStack poseStack, int i1, int i2) {
        this.font.draw(poseStack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(poseStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

    protected TranslatableComponent getCostText(int cost) {
        ItemStack ingredient = this.menu.getSlot(SMAnvilMenu.INGREDIENT_SLOT).getItem();
        ItemStack catalyst = this.menu.getSlot(SMAnvilMenu.CATALYST_SLOT).getItem();
        if(this.cachedForgingRecipe == null){
            Optional<ForgingRecipe> forgingRecipe = ForgingRecipe.getRecipeFor(ingredient, catalyst, this.minecraft.level);
            forgingRecipe.ifPresent(fr -> this.cachedForgingRecipe = fr);
        }

        if(this.cachedForgingRecipe != null && this.cachedForgingRecipe.matches(new SimpleContainer(ingredient, catalyst), this.minecraft.level)){
            return new TranslatableComponent(FORGING_COST_LOCALIZATION, cost);
        } else {
            this.cachedForgingRecipe = null;
            if(ingredient.isDamageableItem() && ingredient.getItem().isValidRepairItem(ingredient, catalyst)){
                return new TranslatableComponent(REPAIR_COST_LOCALIZATION, cost);
            } else {
                return new TranslatableComponent(ENCHANTMENT_COST_LOCALIZATION, cost);
            }
        }
    }
}
