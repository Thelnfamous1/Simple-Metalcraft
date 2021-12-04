package com.infamous.simple_metalcraft.util;

import com.infamous.simple_metalcraft.registry.SMItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class VillagerTradesHelper {
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
    public static Optional<VillagerTrades.ItemListing> buildReplacementTrade(ItemStack tradeResult, VillagerProfession profession) {
        Optional<VillagerTrades.ItemListing> replacementTrade = Optional.empty();

        if(tradeResult.is(Items.DIAMOND)){
            if(profession == VillagerProfession.ARMORER){
                replacementTrade = Optional.of(new EmeraldForItems(SMItems.STEEL_INGOT.get(), 1, 12, 20));
            } else if(profession == VillagerProfession.WEAPONSMITH){
                replacementTrade = Optional.of(new EmeraldForItems(SMItems.STEEL_INGOT.get(), 1, 12, 30));
            } else if(profession == VillagerProfession.TOOLSMITH){
                replacementTrade = Optional.of(new EmeraldForItems(SMItems.STEEL_INGOT.get(), 1, 12, 30));

            }
        }

        else if(tradeResult.is(Items.IRON_BOOTS)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.IRON_CHESTPLATE)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.IRON_HELMET)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.IRON_LEGGINGS)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 3, 1, 12, 1, 0.2F));
        }

        else if(tradeResult.is(Items.CHAINMAIL_BOOTS)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 5, 0.2F));
        }
        else if(tradeResult.is(Items.CHAINMAIL_CHESTPLATE)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 12, 10, 0.2F));
        }
        else if(tradeResult.is(Items.CHAINMAIL_HELMET)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(Items.IRON_HELMET), 5, 1, 12, 10, 0.2F));
        }
        else if(tradeResult.is(Items.CHAINMAIL_LEGGINGS)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 7, 1, 12, 5, 0.2F));
        }

        else if(tradeResult.is(Items.DIAMOND_AXE)){
            replacementTrade = Optional.of(new EnchantedItemForEmeralds(SMItems.STEEL_AXE.get(), 12, 3, 15, 0.2F));
        } else if(tradeResult.is(Items.DIAMOND_BOOTS)){
            replacementTrade = Optional.of(new EnchantedItemForEmeralds(SMItems.STEEL_BOOTS.get(), 8, 3, 15, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_CHESTPLATE)){
            replacementTrade = Optional.of(new EnchantedItemForEmeralds(SMItems.STEEL_CHESTPLATE.get(), 16, 3, 30, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_HELMET)){
            replacementTrade = Optional.of(new EnchantedItemForEmeralds(SMItems.STEEL_HELMET.get(), 8, 3, 30, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_HOE)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(SMItems.STEEL_HOE.get()), 4, 1, 3, 10, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_LEGGINGS)){
            replacementTrade = Optional.of(new EnchantedItemForEmeralds(SMItems.STEEL_LEGGINGS.get(), 14, 3, 15, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_PICKAXE)){
            replacementTrade = Optional.of(new EnchantedItemForEmeralds(SMItems.STEEL_PICKAXE.get(), 13, 3, 30, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_SHOVEL)){
            replacementTrade = Optional.of(new EnchantedItemForEmeralds(SMItems.STEEL_SHOVEL.get(), 5, 3, 15, 0.2F));
        }
        else if(tradeResult.is(Items.DIAMOND_SWORD)){
            replacementTrade = Optional.of(new EnchantedItemForEmeralds(SMItems.STEEL_SWORD.get(), 8, 3, 30, 0.2F));
        }
        else if(tradeResult.is(Items.STONE_AXE)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(SMItems.COPPER_AXE.get()), 1, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.STONE_HOE)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(SMItems.COPPER_HOE.get()), 1, 1, 12, 1, 0.2F));
        }
        else if (tradeResult.is(Items.STONE_PICKAXE)) {
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(SMItems.COPPER_PICKAXE.get()), 1, 1, 12, 1, 0.2F));
        }
        else if(tradeResult.is(Items.STONE_SHOVEL)){
            replacementTrade = Optional.of(new ItemsForEmeralds(new ItemStack(SMItems.COPPER_SHOVEL.get()), 1, 1, 12, 1, 0.2F));
        }

        return replacementTrade;
    }

    public static class EmeraldForItems implements VillagerTrades.ItemListing {
        private final Item item;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public EmeraldForItems(ItemLike forEmerald, int cost, int maxUses, int villagerXp) {
            this.item = forEmerald.asItem();
            this.cost = cost;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity p_35662_, Random p_35663_) {
            ItemStack itemstack = new ItemStack(this.item, this.cost);
            return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class EnchantedItemForEmeralds implements VillagerTrades.ItemListing {
        private final ItemStack result;
        private final int baseEmeraldCost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public EnchantedItemForEmeralds(Item p_35693_, int p_35694_, int p_35695_, int p_35696_) {
            this(p_35693_, p_35694_, p_35695_, p_35696_, 0.05F);
        }

        public EnchantedItemForEmeralds(Item result, int baseCost, int maxUses, int villagerXp, float priceMultiplier) {
            this.result = new ItemStack(result);
            this.baseEmeraldCost = baseCost;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        public MerchantOffer getOffer(Entity p_35704_, Random p_35705_) {
            int i = 5 + p_35705_.nextInt(15);
            ItemStack itemstack = EnchantmentHelper.enchantItem(p_35705_, new ItemStack(this.result.getItem()), i, false);
            int j = Math.min(this.baseEmeraldCost + i, 64);
            ItemStack itemstack1 = new ItemStack(Items.EMERALD, j);
            return new MerchantOffer(itemstack1, itemstack, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    static class ItemsAndEmeraldsToItems implements VillagerTrades.ItemListing {
        private final ItemStack fromItem;
        private final int fromCount;
        private final int emeraldCost;
        private final ItemStack toItem;
        private final int toCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemsAndEmeraldsToItems(ItemLike p_35725_, int p_35726_, Item p_35727_, int p_35728_, int p_35729_, int p_35730_) {
            this(p_35725_, p_35726_, 1, p_35727_, p_35728_, p_35729_, p_35730_);
        }

        public ItemsAndEmeraldsToItems(ItemLike p_35717_, int p_35718_, int p_35719_, Item p_35720_, int p_35721_, int p_35722_, int p_35723_) {
            this.fromItem = new ItemStack(p_35717_);
            this.fromCount = p_35718_;
            this.emeraldCost = p_35719_;
            this.toItem = new ItemStack(p_35720_);
            this.toCount = p_35721_;
            this.maxUses = p_35722_;
            this.villagerXp = p_35723_;
            this.priceMultiplier = 0.05F;
        }

        @Nullable
        public MerchantOffer getOffer(Entity p_35732_, Random p_35733_) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

    public static class ItemsForEmeralds implements VillagerTrades.ItemListing {
        private final ItemStack result;
        private final int emeraldCost;
        private final int numberOfItems;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;

        public ItemsForEmeralds(Block p_35765_, int p_35766_, int p_35767_, int p_35768_, int p_35769_) {
            this(new ItemStack(p_35765_), p_35766_, p_35767_, p_35768_, p_35769_);
        }

        public ItemsForEmeralds(Item p_35741_, int p_35742_, int p_35743_, int p_35744_) {
            this(new ItemStack(p_35741_), p_35742_, p_35743_, 12, p_35744_);
        }

        public ItemsForEmeralds(Item p_35746_, int p_35747_, int p_35748_, int p_35749_, int p_35750_) {
            this(new ItemStack(p_35746_), p_35747_, p_35748_, p_35749_, p_35750_);
        }

        public ItemsForEmeralds(ItemStack p_35752_, int p_35753_, int p_35754_, int p_35755_, int p_35756_) {
            this(p_35752_, p_35753_, p_35754_, p_35755_, p_35756_, 0.05F);
        }

        public ItemsForEmeralds(ItemStack result, int cost, int resultAmount, int maxUses, int villagerXp, float priceMultiplier) {
            this.result = result;
            this.emeraldCost = cost;
            this.numberOfItems = resultAmount;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
            this.priceMultiplier = priceMultiplier;
        }

        public MerchantOffer getOffer(Entity p_35771_, Random p_35772_) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.result.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }

}
