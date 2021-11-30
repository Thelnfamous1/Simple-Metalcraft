package com.infamous.simple_metalcraft.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TieredAnvilItemHolder {

    private final RegistryObject<BlockItem> anvil;
    private final RegistryObject<BlockItem> chippedAnvil;
    private final RegistryObject<BlockItem> damagedAnvil;

    public TieredAnvilItemHolder(DeferredRegister<Item> itemRegister, TieredAnvilBlockHolder tabh){
        this.anvil = itemRegister.register(tabh.getName() + "_anvil", () -> buildAnvilItem(tabh.getAnvil().get()));
        this.chippedAnvil = itemRegister.register("chipped_" + tabh.getName() + "_anvil", () -> buildAnvilItem(tabh.getChippedAnvil().get()));
        this.damagedAnvil = itemRegister.register("damaged_" + tabh.getName() + "_anvil", () -> buildAnvilItem(tabh.getDamagedAnvil().get()));
    }

    private static BlockItem buildAnvilItem(Block block) {
        return new BlockItem(block, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS));
    }

    public RegistryObject<BlockItem> getAnvil() {
        return this.anvil;
    }

    public RegistryObject<BlockItem> getChippedAnvil() {
        return this.chippedAnvil;
    }

    public RegistryObject<BlockItem> getDamagedAnvil() {
        return this.damagedAnvil;
    }
}
