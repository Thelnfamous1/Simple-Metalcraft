package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.batch.blooming.BloomeryMenu;
import com.infamous.simple_metalcraft.crafting.batch.cementation.CementationFurnaceMenu;
import com.infamous.simple_metalcraft.crafting.blasting.SMBlastFurnaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMMenuTypes {

    public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SimpleMetalcraft.MOD_ID);

    public static RegistryObject<MenuType<BloomeryMenu>> BLOOMERY = MENU_TYPES.register(
            "bloomery",
            () -> new MenuType<>(BloomeryMenu::new));

    public static RegistryObject<MenuType<SMBlastFurnaceMenu>> BLAST_FURNACE = MENU_TYPES.register(
            "blast_furnace",
            () -> new MenuType<>(SMBlastFurnaceMenu::new));

    public static RegistryObject<MenuType<CementationFurnaceMenu>> CEMENTATION_FURNACE = MENU_TYPES.register(
            "cementation_furnace",
            () -> new MenuType<>(CementationFurnaceMenu::new));
}
