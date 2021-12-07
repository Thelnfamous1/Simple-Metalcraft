package com.infamous.simple_metalcraft;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.infamous.simple_metalcraft.capability.EquipmentCapability;
import com.infamous.simple_metalcraft.capability.EquipmentCapabilityProvider;
import com.infamous.simple_metalcraft.crafting.anvil.ForgingRecipe;
import com.infamous.simple_metalcraft.crafting.anvil.SMAnvilMenu;
import com.infamous.simple_metalcraft.crafting.anvil.TieredAnvilBlock;
import com.infamous.simple_metalcraft.mixin.ChunkGeneratorAccessor;
import com.infamous.simple_metalcraft.mixin.StructureSettingsAccessor;
import com.infamous.simple_metalcraft.registry.SMStructures;
import com.infamous.simple_metalcraft.util.ArmoringHelper;
import com.infamous.simple_metalcraft.util.VillagerTradesHelper;
import com.infamous.simple_metalcraft.worldgen.OreRegistration;
import com.infamous.simple_metalcraft.worldgen.StructureRegistration;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SimpleMetalcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SMForgeEvents {

    public static final Random RANDOM = new Random();

    // Notes in this method originate from TelepathicGrunt's Structure Tutorial Mod
    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerLevel serverLevel){
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
            StructureSettings worldStructureConfig = chunkGenerator.getSettings();

            //////////// BIOME BASED STRUCTURE SPAWNING ////////////
            /*
             * NOTE: BiomeModifications from Fabric API does not work in 1.18 currently.
             * Instead, we will use the below to add our structure to overworld biomes.
             * Remember, this is temporary until Fabric API finds a better solution for adding structures to biomes.
             */

            // Create a mutable map we will use for easier adding to biomes
            HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> customStructToMultiMap = new HashMap<>();

            // Add the registrykey of all biomes that this Configured Structure can spawn in.
            for(Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : serverLevel.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).entrySet()) {
                Biome.BiomeCategory biomeCategory = biomeEntry.getValue().getBiomeCategory();
                // From Infamous: Here, we are mimicking how vanilla ruined portals are associated with biomes in a more general sense so that there's mod compat
                switch (biomeCategory){
                    case DESERT -> associateBiomeToConfiguredStructure(customStructToMultiMap, StructureRegistration.METEORITE_DESERT, biomeEntry.getKey());
                    case JUNGLE -> associateBiomeToConfiguredStructure(customStructToMultiMap, StructureRegistration.METEORITE_JUNGLE, biomeEntry.getKey());
                    case OCEAN -> associateBiomeToConfiguredStructure(customStructToMultiMap, StructureRegistration.METEORITE_OCEAN, biomeEntry.getKey());
                    case MOUNTAIN, EXTREME_HILLS, MESA -> associateBiomeToConfiguredStructure(customStructToMultiMap, StructureRegistration.METEORITE_MOUNTAIN, biomeEntry.getKey());
                    case THEEND -> associateBiomeToConfiguredStructure(customStructToMultiMap, StructureRegistration.METEORITE_END, biomeEntry.getKey());
                    case NETHER, NONE -> {} // We don't want meteorites spawning in the Nether or the Void
                    default -> associateBiomeToConfiguredStructure(customStructToMultiMap, StructureRegistration.METEORITE_STANDARD, biomeEntry.getKey());
                }
            }

            // Grab the map that holds what ConfigureStructures a structure has and what biomes it can spawn in.
            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            ((StructureSettingsAccessor)worldStructureConfig).simple_metalcraft_getConfiguredStructures().entrySet().forEach(tempStructureToMultiMap::put);

            // Add our structures to the structure map/multimap and set the world to use this combined map/multimap.
            customStructToMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));

            ((StructureSettingsAccessor)worldStructureConfig).simple_metalcraft_setConfiguredStructures(tempStructureToMultiMap.build());

            //////////// DIMENSION BASED STRUCTURE SPAWNING (OPTIONAL) ////////////
            /*
             * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
             * They will handle your structure spacing for your if you add to BuiltinRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
             */
            ResourceLocation chunkGeneratorResource = Registry.CHUNK_GENERATOR.getKey(((ChunkGeneratorAccessor)chunkGenerator).simple_metalcraft_getCodec());
            if(chunkGeneratorResource != null && chunkGeneratorResource.getNamespace().equals("terraforged")) return;

            /*
             * Prevent spawning our structure in Vanilla's superflat world as
             * people seem to want their superflat worlds free of modded structures.
             * Also that vanilla superflat is really tricky and buggy to work with in my experience.
             */
            if(chunkGenerator instanceof FlatLevelSource &&
                    serverLevel.dimension().equals(Level.OVERWORLD)){
                return;
            }

            /*
             * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
             *
             * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as BuiltinRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
             * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
             * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
             */
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureConfig.structureConfig());
            tempMap.putIfAbsent(SMStructures.METEORITE.get(), StructureSettings.DEFAULTS.get(SMStructures.METEORITE.get()));
            ((StructureSettingsAccessor)worldStructureConfig).simple_metalcraft_setStructureConfig(tempMap);
        }
    }

    // Notes in this method originate from TelepathicGrunt's Structure Tutorial Mod
    /**
     * Helper method that handles setting up the map to multimap relationship to help prevent issues.
     */
    private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> customStructureToMultiMap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
        customStructureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
        HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = customStructureToMultiMap.get(configuredStructureFeature.feature);
        if(configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
            SimpleMetalcraft.LOGGER.error("""
                    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                    This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
                    The two conflicting ConfiguredStructures are: {}, {}
                    The biome that is attempting to be shared: {}
                """,
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey()),
                    biomeRegistryKey
            );
        }
        else{
            configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
            SimpleMetalcraft.LOGGER.info("Associating {} to {}",
                    BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getKey(configuredStructureFeature),
                    biomeRegistryKey);
        }
    }

    @SubscribeEvent
    public static void onAttachEntityCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        EquipmentCapabilityProvider.attach(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAnvilUpdate(final AnvilUpdateEvent event){
        if(event.isCanceled()) return;
        ItemStack ingredient = event.getLeft();
        ItemStack catalyst = event.getRight();
        if(catalyst.getCount() < ingredient.getCount()) return; // need at least as many catalysts as ingredients

        Level level = event.getPlayer().level;
        Optional<ForgingRecipe> forgingRecipe = ForgingRecipe.getRecipeFor(ingredient, catalyst, level);

        forgingRecipe.ifPresent(fr -> {
            ItemStack result = fr.assemble(new SimpleContainer(ingredient, catalyst));
            int leftCount = ingredient.getCount();
            result.setCount(result.getCount() * leftCount); // grow the result count based on how many inputs used
            event.setOutput(result);
            event.setCost(leftCount * fr.getExperienceCost()); // experience level cost
            event.setMaterialCost(leftCount); // material cost
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAnvilRepair(final AnvilRepairEvent event){
        Player player = event.getPlayer();
        AbstractContainerMenu menu = player.containerMenu;
        if(menu instanceof SMAnvilMenu anvilMenu){
            ContainerLevelAccess access = anvilMenu.getAccess();
            access.execute((level, blockPos) -> {
                if(level.getBlockState(blockPos).getBlock() instanceof TieredAnvilBlock tieredAnvil){
                    event.setBreakChance(tieredAnvil.getAnvilTier().getBreakChance());
                }
            });

        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorld(final EntityJoinWorldEvent event){
        Level level = event.getWorld();
        if(!level.isClientSide){
            Entity entity = event.getEntity();
            BlockPos spawnPos = entity.blockPosition();
            if(entity instanceof Mob mob){
                //noinspection ConstantConditions
                level.getServer().addTickable(new TickTask(1, () ->
                        tryEquipMob(level, spawnPos, mob)));
            }
        }
    }

    private static void tryEquipMob(Level level, BlockPos spawnPos, Mob mob) {
        LazyOptional<EquipmentCapability> equipmentCap = EquipmentCapabilityProvider.get(mob);
        equipmentCap.ifPresent(ec -> {
            if(!ec.getWasEquipped()){
                if(ArmoringHelper.clearSpawnEquipment(mob)){
                    DifficultyInstance difficultyAt = level.getCurrentDifficultyAt(spawnPos);
                    boolean equipped = ArmoringHelper.populateDefaultEquipmentSlots(mob, difficultyAt);
                    if(equipped){
                        ArmoringHelper.populateDefaultEquipmentEnchantments(mob, difficultyAt);
                    }
                }
                ec.setWasEquipped(true);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoading(final BiomeLoadingEvent event){
        BiomeGenerationSettingsBuilder builder = event.getGeneration();
        List<Supplier<PlacedFeature>> undergroundOreFeatures =
                builder.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        ResourceLocation biomeName = event.getName();
        Biome.BiomeCategory category = event.getCategory();
        if(category != Biome.BiomeCategory.THEEND
                && category != Biome.BiomeCategory.NETHER){
            SimpleMetalcraft.LOGGER.info("Adding tin ore to biome: " + biomeName);
            undergroundOreFeatures.add(() -> OreRegistration.PLACED_ORE_TIN);
            undergroundOreFeatures.add(() -> OreRegistration.PLACED_ORE_TIN_LOWER);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onVillagerTrades(final VillagerTradesEvent event){
        VillagerProfession profession = event.getType();

        if(profession != VillagerProfession.ARMORER
                && profession != VillagerProfession.WEAPONSMITH
                && profession != VillagerProfession.TOOLSMITH)
            return; // We don't care about non-blacksmith professions

        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        if (trades != null && !trades.isEmpty()) {
            for(int levelIndex = 1; levelIndex <= 5; levelIndex++){
                List<VillagerTrades.ItemListing> tradesForLevel = trades.get(levelIndex);
                if (tradesForLevel != null) {
                    for(int tradeIndex = 0; tradeIndex < tradesForLevel.size(); tradeIndex++){
                        VillagerTrades.ItemListing trade = tradesForLevel.get(tradeIndex);
                        try{
                            /*
                                I know this looks strange, but we need to get the MerchantOffer
                                from the ItemListing so we can query the Item involved in the trade.
                                ItemListing#getOffer asks for an Entity and a Random, and we do not have
                                the ability to instantiate a dummy Entity to handle something like the
                                TreasureMapForEmeralds implementation of ItemListing which uses the Villager
                                involved with the trade to get a ServerLevel for Treasure Map creation.
                                If we somehow come across an implementation of ItemListing that actually
                                utilizes the Entity argument, we can just catch the inevitable NPE and
                                skip the trade manipulation check since we don't care - it's either a Treasure Map trade,
                                or a custom trade implementation from a mod that shouldn't be replaced anyway.
                                None of the vanilla trades apart from the Treasure Map trade utilize the Entity argument.
                             */
                            MerchantOffer merchantOffer = trade.getOffer(null, RANDOM);
                            if (merchantOffer != null) {
                                ItemStack tradeResult = merchantOffer.getResult();
                                Optional<VillagerTrades.ItemListing> optionalReplacementTrade = VillagerTradesHelper.buildReplacementTrade(tradeResult, profession);
                                if(optionalReplacementTrade.isPresent()){

                                    SimpleMetalcraft.LOGGER.info("Replacing level {} trade for {}: {} -> {}",
                                            levelIndex,
                                            profession,
                                            merchantOffer.getResult(),
                                            // Our own implementations of ItemListing do not utilize the Entity argument
                                            optionalReplacementTrade.get().getOffer(null, RANDOM).getResult()
                                    );

                                    tradesForLevel.set(tradeIndex, optionalReplacementTrade.get());
                                }
                            }
                        } catch (NullPointerException ignored){
                            // If ItemListing#getOffer fails, just make a note of it in the log.
                            SimpleMetalcraft.LOGGER.info("Failed to modify {} at index {} at level {} for {}", trade, tradeIndex, levelIndex, profession);
                        }
                    }
                }
            }
        }
    }
}
