package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.anvil.AnvilTier;
import com.infamous.simple_metalcraft.crafting.furnace.cementation.CementationFurnaceBlock;
import com.infamous.simple_metalcraft.crafting.bellows.BellowsBlock;
import com.infamous.simple_metalcraft.crafting.furnace.blooming.BloomeryBlock;
import com.infamous.simple_metalcraft.crafting.furnace.blasting.SMBlastFurnaceBlock;
import com.infamous.simple_metalcraft.crafting.furnace.smelter.SmelterBlock;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.ToIntFunction;

public class SMBlocks {

    private SMBlocks(){};

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SimpleMetalcraft.MOD_ID);

    public static final RegistryObject<Block> TIN_ORE = BLOCKS.register("tin_ore",
            SMBlocks::buildMetalOreBlock);

    public static final RegistryObject<Block> DEEPSLATE_TIN_ORE = BLOCKS.register("deepslate_tin_ore",
            SMBlocks::buildDeepslateMetalOreBlock);

    public static final RegistryObject<Block> RAW_TIN_BLOCK = BLOCKS.register("raw_tin_block",
            SMBlocks::buildMetalOreBlock);

    public static final RegistryObject<Block> SUEVITE = BLOCKS.register("suevite",
            SMBlocks::buildObsidianLikeBlock);

    public static final RegistryObject<Block> METEORIC_IRON_ORE = BLOCKS.register("meteoric_iron_ore",
            SMBlocks::buildMetalOreBlock);

    public static final RegistryObject<Block> RAW_METEORIC_IRON_BLOCK = BLOCKS.register("raw_meteoric_iron_block",
            SMBlocks::buildMetalOreBlock);

    public static final RegistryObject<Block> BRONZE_BLOCK = BLOCKS.register("bronze_block",
            SMBlocks::buildMetalBlock);
    public static final RegistryObject<Block> CUT_BRONZE = BLOCKS.register("cut_bronze",
            SMBlocks::buildMetalBlock);
    public static final RegistryObject<Block> METEORIC_IRON_BLOCK = BLOCKS.register("meteoric_iron_block",
            SMBlocks::buildMetalBlock);
    public static final RegistryObject<Block> PIG_IRON_BLOCK = BLOCKS.register("pig_iron_block",
            SMBlocks::buildMetalBlock);
    public static final RegistryObject<Block> BLISTER_STEEL_BLOCK = BLOCKS.register("blister_steel_block",
            SMBlocks::buildMetalBlock);
    public static final RegistryObject<Block> STEEL_BLOCK = BLOCKS.register("steel_block",
            SMBlocks::buildMetalBlock);

    public static final RegistryObject<Block> SMELTER = BLOCKS.register("smelter",
            () -> new SmelterBlock(furnaceProperties()));

    public static final RegistryObject<Block> BLOOMERY = BLOCKS.register("bloomery",
            () -> new BloomeryBlock(furnaceProperties()));

    public static final RegistryObject<Block> BLAST_FURNACE = BLOCKS.register("blast_furnace",
            () -> new SMBlastFurnaceBlock(furnaceProperties()));

    public static final RegistryObject<Block> CEMENTATION_FURNACE = BLOCKS.register("cementation_furnace",
            () -> new CementationFurnaceBlock(furnaceProperties()));

    public static final RegistryObject<Block> BELLOWS = BLOCKS.register("bellows",
            () -> new BellowsBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5F).sound(SoundType.WOOD).noOcclusion()));

    public static final TieredAnvilBlockHolder STONE_ANVILS = new TieredAnvilBlockHolder(BLOCKS, AnvilTier.Impl.STONE);
    public static final TieredAnvilBlockHolder COPPER_ANVILS = new TieredAnvilBlockHolder(BLOCKS, AnvilTier.Impl.COPPER);
    public static final TieredAnvilBlockHolder BRONZE_ANVILS = new TieredAnvilBlockHolder(BLOCKS, AnvilTier.Impl.BRONZE);
    public static final TieredAnvilBlockHolder IRON_ANVILS = new TieredAnvilBlockHolder(BLOCKS, AnvilTier.Impl.IRON);
    public static final TieredAnvilBlockHolder METEORIC_IRON_ANVILS = new TieredAnvilBlockHolder(BLOCKS, AnvilTier.Impl.METEORIC_IRON);
    public static final TieredAnvilBlockHolder STEEL_ANVILS = new TieredAnvilBlockHolder(BLOCKS, AnvilTier.Impl.STEEL);
    public static final TieredAnvilBlockHolder DIAMOND_ANVILS = new TieredAnvilBlockHolder(BLOCKS, AnvilTier.Impl.DIAMOND);
    public static final TieredAnvilBlockHolder NETHERITE_ANVILS = new TieredAnvilBlockHolder(BLOCKS, AnvilTier.Impl.NETHERITE);


    private static BlockBehaviour.Properties furnaceProperties(){
        return BlockBehaviour.Properties.of(Material.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F)
                .lightLevel(litBlockEmission(13));
    }

    private static ToIntFunction<BlockState> litBlockEmission(int levelToEmit) {
        return (blockState) -> blockState.getValue(BlockStateProperties.LIT) ? levelToEmit : 0;
    }

    private static OreBlock buildMetalOreBlock() {
        return new OreBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE));
    }

    private static OreBlock buildDeepslateMetalOreBlock() {
        return new OreBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE));
    }

    private static Block buildMetalBlock(){
        return new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL));
    }

    private static Block buildObsidianLikeBlock(){
        return new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F));
    }

}
