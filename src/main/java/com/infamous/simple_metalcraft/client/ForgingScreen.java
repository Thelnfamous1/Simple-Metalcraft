package com.infamous.simple_metalcraft.client;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.MetalworkingMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ForgingScreen extends MetalworkingScreen{
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(SimpleMetalcraft.MOD_ID, "textures/gui/container/forging_table.png");
    public static final String XP_COST = "container." + SimpleMetalcraft.MOD_ID + ".forging.cost";
    public static final String LEVEL_REQUIREMENT = "container." + SimpleMetalcraft.MOD_ID + ".forging.level.requirement";

    public ForgingScreen(MetalworkingMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected ResourceLocation getBgLocation() {
        return BG_LOCATION;
    }

    @Override
    protected TranslatableComponent getLevelRequirementComponent(int levelRequirement) {
        return new TranslatableComponent(LEVEL_REQUIREMENT, levelRequirement);
    }

    @Override
    protected TranslatableComponent getCostComponent(int xpCost) {
        return new TranslatableComponent(XP_COST, xpCost);
    }
}
