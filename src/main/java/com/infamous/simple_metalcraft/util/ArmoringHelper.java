package com.infamous.simple_metalcraft.util;

import com.infamous.simple_metalcraft.registry.SMItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Random;

public class ArmoringHelper {

    private static final Random RANDOM = new Random();

    public static final Item[] SORTED_SWORDS =
            {
                    Items.STONE_SWORD,
                    SMItems.COPPER_SWORD.get(),
                    SMItems.BRONZE_SWORD.get(),
                    Items.IRON_SWORD,
                    SMItems.STEEL_SWORD.get(),
                    Items.DIAMOND_SWORD
            };
    public static final Item[] SORTED_SHOVELS =
            {
                    Items.STONE_SHOVEL,
                    SMItems.COPPER_SHOVEL.get(),
                    SMItems.BRONZE_SHOVEL.get(),
                    Items.IRON_SHOVEL,
                    SMItems.STEEL_SHOVEL.get(),
                    Items.DIAMOND_SHOVEL
            };
    public static final Item[] SORTED_PICKAXES =
            {
                    Items.STONE_PICKAXE,
                    SMItems.COPPER_PICKAXE.get(),
                    SMItems.BRONZE_PICKAXE.get(),
                    Items.IRON_PICKAXE,
                    SMItems.STEEL_PICKAXE.get(),
                    Items.DIAMOND_PICKAXE
            };
    public static final Item[] SORTED_HOES =
            {
                    Items.STONE_HOE,
                    SMItems.COPPER_HOE.get(),
                    SMItems.BRONZE_HOE.get(),
                    Items.IRON_HOE,
                    SMItems.STEEL_HOE.get(),
                    Items.DIAMOND_HOE
            };
    public static final Item[] SORTED_AXES =
            {
                    Items.STONE_AXE,
                    SMItems.COPPER_AXE.get(),
                    SMItems.BRONZE_AXE.get(),
                    Items.IRON_AXE,
                    SMItems.STEEL_AXE.get(),
                    Items.DIAMOND_AXE
            };
    public static final Item[] SORTED_HELMETS =
            {
                    Items.LEATHER_HELMET,
                    SMItems.COPPER_HELMET.get(),
                    SMItems.BRONZE_HELMET.get(),
                    Items.IRON_HELMET,
                    SMItems.STEEL_HELMET.get(),
                    Items.DIAMOND_HELMET
            };
    public static final Item[] SORTED_CHESTPLATES =
            {
                    Items.LEATHER_CHESTPLATE,
                    SMItems.COPPER_CHESTPLATE.get(),
                    SMItems.BRONZE_CHESTPLATE.get(),
                    Items.IRON_HELMET,
                    SMItems.STEEL_CHESTPLATE.get(),
                    Items.DIAMOND_CHESTPLATE
            };
    public static final Item[] SORTED_LEGGINGS =
            {
                    Items.LEATHER_LEGGINGS,
                    SMItems.COPPER_LEGGINGS.get(),
                    SMItems.BRONZE_LEGGINGS.get(),
                    Items.IRON_HELMET,
                    SMItems.STEEL_LEGGINGS.get(),
                    Items.DIAMOND_LEGGINGS
            };

    public static final Item[] SORTED_BOOTS =
            {
                    Items.LEATHER_BOOTS,
                    SMItems.COPPER_BOOTS.get(),
                    SMItems.BRONZE_BOOTS.get(),
                    Items.IRON_BOOTS,
                    SMItems.STEEL_BOOTS.get(),
                    Items.DIAMOND_BOOTS
            };

    private ArmoringHelper(){
        throw new IllegalStateException("Utility class");
    }


    public static Item getRandomWeapon(int index) {
        int weaponType = RANDOM.nextInt(5);
        if(weaponType == 0){
            return SORTED_AXES[index];
        } else if(weaponType == 1){
            return SORTED_HOES[index];
        } else if(weaponType == 2){
            return SORTED_PICKAXES[index];
        } else if(weaponType == 3){
            return SORTED_SHOVELS[index];
        } else {
            return SORTED_SWORDS[index];
        }
    }
}
