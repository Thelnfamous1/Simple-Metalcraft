package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.MetalworkingMenu;
import com.infamous.simple_metalcraft.crafting.blooming.BloomeryMenu;
import com.infamous.simple_metalcraft.crafting.casting.CastingMenu;
import com.infamous.simple_metalcraft.crafting.forging.ForgingMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMMenuTypes {

    public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SimpleMetalcraft.MOD_ID);

    public static RegistryObject<MenuType<MetalworkingMenu>> CASTING_TABLE = MENU_TYPES.register(
            "casting_table",
            () -> new MenuType<>(CastingMenu::new));

    public static RegistryObject<MenuType<MetalworkingMenu>> FORGING_TABLE = MENU_TYPES.register(
            "forging_table",
            () -> new MenuType<>(ForgingMenu::new));

    public static RegistryObject<MenuType<BloomeryMenu>> BLOOMERY = MENU_TYPES.register(
            "bloomery",
            () -> new MenuType<>(BloomeryMenu::new));
}
