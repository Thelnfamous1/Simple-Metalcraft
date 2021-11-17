package com.infamous.simple_metalcraft.crafting.blooming;

import com.infamous.simple_metalcraft.SMModEvents;
import com.infamous.simple_metalcraft.registry.SMItems;
import com.infamous.simple_metalcraft.registry.SMRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.*;

public class BloomingRecipe extends AbstractCookingRecipe {
    private static final Random RANDOM = new Random();
    protected final Map<ItemStack, Integer> weightedResults;
    protected final Map<ItemStack, UniformInt> ranges;
    protected final WeightedRandomList<WeightedEntry.Wrapper<ItemStack>> resultsAsWRL;
    protected final ItemStack cachedBestResult;

    public BloomingRecipe(ResourceLocation id, String group, Ingredient ingredient, Map<ItemStack, Integer> weightedResults, Map<ItemStack, UniformInt> ranges, float experience, int cookingTime) {
        super(SMModEvents.BLOOMING, id, group, ingredient, ItemStack.EMPTY, experience, cookingTime);
        this.weightedResults = weightedResults;
        this.ranges = ranges;
        this.resultsAsWRL = buildWeightedResultsAsWRL(weightedResults);
        this.cachedBestResult = getBestResult(weightedResults, ranges);
    }

    private static WeightedRandomList<WeightedEntry.Wrapper<ItemStack>> buildWeightedResultsAsWRL(Map<ItemStack, Integer> results) {
        List<WeightedEntry.Wrapper<ItemStack>> weightedEntries = new ArrayList<>();
        for(ItemStack stack : results.keySet()){
            WeightedEntry.Wrapper<ItemStack> wrapper = WeightedEntry.wrap(stack, results.get(stack));
            weightedEntries.add(wrapper);
        }
        return WeightedRandomList.create(weightedEntries);
    }

    public Ingredient getIngredient(){
        return this.ingredient;
    }

    public Map<ItemStack, Integer> getWeightedResults() {
        return this.weightedResults;
    }

    /*
        Only would have been called in AbstractFurnaceBlockEntity.canBurn & AbstractFurnaceBlockEntity.burn
     */
    @Override
    public ItemStack assemble(Container container) {
        return this.cachedBestResult.copy();
    }

    /*
        Mostly used in the client for displaying, also used in RecipeManager#getRecipesFor for sorting
     */
    @Override
    public ItemStack getResultItem() {
        return this.cachedBestResult;
    }

    /*
        Use this when calculating an ItemStack result for an active Bloomery.
        Make use sparingly, caching whenever possible.
     */
    public ItemStack assembleRandomResult() {
        Optional<WeightedEntry.Wrapper<ItemStack>> randomResultWrapper = this.resultsAsWRL.getRandom(RANDOM);
        ItemStack randomResult = randomResultWrapper.map(WeightedEntry.Wrapper::getData).orElse(this.cachedBestResult);
        if(randomResult != cachedBestResult && this.ranges.containsKey(randomResult)){
            UniformInt uniformInt = this.ranges.get(randomResult);
            randomResult = randomResult.copy();
            randomResult.setCount(uniformInt.sample(RANDOM));
        }
        return randomResult.copy();
    }

    private static ItemStack getBestResult(Map<ItemStack, Integer> results, Map<ItemStack, UniformInt> ranges){
        int highestWeight = 0;
        ItemStack result = ItemStack.EMPTY;
        for(ItemStack itemStack : results.keySet()){
            int weight = results.get(itemStack);
            if(weight > highestWeight){
                highestWeight = weight;
                result = itemStack;
            }
        }
        if(ranges.containsKey(result)){
            UniformInt uniformInt = ranges.get(result);
            int mean = (uniformInt.getMinValue() + uniformInt.getMaxValue()) / 2;
            result = result.copy();
            result.setCount(mean);
        }
        return result;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(SMItems.BLOOMERY.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SMRecipes.BLOOMING.get();
    }

    public Map<ItemStack, UniformInt> getRanges() {
        return this.ranges;
    }
}
