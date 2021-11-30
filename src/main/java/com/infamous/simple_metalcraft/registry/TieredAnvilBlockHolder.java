package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.crafting.anvil.AnvilTier;
import com.infamous.simple_metalcraft.crafting.anvil.TieredAnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TieredAnvilBlockHolder {

    private final String name;
    private final RegistryObject<TieredAnvilBlock> anvil;
    private final RegistryObject<TieredAnvilBlock> chippedAnvil;
    private final RegistryObject<TieredAnvilBlock> damagedAnvil;

    public TieredAnvilBlockHolder(DeferredRegister<Block> blockRegister, AnvilTier anvilTier){
        this.name = anvilTier.getName();
        this.anvil = blockRegister.register(this.name + "_anvil", () -> buildAnvil(anvilTier));
        this.chippedAnvil = blockRegister.register("chipped_" + this.name + "_anvil", () -> buildAnvil(anvilTier));
        this.damagedAnvil = blockRegister.register("damaged_" + this.name + "_anvil", () -> buildAnvil(anvilTier));
    }

    public String getName() {
        return this.name;
    }

    public RegistryObject<TieredAnvilBlock> getAnvil() {
        return this.anvil;
    }

    public RegistryObject<TieredAnvilBlock> getChippedAnvil() {
        return this.chippedAnvil;
    }

    public RegistryObject<TieredAnvilBlock> getDamagedAnvil() {
        return this.damagedAnvil;
    }

    private TieredAnvilBlock buildAnvil(AnvilTier anvilTier) {
        return new TieredAnvilBlock(
                anvilTier,
                BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL)
                );
    }
}
