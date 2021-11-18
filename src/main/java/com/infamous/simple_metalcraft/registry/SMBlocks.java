package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.blooming.BloomeryBlock;
import com.infamous.simple_metalcraft.crafting.casting.CastingTableBlock;
import com.infamous.simple_metalcraft.crafting.forging.ForgingTableBlock;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

public class SMBlocks {

    private SMBlocks(){};

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleMetalcraft.MOD_ID);

    public static final RegistryObject<Block> TIN_ORE = BLOCKS.register("tin_ore",
            SMBlocks::buildMetalOreBlock);

    public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = BLOCKS.register("deepslate_tin_ore",
            SMBlocks::buildDeepslateMetalOreBlock);

    public static final RegistryObject<Block> NETHER_PIG_IRON_ORE = BLOCKS.register("nether_pig_iron_ore",
            SMBlocks::buildNetherOreBlock);

    public static final RegistryObject<Block> CASTING_TABLE = BLOCKS.register("casting_table",
            () -> new CastingTableBlock(craftingTableProperties()));


    public static final RegistryObject<Block> FORGING_TABLE = BLOCKS.register("forging_table",
            () -> new ForgingTableBlock(craftingTableProperties()));


    public static final RegistryObject<Block> BLOOMERY = BLOCKS.register("bloomery",
            () -> new BloomeryBlock(furnaceProperties()));

    private static BlockBehaviour.Properties furnaceProperties(){
        return BlockBehaviour.Properties.of(Material.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F)
                .lightLevel(litBlockEmission(13));
    }

    private static ToIntFunction<BlockState> litBlockEmission(int levelToEmit) {
        return (blockState) -> blockState.getValue(BlockStateProperties.LIT) ? levelToEmit : 0;
    }

    private static BlockBehaviour.Properties craftingTableProperties() {
        return BlockBehaviour.Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD);
    }
    private static OreBlock buildMetalOreBlock() {
        return new OreBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE));
    }

    private static OreBlock buildDeepslateMetalOreBlock() {
        return new OreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE));
    }

    private static OreBlock buildNetherOreBlock(){
        return new OreBlock(
                BlockBehaviour.Properties.of(Material.STONE, MaterialColor.NETHER)
                        .requiresCorrectToolForDrops()
                        .strength(3.0F, 3.0F)
                        .sound(SoundType.NETHER_GOLD_ORE),
                UniformInt.of(0, 1));
    }
}
