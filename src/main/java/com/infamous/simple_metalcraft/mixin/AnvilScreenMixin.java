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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {

    private static final String FORGING_COST_LOCALIZATION = SimpleMetalcraft.MOD_ID + "." + "container.forging.cost";

    public AnvilScreenMixin(AnvilMenu p_98901_, Inventory p_98902_, Component p_98903_, ResourceLocation p_98904_) {
        super(p_98901_, p_98902_, p_98903_, p_98904_);
    }

    @ModifyVariable(at = @At(value = "STORE", ordinal = 0), ordinal = 1, method = "renderLabels", print = true)
    private Component getForgingComponent(Component defaultValue){
        int cost = this.menu.getCost();
        ItemStack left = this.menu.getSlot(0).getItem();
        ItemStack right = this.menu.getSlot(1).getItem();
        boolean isForgeable = left.is(SMItems.PIG_IRON_INGOT.get()) || left.is(SMItems.BLISTER_STEEL_INGOT.get());
        if(isForgeable && right.isEmpty()){
            return new TranslatableComponent(FORGING_COST_LOCALIZATION, cost);
        } else{
            return defaultValue;
        }
    }
}
