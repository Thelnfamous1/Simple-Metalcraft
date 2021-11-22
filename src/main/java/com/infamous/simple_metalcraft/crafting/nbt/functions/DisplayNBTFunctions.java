package com.infamous.simple_metalcraft.crafting.nbt.functions;

import com.google.gson.JsonParseException;
import com.infamous.simple_metalcraft.crafting.nbt.NBTFunction;
import com.infamous.simple_metalcraft.crafting.nbt.NBTOperator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DisplayNBTFunctions {

    public static final int MAX_LIGHT = 255;

    private DisplayNBTFunctions(){
        throw new IllegalStateException("Utility class");
    }

    public static final String COLOR_TAG_NAME = ItemStack.TAG_COLOR;
    public static final String LORE_TAG_NAME = ItemStack.TAG_LORE;
    public static final String NAME_TAG_NAME = ItemStack.TAG_LORE;
    public static final NBTFunction APPEND_TO_BASE
            = (baseTag, additiveTag) ->
            {
                makeDisplayTagsIfNeeded(baseTag, additiveTag);

                CompoundTag baseDisplay = baseTag.getCompound(NBTOperator.DISPLAY_TAG_NAME);
                CompoundTag additiveDisplay = additiveTag.getCompound(NBTOperator.DISPLAY_TAG_NAME);

                combineNames(baseDisplay, additiveDisplay, true);
                combineColors(baseDisplay, additiveDisplay, true);
                combineLore(baseDisplay, additiveDisplay, true);

                return baseTag;
            };

    public static final NBTFunction MERGE_TO_BASE
            = (baseTag, additiveTag) ->
            {
                makeDisplayTagsIfNeeded(baseTag, additiveTag);

                CompoundTag baseDisplay = baseTag.getCompound(NBTOperator.DISPLAY_TAG_NAME);
                CompoundTag additiveDisplay = additiveTag.getCompound(NBTOperator.DISPLAY_TAG_NAME);

                combineNames(baseDisplay, additiveDisplay, false);
                combineColors(baseDisplay, additiveDisplay, false);
                combineLore(baseDisplay, additiveDisplay, false);
                return baseTag;
            };

    public static final NBTFunction REPLACE_WITH_ADDITIVE
            = (baseTag, additiveTag) ->
            {
                makeDisplayTagsIfNeeded(baseTag, additiveTag);

                CompoundTag additiveDisplay = additiveTag.getCompound(NBTOperator.DISPLAY_TAG_NAME);

                baseTag.put(NBTOperator.DISPLAY_TAG_NAME, additiveDisplay);

                return baseTag;
            };

    private static void makeDisplayTagsIfNeeded(CompoundTag baseTag, CompoundTag additiveTag) {
        if(!baseTag.contains(NBTOperator.DISPLAY_TAG_NAME, 10)){
            baseTag.put(NBTOperator.DISPLAY_TAG_NAME, new CompoundTag());
        }
        if(!additiveTag.contains(NBTOperator.DISPLAY_TAG_NAME, 10)){
            additiveTag.put(NBTOperator.DISPLAY_TAG_NAME, new CompoundTag());
        }
    }

    private static void combineNames(CompoundTag baseDisplay, CompoundTag additiveDisplay, boolean addTogether) {
        List<MutableComponent> namesList = new ArrayList<>();
        tryAddNameToList(baseDisplay, namesList, true);
        tryAddNameToList(additiveDisplay, namesList, addTogether);
        if(!namesList.isEmpty()){
            MutableComponent newNameComponent = namesList.get(0);
            for(int i = 1; i < namesList.size(); i++){
                newNameComponent.append(namesList.get(i));
            }
            writeNameComponent(baseDisplay, newNameComponent);
        }
    }

    private static void tryAddNameToList(CompoundTag displayTag, List<MutableComponent> namesList, boolean addTogether) {
        if (displayTag.contains(NAME_TAG_NAME, 8)) {
            Component nameComponent = readNameComponent(displayTag);
            if (nameComponent instanceof MutableComponent mutableComponent) {
                if(!namesList.contains(nameComponent) || addTogether){
                    namesList.add(mutableComponent);
                }
            }
        }
    }

    //TODO: Average out in HSL color space instead of through RGB to get more intuitive colors
    private static void combineColors(CompoundTag baseDisplay, CompoundTag additiveDisplay, boolean addTogether) {
        List<Color> colorList = new ArrayList<>();
        tryAddColorToList(baseDisplay, colorList);
        tryAddColorToList(additiveDisplay, colorList);

        if(!colorList.isEmpty()){
            int newColor;
            if(addTogether){
                Color sumColor = addColors(colorList.toArray(new Color[0]));
                newColor = sumColor.getRGB();
            } else{
                Color averageColor = averageColors(colorList.toArray(new Color[0]));
                newColor = averageColor.getRGB();
            }

            baseDisplay.putInt(COLOR_TAG_NAME, newColor);
        }
    }

    private static void tryAddColorToList(CompoundTag displayTag, List<Color> colorList) {
        if (displayTag.contains(COLOR_TAG_NAME, 99)) {
            int rgb = displayTag.getInt(COLOR_TAG_NAME);
            Color baseColorObj = new Color(rgb);
            colorList.add(baseColorObj);
        }
    }

    private static Color addColors(Color... colors){
        int red = Math.min(sum(buildIntValueArray(Color::getRed, colors)), MAX_LIGHT);
        int green = Math.min(sum(buildIntValueArray(Color::getGreen, colors)), MAX_LIGHT);
        int blue = Math.min(sum(buildIntValueArray(Color::getBlue, colors)), MAX_LIGHT);
        return new Color(red, green, blue);
    }

    private static Color averageColors(Color... colors){
        int red = Math.min(average(buildIntValueArray(Color::getRed, colors)), MAX_LIGHT);
        int green = Math.min(average(buildIntValueArray(Color::getGreen, colors)), MAX_LIGHT);
        int blue = Math.min(average(buildIntValueArray(Color::getBlue, colors)), MAX_LIGHT);
        return new Color(red, green, blue);
    }

    private static int[] buildIntValueArray(Function<Color, Integer> getter, Color... colors){
        int[] values = new int[colors.length];
        for(int i = 0; i < colors.length; i++){
            values[i] = getter.apply(colors[i]);
        }
        return values;
    }

    private static int average(int... values) {
       return sum(values) / values.length;
    }

    private static int sum(int... values){
        int sum = 0;
        for(int value : values){
            sum += value;
        }
        return sum;
    }

    private static void combineLore(CompoundTag baseDisplay, CompoundTag additiveDisplay, boolean addTogether) {
        ListTag combinedLoreList = new ListTag();
        if (baseDisplay.getTagType(LORE_TAG_NAME) == 9) {
            ListTag baseLoreList = baseDisplay.getList(LORE_TAG_NAME, 8);
            combinedLoreList.addAll(baseLoreList);
        }
        if (additiveDisplay.getTagType(LORE_TAG_NAME) == 9) {
            ListTag additiveLoreList = additiveDisplay.getList(LORE_TAG_NAME, 8);
            if(addTogether){
                combinedLoreList.addAll(additiveLoreList);
            } else{ // avoiding duplicates of existing lore
                for(Tag tag : additiveLoreList){
                    if(!combinedLoreList.contains(tag)){
                        combinedLoreList.add(tag);
                    }
                }
            }
        }
        if(!combinedLoreList.isEmpty()){
            baseDisplay.put(LORE_TAG_NAME, combinedLoreList);
        }
    }

    @Nullable
    private static Component readNameComponent(CompoundTag displayTag) {
        Component nameComponent = null;
        try {
            nameComponent = Component.Serializer.fromJson(displayTag.getString(NAME_TAG_NAME));
            //displayTag.remove(NAME_TAG_NAME);
        } catch (JsonParseException jsonparseexception) {
            //displayTag.remove(NAME_TAG_NAME);
        }
        return nameComponent;
    }

    private static void writeNameComponent(CompoundTag displayTag, Component component){
        displayTag.putString(NAME_TAG_NAME, Component.Serializer.toJson(component));
    }
}
