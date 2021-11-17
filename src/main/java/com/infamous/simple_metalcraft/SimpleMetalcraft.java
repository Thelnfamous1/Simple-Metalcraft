package com.infamous.simple_metalcraft;

import com.infamous.simple_metalcraft.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SimpleMetalcraft.MOD_ID)
public class SimpleMetalcraft
{
    public static final String MOD_ID = "simple_metalcraft";
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public SimpleMetalcraft() {
        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        SMItems.ITEMS.register(modEventBus);
        SMBlocks.BLOCKS.register(modEventBus);
        SMRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        SMMenuTypes.MENU_TYPES.register(modEventBus);
        SMBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
}
