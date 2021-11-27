package com.infamous.simple_metalcraft.mixin;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.registry.SMItems;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {

    private static final String FORGING_COST_LOCALIZATION = "container." + SimpleMetalcraft.MOD_ID + ".forging.cost";

    public AnvilScreenMixin(AnvilMenu p_98901_, Inventory p_98902_, Component p_98903_, ResourceLocation p_98904_) {
        super(p_98901_, p_98902_, p_98903_, p_98904_);
    }

    @ModifyVariable(at = @At(value = "STORE", ordinal = 2), ordinal = 0, method = "renderLabels")
    private Component getForgingComponent(Component defaultValue){
        int cost = this.menu.getCost();
        ItemStack left = this.menu.getSlot(0).getItem();
        ItemStack right = this.menu.getSlot(1).getItem();
        boolean isForgeable = left.is(SMItems.IRON_BLOOM.get()) || left.is(SMItems.BLISTER_STEEL_INGOT.get());
        if(isForgeable && right.is(Items.FIRE_CHARGE)){
            return new TranslatableComponent(FORGING_COST_LOCALIZATION, cost);
        } else{
            return defaultValue;
        }
    }
}
