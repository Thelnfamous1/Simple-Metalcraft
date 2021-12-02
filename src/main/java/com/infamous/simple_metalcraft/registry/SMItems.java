package com.infamous.simple_metalcraft.registry;

import com.infamous.simple_metalcraft.crafting.SmeltingByproductItem;
import com.infamous.simple_metalcraft.crafting.TieredShearsItem;
import com.infamous.simple_metalcraft.util.SMArmorMaterials;
import com.infamous.simple_metalcraft.util.SMTiers;
import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.BowDrillItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SMItems {

    private SMItems(){};

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SimpleMetalcraft.MOD_ID);

    // SPECIALTY

    public static final RegistryObject<Item> BOW_DRILL = ITEMS.register("bow_drill", () -> new BowDrillItem((new Item.Properties()).durability(16).tab(CreativeModeTab.TAB_TOOLS)));
    public static final RegistryObject<Item> CHERT = ITEMS.register("chert", SMItems::buildMaterial);
    public static final RegistryObject<Item> DROSS = ITEMS.register("dross", SMItems::buildSmeltingByproduct);

    // METALWORKING BLOCKS

    public static final RegistryObject<Item> SMELTER =
            ITEMS.register("smelter",
                    () -> buildDecorationBlock(SMBlocks.SMELTER.get()));

    public static final RegistryObject<Item> BLOOMERY =
            ITEMS.register("bloomery",
                    () -> buildDecorationBlock(SMBlocks.BLOOMERY.get()));

    public static final RegistryObject<Item> BLAST_FURNACE =
            ITEMS.register("blast_furnace",
                    () -> buildDecorationBlock(SMBlocks.BLAST_FURNACE.get()));

    public static final RegistryObject<Item> CEMENTATION_FURNACE =
            ITEMS.register("cementation_furnace",
                    () -> buildDecorationBlock(SMBlocks.CEMENTATION_FURNACE.get()));

    public static final RegistryObject<Item> BELLOWS =
            ITEMS.register("bellows",
                    () -> buildRedstoneBlock(SMBlocks.BELLOWS.get()));

    public static final TieredAnvilItemHolder STONE_ANVILS = new TieredAnvilItemHolder(ITEMS, SMBlocks.STONE_ANVILS);
    public static final TieredAnvilItemHolder COPPER_ANVILS = new TieredAnvilItemHolder(ITEMS, SMBlocks.COPPER_ANVILS);
    public static final TieredAnvilItemHolder BRONZE_ANVILS = new TieredAnvilItemHolder(ITEMS, SMBlocks.BRONZE_ANVILS);
    public static final TieredAnvilItemHolder IRON_ANVILS = new TieredAnvilItemHolder(ITEMS, SMBlocks.IRON_ANVILS);
    public static final TieredAnvilItemHolder METEORIC_IRON_ANVILS = new TieredAnvilItemHolder(ITEMS, SMBlocks.METEORIC_IRON_ANVILS);
    public static final TieredAnvilItemHolder STEEL_ANVILS = new TieredAnvilItemHolder(ITEMS, SMBlocks.STEEL_ANVILS);
    public static final TieredAnvilItemHolder DIAMOND_ANVILS = new TieredAnvilItemHolder(ITEMS, SMBlocks.DIAMOND_ANVILS);
    public static final TieredAnvilItemHolder NETHERITE_ANVILS = new TieredAnvilItemHolder(ITEMS, SMBlocks.NETHERITE_ANVILS);

    // TIN

    public static final RegistryObject<Item> TIN_ORE =
            ITEMS.register("tin_ore",
                    () -> buildBuildingBlock(SMBlocks.TIN_ORE.get()));

    public static final RegistryObject<Item> DEEPSLATE_TIN_ORE =
            ITEMS.register("deepslate_tin_ore",
                    () -> buildBuildingBlock(SMBlocks.DEEPSLATE_TIN_ORE.get()));

    public static final RegistryObject<Item> RAW_TIN =
            ITEMS.register("raw_tin", SMItems::buildMaterial);

    public static final RegistryObject<Item> TIN_NUGGET =
            ITEMS.register("tin_nugget", SMItems::buildMaterial);

    public static final RegistryObject<Item> TIN_INGOT =
            ITEMS.register("tin_ingot", SMItems::buildMaterial);

    // STONE
    public static final RegistryObject<Item> STONE_SHEARS =
            ITEMS.register("stone_shears", () -> buildTieredShears(Tiers.STONE));

    // COPPER
    public static final RegistryObject<Item> COPPER_NUGGET =
            ITEMS.register("copper_nugget", SMItems::buildMaterial);

    public static final RegistryObject<Item> COPPER_SHEARS =
            ITEMS.register("copper_shears", () -> buildTieredShears(SMTiers.COPPER));

    public static final RegistryObject<Item> COPPER_SWORD =
            ITEMS.register("copper_sword", () -> buildSword(SMTiers.COPPER, 3, -2.4F));

    public static final RegistryObject<Item> COPPER_SHOVEL =
            ITEMS.register("copper_shovel", () -> buildShovel(SMTiers.COPPER, 1.5F, -3.0F));

    public static final RegistryObject<Item> COPPER_PICKAXE =
            ITEMS.register("copper_pickaxe", () -> buildPickaxe(SMTiers.COPPER, 1, -2.8F));

    public static final RegistryObject<Item> COPPER_AXE =
            ITEMS.register("copper_axe", () -> buildAxe(SMTiers.COPPER, 6.0F, -3.2F));

    public static final RegistryObject<Item> COPPER_HOE =
            ITEMS.register("copper_hoe", () -> buildHoe(SMTiers.COPPER, 0, -2.0F));

    public static final RegistryObject<Item> COPPER_HELMET =
            ITEMS.register("copper_helmet", () -> buildHelmet(SMArmorMaterials.COPPER));

    public static final RegistryObject<Item> COPPER_CHESTPLATE =
            ITEMS.register("copper_chestplate", () -> buildChestplate(SMArmorMaterials.COPPER));

    public static final RegistryObject<Item> COPPER_LEGGINGS =
            ITEMS.register("copper_leggings", () -> buildLeggings(SMArmorMaterials.COPPER));

    public static final RegistryObject<Item> COPPER_BOOTS =
            ITEMS.register("copper_boots", () -> buildBoots(SMArmorMaterials.COPPER));

    // BRONZE

    public static final RegistryObject<Item> BRONZE_BLOCK = ITEMS.register("bronze_block",
            () -> buildBuildingBlock(SMBlocks.BRONZE_BLOCK.get()));

    public static final RegistryObject<Item> CUT_BRONZE = ITEMS.register("cut_bronze",
            () -> buildBuildingBlock(SMBlocks.CUT_BRONZE.get()));

    public static final RegistryObject<Item> BRONZE_NUGGET =
            ITEMS.register("bronze_nugget", SMItems::buildMaterial);

    public static final RegistryObject<Item> BRONZE_INGOT =
            ITEMS.register("bronze_ingot", SMItems::buildMaterial);

    public static final RegistryObject<Item> BRONZE_SHEARS =
            ITEMS.register("bronze_shears", () -> buildTieredShears(SMTiers.BRONZE));

    public static final RegistryObject<Item> BRONZE_SWORD =
            ITEMS.register("bronze_sword", () -> buildSword(SMTiers.BRONZE, 3, -2.4F));

    public static final RegistryObject<Item> BRONZE_SHOVEL =
            ITEMS.register("bronze_shovel", () -> buildShovel(SMTiers.BRONZE, 1.5F, -3.0F));

    public static final RegistryObject<Item> BRONZE_PICKAXE =
            ITEMS.register("bronze_pickaxe", () -> buildPickaxe(SMTiers.BRONZE, 1, -2.8F));

    public static final RegistryObject<Item> BRONZE_AXE =
            ITEMS.register("bronze_axe", () -> buildAxe(SMTiers.BRONZE, 6.0F, -3.1F));

    public static final RegistryObject<Item> BRONZE_HOE =
            ITEMS.register("bronze_hoe", () -> buildHoe(SMTiers.BRONZE, 0, -1.0F));

    public static final RegistryObject<Item> BRONZE_HELMET =
            ITEMS.register("bronze_helmet", () -> buildHelmet(SMArmorMaterials.BRONZE));

    public static final RegistryObject<Item> BRONZE_CHESTPLATE =
            ITEMS.register("bronze_chestplate", () -> buildChestplate(SMArmorMaterials.BRONZE));

    public static final RegistryObject<Item> BRONZE_LEGGINGS =
            ITEMS.register("bronze_leggings", () -> buildLeggings(SMArmorMaterials.BRONZE));

    public static final RegistryObject<Item> BRONZE_BOOTS =
            ITEMS.register("bronze_boots", () -> buildBoots(SMArmorMaterials.BRONZE));

    // IRON

    public static final RegistryObject<Item> IRON_BLOOM =
            ITEMS.register("iron_bloom", SMItems::buildMaterial);

    public static final RegistryObject<Item> IRON_SLAG =
            ITEMS.register("iron_slag", SMItems::buildSmeltingByproduct);

    public static final RegistryObject<Item> IRON_SHEARS =
            ITEMS.register("iron_shears", () -> buildTieredShears(Tiers.IRON));

    // METEORIC IRON

    public static final RegistryObject<Item> METEORIC_IRON_BLOCK = ITEMS.register("meteoric_iron_block",
            () -> buildBuildingBlock(SMBlocks.METEORIC_IRON_BLOCK.get()));

    public static final RegistryObject<Item> METEORIC_IRON_ORE = ITEMS.register("meteoric_iron_ore",
            () -> buildBuildingBlock(SMBlocks.METEORIC_IRON_ORE.get()));

    public static final RegistryObject<Item> RAW_METEORIC_IRON =
            ITEMS.register("raw_meteoric_iron", SMItems::buildMaterial);

    public static final RegistryObject<Item> METEORIC_IRON_NUGGET =
            ITEMS.register("meteoric_iron_nugget", SMItems::buildMaterial);

    public static final RegistryObject<Item> METEORIC_IRON_INGOT =
            ITEMS.register("meteoric_iron_ingot", SMItems::buildMaterial);

    public static final RegistryObject<Item> METEORIC_IRON_SHEARS =
            ITEMS.register("meteoric_iron_shears", () -> buildTieredShears(SMTiers.METEORIC_IRON));

    public static final RegistryObject<Item> METEORIC_IRON_SWORD =
            ITEMS.register("meteoric_iron_sword", () -> buildSword(SMTiers.METEORIC_IRON, 3, -2.4F));

    public static final RegistryObject<Item> METEORIC_IRON_SHOVEL =
            ITEMS.register("meteoric_iron_shovel", () -> buildShovel(SMTiers.METEORIC_IRON, 1.5F, -3.0F));

    public static final RegistryObject<Item> METEORIC_IRON_PICKAXE =
            ITEMS.register("meteoric_iron_pickaxe", () -> buildPickaxe(SMTiers.METEORIC_IRON, 1, -2.8F));

    public static final RegistryObject<Item> METEORIC_IRON_AXE =
            ITEMS.register("meteoric_iron_axe", () -> buildAxe(SMTiers.METEORIC_IRON, 6.0F, -3.1F));

    public static final RegistryObject<Item> METEORIC_IRON_HOE =
            ITEMS.register("meteoric_iron_hoe", () -> buildHoe(SMTiers.METEORIC_IRON, 0, -1.0F));

    public static final RegistryObject<Item> METEORIC_IRON_HELMET =
            ITEMS.register("meteoric_iron_helmet", () -> buildHelmet(SMArmorMaterials.METEORIC_IRON));

    public static final RegistryObject<Item> METEORIC_IRON_CHESTPLATE =
            ITEMS.register("meteoric_iron_chestplate", () -> buildChestplate(SMArmorMaterials.METEORIC_IRON));

    public static final RegistryObject<Item> METEORIC_IRON_LEGGINGS =
            ITEMS.register("meteoric_iron_leggings", () -> buildLeggings(SMArmorMaterials.METEORIC_IRON));

    public static final RegistryObject<Item> METEORIC_IRON_BOOTS =
            ITEMS.register("meteoric_iron_boots", () -> buildBoots(SMArmorMaterials.METEORIC_IRON));

    // PIG IRON

    public static final RegistryObject<Item> PIG_IRON_BLOCK = ITEMS.register("pig_iron_block",
            () -> buildBuildingBlock(SMBlocks.PIG_IRON_BLOCK.get()));

    public static final RegistryObject<Item> PIG_IRON_INGOT =
            ITEMS.register("pig_iron_ingot", SMItems::buildMaterial);

    // TURTLE

    public static final RegistryObject<Item> TURTLE_CHESTPLATE =
            ITEMS.register("turtle_chestplate", () -> buildChestplate(ArmorMaterials.TURTLE));

    public static final RegistryObject<Item> TURTLE_LEGGINGS =
            ITEMS.register("turtle_leggings", () -> buildLeggings(ArmorMaterials.TURTLE));

    public static final RegistryObject<Item> TURTLE_BOOTS =
            ITEMS.register("turtle_boots", () -> buildBoots(ArmorMaterials.TURTLE));

    // BLISTER STEEL

    public static final RegistryObject<Item> BLISTER_STEEL_BLOCK = ITEMS.register("blister_steel_block",
            () -> buildBuildingBlock(SMBlocks.BLISTER_STEEL_BLOCK.get()));

    public static final RegistryObject<Item> BLISTER_STEEL_INGOT =
            ITEMS.register("blister_steel_ingot", SMItems::buildMaterial);

    // STEEL

    public static final RegistryObject<Item> STEEL_BLOCK = ITEMS.register("steel_block",
            () -> buildBuildingBlock(SMBlocks.STEEL_BLOCK.get()));

    public static final RegistryObject<Item> STEEL_NUGGET =
            ITEMS.register("steel_nugget", SMItems::buildMaterial);

    public static final RegistryObject<Item> STEEL_INGOT =
            ITEMS.register("steel_ingot", SMItems::buildMaterial);

    public static final RegistryObject<Item> STEEL_SHEARS =
            ITEMS.register("steel_shears", () -> buildTieredShears(SMTiers.STEEL));

    public static final RegistryObject<Item> STEEL_SWORD =
            ITEMS.register("steel_sword", () -> buildSword(SMTiers.STEEL, 3, -2.4F));

    public static final RegistryObject<Item> STEEL_SHOVEL =
            ITEMS.register("steel_shovel", () -> buildShovel(SMTiers.STEEL, 1.5F, -3.0F));

    public static final RegistryObject<Item> STEEL_PICKAXE =
            ITEMS.register("steel_pickaxe", () -> buildPickaxe(SMTiers.STEEL, 1, -2.8F));

    public static final RegistryObject<Item> STEEL_AXE =
            ITEMS.register("steel_axe", () -> buildAxe(SMTiers.STEEL, 5.0F, -3.0F));

    public static final RegistryObject<Item> STEEL_HOE =
            ITEMS.register("steel_hoe", () -> buildHoe(SMTiers.STEEL, 0, 0.0F));

    public static final RegistryObject<Item> STEEL_HELMET =
            ITEMS.register("steel_helmet", () -> buildHelmet(SMArmorMaterials.STEEL));

    public static final RegistryObject<Item> STEEL_CHESTPLATE =
            ITEMS.register("steel_chestplate", () -> buildChestplate(SMArmorMaterials.STEEL));

    public static final RegistryObject<Item> STEEL_LEGGINGS =
            ITEMS.register("steel_leggings", () -> buildLeggings(SMArmorMaterials.STEEL));

    public static final RegistryObject<Item> STEEL_BOOTS =
            ITEMS.register("steel_boots", () -> buildBoots(SMArmorMaterials.STEEL));

    // DIAMOND

    public static final RegistryObject<Item> DIAMOND_SHEARS =
            ITEMS.register("diamond_shears", () -> buildTieredShears(Tiers.DIAMOND));


    // NETHERITE

    public static final RegistryObject<Item> NETHERITE_SHEARS =
            ITEMS.register("netherite_shears", () -> buildTieredShears(Tiers.NETHERITE));

    /*
        public static final Item IRON_SWORD = registerItem("iron_sword", new SwordItem(Tiers.IRON, 3, -2.4F, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
        public static final Item IRON_SHOVEL = registerItem("iron_shovel", new ShovelItem(Tiers.IRON, 1.5F, -3.0F, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS)));
        public static final Item IRON_PICKAXE = registerItem("iron_pickaxe", new PickaxeItem(Tiers.IRON, 1, -2.8F, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS)));
        public static final Item IRON_AXE = registerItem("iron_axe", new AxeItem(Tiers.IRON, 6.0F, -3.1F, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS)));
        public static final Item IRON_HOE = registerItem("iron_hoe", new HoeItem(Tiers.IRON, -2, -1.0F, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS)));
     */

    /*
        public static final Item IRON_HELMET = registerItem("iron_helmet", new ArmorItem(ArmorMaterials.IRON, EquipmentSlot.HEAD, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
        public static final Item IRON_CHESTPLATE = registerItem("iron_chestplate", new ArmorItem(ArmorMaterials.IRON, EquipmentSlot.CHEST, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
        public static final Item IRON_LEGGINGS = registerItem("iron_leggings", new ArmorItem(ArmorMaterials.IRON, EquipmentSlot.LEGS, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
        public static final Item IRON_BOOTS = registerItem("iron_boots", new ArmorItem(ArmorMaterials.IRON, EquipmentSlot.FEET, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
     */

    private static SwordItem buildSword(Tier tier, int baseAttackDamage, float attackSpeed){
        return new SwordItem(tier, baseAttackDamage, attackSpeed, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT));
    }

    private static ShovelItem buildShovel(Tier tier, float baseAttackDamage, float attackSpeed){
        return new ShovelItem(tier, baseAttackDamage, attackSpeed, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS));
    }

    private static PickaxeItem buildPickaxe(Tier tier, int baseAttackDamage, float attackSpeed){
        return new PickaxeItem(tier, baseAttackDamage, attackSpeed, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS));
    }

    private static AxeItem buildAxe(Tier tier, float baseAttackDamage, float attackSpeed){
        return new AxeItem(tier, baseAttackDamage, attackSpeed, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS));
    }

    private static HoeItem buildHoe(Tier tier, int baseAttackDamage, float attackSpeed){
        return new HoeItem(tier, baseAttackDamage, attackSpeed, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS));
    }

    private static ArmorItem buildHelmet(ArmorMaterial armorMaterial){
        return new ArmorItem(armorMaterial, EquipmentSlot.HEAD, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT));
    }

    private static ArmorItem buildChestplate(ArmorMaterial armorMaterial){
        return new ArmorItem(armorMaterial, EquipmentSlot.CHEST, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT));
    }

    private static ArmorItem buildLeggings(ArmorMaterial armorMaterial){
        return new ArmorItem(armorMaterial, EquipmentSlot.LEGS, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT));
    }

    private static ArmorItem buildBoots(ArmorMaterial armorMaterial){
        return new ArmorItem(armorMaterial, EquipmentSlot.FEET, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT));
    }

    private static Item buildMaterial(){
        return new Item((new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS));
    }

    private static Item buildSmeltingByproduct(){
        return new SmeltingByproductItem((new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS));
    }

    private static BlockItem buildBuildingBlock(Block block) {
        return new BlockItem(block, (new Item.Properties()).tab(CreativeModeTab.TAB_BUILDING_BLOCKS));
    }

    private static BlockItem buildDecorationBlock(Block block) {
        return new BlockItem(block, (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS));
    }

    private static BlockItem buildRedstoneBlock(Block block) {
        return new BlockItem(block, (new Item.Properties()).tab(CreativeModeTab.TAB_REDSTONE));
    }

    private static Item buildTieredShears(Tier tier) {
        return new TieredShearsItem(tier, (new Item.Properties()).tab(CreativeModeTab.TAB_TOOLS));
    }
}
