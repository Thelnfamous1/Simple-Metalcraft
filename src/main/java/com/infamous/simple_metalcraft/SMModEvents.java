package com.infamous.simple_metalcraft;

import com.infamous.simple_metalcraft.capability.EquipmentCapabilityProvider;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import com.infamous.simple_metalcraft.util.DispenserRegistration;
import com.infamous.simple_metalcraft.worldgen.*;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.Locale;

@Mod.EventBusSubscriber(modid = SimpleMetalcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SMModEvents {

    @SubscribeEvent
    public static void registerCaps(final RegisterCapabilitiesEvent event) {
        EquipmentCapabilityProvider.register(event);
    }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event){
        event.enqueueWork(
                () -> {
                    SMRecipes.Types.registerRecipeTypes();
                    OreRegistration.registerOreFeatures();
                    DispenserRegistration.registerDispenserBehavior();
                    StructureRegistration.registerStructurePieceTypes();
                    StructureRegistration.registerStructureFeatures();
                    StructureRegistration.addStructuresToMaps();
                }
        );
    }

}
