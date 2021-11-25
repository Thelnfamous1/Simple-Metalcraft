package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.blooming.BloomeryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMMenuTypes {

    public static DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SimpleMetalcraft.MOD_ID);

    public static RegistryObject<MenuType<BloomeryMenu>> BLOOMERY = MENU_TYPES.register(
            "bloomery",
            () -> new MenuType<>(BloomeryMenu::new));
}
