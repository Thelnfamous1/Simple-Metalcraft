package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.blooming.BloomeryBlockEntity;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class SMBlockEntityTypes {

    private SMBlockEntityTypes(){};

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SimpleMetalcraft.MOD_ID);

    public static final RegistryObject<BlockEntityType<BloomeryBlockEntity>> BLOOMERY = BLOCK_ENTITIES.register(
      "bloomery", () -> BlockEntityType.Builder.of(BloomeryBlockEntity::new, SMBlocks.BLOOMERY.get()).build(buildType("bloomery"))
    );

    private static Type<?> buildType(String name){
        return Util.fetchChoiceType(References.BLOCK_ENTITY, new ResourceLocation(SimpleMetalcraft.MOD_ID, name).toString());
    }

}
