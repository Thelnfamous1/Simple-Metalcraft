package com.infamous.simple_metalcraft;

import com.infamous.simple_metalcraft.capability.EquipmentCapability;
import com.infamous.simple_metalcraft.capability.EquipmentCapabilityProvider;
import com.infamous.simple_metalcraft.crafting.anvil.TieredAnvilBlock;
import com.infamous.simple_metalcraft.mixin.ItemCombinerMenuAccessor;
import com.infamous.simple_metalcraft.registry.SMItems;
import com.infamous.simple_metalcraft.util.ArmoringHelper;
import com.infamous.simple_metalcraft.util.VillagerTradesHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = SimpleMetalcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SMForgeEvents {

    public static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        EquipmentCapabilityProvider.attach(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAnvilUpdate(AnvilUpdateEvent event){
        if(event.isCanceled()) return;
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if(right.is(Items.FIRE_CHARGE) && right.getCount() >= left.getCount()){
            if(left.is(SMItems.PIG_IRON_INGOT.get())){
                event.setOutput(new ItemStack(Items.IRON_INGOT, left.getCount()));
                event.setCost(left.getCount());
                event.setMaterialCost(left.getCount());
            }
            else if(left.is(SMItems.BLISTER_STEEL_INGOT.get())){
                event.setOutput(new ItemStack(SMItems.STEEL_INGOT.get(), left.getCount()));
                event.setCost(left.getCount());
                event.setMaterialCost(left.getCount());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAnvilUpdate(AnvilRepairEvent event){
        Player player = event.getPlayer();
        AbstractContainerMenu menu = player.containerMenu;
        if(menu instanceof AnvilMenu anvilMenu){
            ContainerLevelAccess access = ((ItemCombinerMenuAccessor)anvilMenu).getAccess();
            access.execute((level, blockPos) -> {
                if(level.getBlockState(blockPos).getBlock() instanceof TieredAnvilBlock tieredAnvil){
                    event.setBreakChance(tieredAnvil.getAnvilTier().getBreakChance());
                }
            });

        }

        ItemStack left = event.getItemInput();
        ItemStack right = event.getIngredientInput();
        if(right.is(Items.FIRE_CHARGE)){
            if(left.is(SMItems.PIG_IRON_INGOT.get())){
            }
            else if(left.is(SMItems.BLISTER_STEEL_INGOT.get())){
            }
        }
    }


    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEntityJoinWorld(EntityJoinWorldEvent event){
        Level level = event.getWorld();
        if(!level.isClientSide){
            Entity entity = event.getEntity();
            if(entity instanceof Mob mob){
                LazyOptional<EquipmentCapability> equipmentCap = EquipmentCapabilityProvider.get(mob);
                equipmentCap.ifPresent(ec -> {
                    if(!ec.getWasEquipped()){
                        SimpleMetalcraft.LOGGER.info("Handling equipment for mob {}", mob);
                        DifficultyInstance difficultyAt = level.getCurrentDifficultyAt(mob.getOnPos());
                        boolean equipped = ArmoringHelper.populateDefaultEquipmentSlots(mob, difficultyAt, true);
                        if(equipped){
                            ArmoringHelper.populateDefaultEquipmentEnchantments(mob, difficultyAt);
                        }
                        ec.setWasEquipped(true);
                    }
                });
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoading(BiomeLoadingEvent event){
        BiomeGenerationSettingsBuilder builder = event.getGeneration();
        List<Supplier<ConfiguredFeature<?, ?>>> undergroundOreFeatures =
                builder.getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        ResourceLocation biomeName = event.getName();
        Biome.BiomeCategory category = event.getCategory();
        if(category != Biome.BiomeCategory.THEEND
                && category != Biome.BiomeCategory.NETHER){
            SimpleMetalcraft.LOGGER.info("Adding tin ore to biome: " + biomeName);
            undergroundOreFeatures.add(() -> SMModEvents.ORE_TIN);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onVillagerTrades(VillagerTradesEvent event){
        VillagerProfession profession = event.getType();

        if(profession != VillagerProfession.ARMORER
                && profession != VillagerProfession.WEAPONSMITH
                && profession != VillagerProfession.TOOLSMITH)
            return;

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
                                Optional<VillagerTrades.ItemListing> optionalReplacementTrade = buildReplacementTrade(tradeResult, profession);
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
                            SimpleMetalcraft.LOGGER.info("Failed to modify level {} trade for {}", levelIndex, profession);
                        }
                    }
                }
            }
        }
    }

        /*
            p_35633_.put(VillagerProfession.ARMORER, toIntMap(
                ImmutableMap.of(
                1, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.COAL, 15, 16, 2),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_LEGGINGS), 7, 1, 12, 1, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 1, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_HELMET), 5, 1, 12, 1, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 12, 1, 0.2F)
                    },
                2, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.IRON_INGOT, 4, 12, 10),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 5, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 5, 0.2F)
                    },
                3, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.LAVA_BUCKET, 1, 12, 20),
                    new VillagerTrades.EmeraldForItems(Items.DIAMOND, 1, 12, 20),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 10, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 10, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.SHIELD), 5, 1, 12, 10, 0.2F)
                    },
                4, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_LEGGINGS, 14, 3, 15, 0.2F),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_BOOTS, 8, 3, 15, 0.2F)
                    },
                5, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_HELMET, 8, 3, 30, 0.2F),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, 16, 3, 30, 0.2F)
                    }))
                );
            p_35633_.put(VillagerProfession.WEAPONSMITH, toIntMap(
                ImmutableMap.of(
                1, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.COAL, 15, 16, 2),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2F),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.IRON_SWORD, 2, 3, 1)
                    },
                2, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.IRON_INGOT, 4, 12, 10),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)
                    },
                3, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.FLINT, 24, 12, 20)
                    },
                4, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.DIAMOND, 1, 12, 30),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2F)
                    },
                5, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_SWORD, 8, 3, 30, 0.2F)
                    }))
                );
            p_35633_.put(VillagerProfession.TOOLSMITH, toIntMap(
                ImmutableMap.of(
                1, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.COAL, 15, 16, 2),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.STONE_AXE), 1, 1, 12, 1, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.STONE_HOE), 1, 1, 12, 1, 0.2F)
                    },
                2, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.IRON_INGOT, 4, 12, 10),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)
                    },
                3, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.FLINT, 30, 12, 20),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.IRON_AXE, 1, 3, 10, 0.2F),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.IRON_SHOVEL, 2, 3, 10, 0.2F),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.IRON_PICKAXE, 3, 3, 10, 0.2F),
                    new VillagerTrades.ItemsForEmeralds(new ItemStack(Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F)
                    },
                4, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EmeraldForItems(Items.DIAMOND, 1, 12, 30),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2F),
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_SHOVEL, 5, 3, 15, 0.2F)
                    },
                5, new VillagerTrades.ItemListing[]{
                    new VillagerTrades.EnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, 13, 3, 30, 0.2F)
                    }))
                );
         */

    private static Optional<VillagerTrades.ItemListing> buildReplacementTrade(ItemStack tradeResult, VillagerProfession profession) {
        Optional<VillagerTrades.ItemListing> replacementTrade = Optional.empty();

        if(tradeResult.is(Items.DIAMOND)){
            if(profession == VillagerProfession.ARMORER){
                replacementTrade = Optional.of(new VillagerTradesHelper.EmeraldForItems(SMItems.STEEL_INGOT.get(), 1, 12, 20));
            } else if(profession == VillagerProfession.WEAPONSMITH){
                replacementTrade = Optional.of(new VillagerTradesHelper.EmeraldForItems(SMItems.STEEL_INGOT.get(), 1, 12, 30));
            } else if(profession == VillagerProfession.TOOLSMITH){
                replacementTrade = Optional.of(new VillagerTradesHelper.EmeraldForItems(SMItems.STEEL_INGOT.get(), 1, 12, 30));

            }
        }

        else if(tradeResult.is(Items.IRON_BOOTS)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.IRON_CHESTPLATE)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.IRON_HELMET)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.IRON_LEGGINGS)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 3, 1, 12, 1, 0.2F));
        }

        else if(tradeResult.is(Items.CHAINMAIL_BOOTS)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 5, 0.2F));
        }
        else if(tradeResult.is(Items.CHAINMAIL_CHESTPLATE)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 12, 10, 0.2F));
        }
        else if(tradeResult.is(Items.CHAINMAIL_HELMET)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(Items.IRON_HELMET), 5, 1, 12, 10, 0.2F));
        }
        else if(tradeResult.is(Items.CHAINMAIL_LEGGINGS)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 7, 1, 12, 5, 0.2F));
        }

        else if(tradeResult.is(Items.DIAMOND_AXE)){
            replacementTrade = Optional.of(new VillagerTradesHelper.EnchantedItemForEmeralds(SMItems.STEEL_AXE.get(), 12, 3, 15, 0.2F));
        } else if(tradeResult.is(Items.DIAMOND_BOOTS)){
            replacementTrade = Optional.of(new VillagerTradesHelper.EnchantedItemForEmeralds(SMItems.STEEL_BOOTS.get(), 8, 3, 15, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_CHESTPLATE)){
            replacementTrade = Optional.of(new VillagerTradesHelper.EnchantedItemForEmeralds(SMItems.STEEL_CHESTPLATE.get(), 16, 3, 30, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_HELMET)){
            replacementTrade = Optional.of(new VillagerTradesHelper.EnchantedItemForEmeralds(SMItems.STEEL_HELMET.get(), 8, 3, 30, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_HOE)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(SMItems.STEEL_HOE.get()), 4, 1, 3, 10, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_LEGGINGS)){
            replacementTrade = Optional.of(new VillagerTradesHelper.EnchantedItemForEmeralds(SMItems.STEEL_LEGGINGS.get(), 14, 3, 15, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_PICKAXE)){
            replacementTrade = Optional.of(new VillagerTradesHelper.EnchantedItemForEmeralds(SMItems.STEEL_PICKAXE.get(), 13, 3, 30, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_SHOVEL)){
            replacementTrade = Optional.of(new VillagerTradesHelper.EnchantedItemForEmeralds(SMItems.STEEL_SHOVEL.get(), 5, 3, 15, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_SWORD)){
            replacementTrade = Optional.of(new VillagerTradesHelper.EnchantedItemForEmeralds(SMItems.STEEL_SWORD.get(), 8, 3, 30, 0.2F));
        }
        else if(tradeResult.is(Items.STONE_AXE)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(SMItems.COPPER_AXE.get()), 1, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.STONE_HOE)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(SMItems.COPPER_HOE.get()), 1, 1, 12, 1, 0.2F));
        }
        else if (tradeResult.is(Items.STONE_PICKAXE)) {
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(SMItems.COPPER_PICKAXE.get()), 1, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.STONE_SHOVEL)){
            replacementTrade = Optional.of(new VillagerTradesHelper.ItemsForEmeralds(new ItemStack(SMItems.COPPER_SHOVEL.get()), 1, 1, 12, 1, 0.2F));
        }

        return replacementTrade;
    }

}
