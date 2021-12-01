package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.anvil.SMAnvilMenu;
import com.infamous.simple_metalcraft.crafting.furnace.blasting.SMBlastFurnaceMenu;
import com.infamous.simple_metalcraft.crafting.furnace.blooming.BloomeryMenu;
import com.infamous.simple_metalcraft.crafting.furnace.cementation.CementationFurnaceMenu;
import com.infamous.simple_metalcraft.crafting.furnace.smelter.SmelterMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMMenuTypes {

    public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SimpleMetalcraft.MOD_ID);

    public static RegistryObject<MenuType<SmelterMenu>> SMELTER = MENU_TYPES.register(
            "smelter",
            () -> new MenuType<>(SmelterMenu::new));

    public static RegistryObject<MenuType<BloomeryMenu>> BLOOMERY = MENU_TYPES.register(
            "bloomery",
            () -> new MenuType<>(BloomeryMenu::new));

    public static RegistryObject<MenuType<SMBlastFurnaceMenu>> BLAST_FURNACE = MENU_TYPES.register(
            "blast_furnace",
            () -> new MenuType<>(SMBlastFurnaceMenu::new));

    public static RegistryObject<MenuType<CementationFurnaceMenu>> CEMENTATION_FURNACE = MENU_TYPES.register(
            "cementation_furnace",
            () -> new MenuType<>(CementationFurnaceMenu::new));

    public static RegistryObject<MenuType<SMAnvilMenu>> ANVIL = MENU_TYPES.register(
            "anvil",
            () -> new MenuType<>(SMAnvilMenu::new));
}
