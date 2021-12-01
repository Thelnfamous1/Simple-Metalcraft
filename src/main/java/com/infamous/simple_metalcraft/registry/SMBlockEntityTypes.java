package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.furnace.blasting.SMBlastFurnaceBlockEntity;
import com.infamous.simple_metalcraft.crafting.furnace.blooming.BloomeryBlockEntity;
import com.infamous.simple_metalcraft.crafting.furnace.cementation.CementationFurnaceBlockEntity;
import com.infamous.simple_metalcraft.crafting.furnace.smelter.SmelterBlockEntity;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMBlockEntityTypes {

    private SMBlockEntityTypes(){};

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SimpleMetalcraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<SmelterBlockEntity>> SMELTER = BLOCK_ENTITIES.register(
            "smelter", () -> BlockEntityType.Builder.of(SmelterBlockEntity::new, SMBlocks.SMELTER.get()).build(buildType("smelter"))
    );

    public static final RegistryObject<BlockEntityType<BloomeryBlockEntity>> BLOOMERY = BLOCK_ENTITIES.register(
      "bloomery", () -> BlockEntityType.Builder.of(BloomeryBlockEntity::new, SMBlocks.BLOOMERY.get()).build(buildType("bloomery"))
    );

    public static final RegistryObject<BlockEntityType<SMBlastFurnaceBlockEntity>> BLAST_FURNACE = BLOCK_ENTITIES.register(
            "blast_furnace", () -> BlockEntityType.Builder.of(SMBlastFurnaceBlockEntity::new, SMBlocks.BLAST_FURNACE.get()).build(buildType("blast_furnace"))
    );

    public static final RegistryObject<BlockEntityType<CementationFurnaceBlockEntity>> CEMENTATION_FURNACE = BLOCK_ENTITIES.register(
            "cementation_furnace", () -> BlockEntityType.Builder.of(CementationFurnaceBlockEntity::new, SMBlocks.CEMENTATION_FURNACE.get()).build(buildType("cementation_furnace"))
    );

    private static Type<?> buildType(String name){
        return Util.fetchChoiceType(References.BLOCK_ENTITY, new ResourceLocation(SimpleMetalcraft.MOD_ID, name).toString());
    }

}
