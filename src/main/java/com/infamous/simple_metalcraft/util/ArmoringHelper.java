package com.infamous.simple_metalcraft.util;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.registry.SMItems;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.*;

public class ArmoringHelper {

    private static final Random RANDOM = new Random();

    public static final Item[] SORTED_SWORDS =
            {
                    Items.STONE_SWORD,
                    SMItems.COPPER_SWORD.get(),
                    SMItems.BRONZE_SWORD.get(),
                    Items.IRON_SWORD,
                    SMItems.METEORIC_IRON_SWORD.get(),
                    SMItems.STEEL_SWORD.get(),
                    Items.DIAMOND_SWORD
            };
    public static final Item[] SORTED_SHOVELS =
            {
                    Items.STONE_SHOVEL,
                    SMItems.COPPER_SHOVEL.get(),
                    SMItems.BRONZE_SHOVEL.get(),
                    Items.IRON_SHOVEL,
                    SMItems.METEORIC_IRON_SHOVEL.get(),
                    SMItems.STEEL_SHOVEL.get(),
                    Items.DIAMOND_SHOVEL
            };
    public static final Item[] SORTED_PICKAXES =
            {
                    Items.STONE_PICKAXE,
                    SMItems.COPPER_PICKAXE.get(),
                    SMItems.BRONZE_PICKAXE.get(),
                    Items.IRON_PICKAXE,
                    SMItems.METEORIC_IRON_PICKAXE.get(),
                    SMItems.STEEL_PICKAXE.get(),
                    Items.DIAMOND_PICKAXE
            };
    public static final Item[] SORTED_HOES =
            {
                    Items.STONE_HOE,
                    SMItems.COPPER_HOE.get(),
                    SMItems.BRONZE_HOE.get(),
                    Items.IRON_HOE,
                    SMItems.METEORIC_IRON_HOE.get(),
                    SMItems.STEEL_HOE.get(),
                    Items.DIAMOND_HOE
            };
    public static final Item[] SORTED_AXES =
            {
                    Items.STONE_AXE,
                    SMItems.COPPER_AXE.get(),
                    SMItems.BRONZE_AXE.get(),
                    Items.IRON_AXE,
                    SMItems.METEORIC_IRON_AXE.get(),
                    SMItems.STEEL_AXE.get(),
                    Items.DIAMOND_AXE
            };
    public static final Item[] SORTED_HELMETS =
            {
                    Items.LEATHER_HELMET,
                    SMItems.COPPER_HELMET.get(),
                    SMItems.BRONZE_HELMET.get(),
                    Items.IRON_HELMET,
                    SMItems.METEORIC_IRON_HELMET.get(),
                    SMItems.STEEL_HELMET.get(),
                    Items.DIAMOND_HELMET
            };
    public static final Item[] SORTED_CHESTPLATES =
            {
                    Items.LEATHER_CHESTPLATE,
                    SMItems.COPPER_CHESTPLATE.get(),
                    SMItems.BRONZE_CHESTPLATE.get(),
                    Items.IRON_CHESTPLATE,
                    SMItems.METEORIC_IRON_CHESTPLATE.get(),
                    SMItems.STEEL_CHESTPLATE.get(),
                    Items.DIAMOND_CHESTPLATE
            };
    public static final Item[] SORTED_LEGGINGS =
            {
                    Items.LEATHER_LEGGINGS,
                    SMItems.COPPER_LEGGINGS.get(),
                    SMItems.BRONZE_LEGGINGS.get(),
                    Items.IRON_LEGGINGS,
                    SMItems.METEORIC_IRON_LEGGINGS.get(),
                    SMItems.STEEL_LEGGINGS.get(),
                    Items.DIAMOND_LEGGINGS
            };

    public static final Item[] SORTED_BOOTS =
            {
                    Items.LEATHER_BOOTS,
                    SMItems.COPPER_BOOTS.get(),
                    SMItems.BRONZE_BOOTS.get(),
                    Items.IRON_BOOTS,
                    SMItems.METEORIC_IRON_BOOTS.get(),
                    SMItems.STEEL_BOOTS.get(),
                    Items.DIAMOND_BOOTS
            };
    public static final double TIER_UP_CHANCE = 0.25D;
    public static final float EQUIP_CHANCE = 0.15F;
    public static final float HARD_MODE_FINISH_EARLY_CHANCE = 0.1F;
    public static final float DEFAULT_FINISH_EARLY_CHANCE = 0.25F;

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

    public static boolean clearSpawnEquipment(LivingEntity mob){
        boolean equipArmor = mob.getType().is(SMTags.EQUIP_ARMOR);
        boolean equipWeapon = mob.getType().is((SMTags.EQUIP_WEAPON));

        if(!equipArmor && !equipWeapon){
            return false;
        }

        for(EquipmentSlot equipmentSlot : EquipmentSlot.values()){
            if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR
                    && equipArmor) {
                mob.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }
            else if (equipmentSlot == EquipmentSlot.MAINHAND
                    && equipWeapon) {
                mob.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }
        }
        return true;
    }

    public static void populateDefaultEquipmentEnchantments(LivingEntity mob, DifficultyInstance difficultyInstance) {
        float specialMultiplier = difficultyInstance.getSpecialMultiplier();
        enchantSpawnedWeapon(mob, specialMultiplier);

        for(EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            if (equipmentslot.getType() == EquipmentSlot.Type.ARMOR) {
                enchantSpawnedArmor(mob, specialMultiplier, equipmentslot);
            }
        }

    }

    public static void enchantSpawnedWeapon(LivingEntity mob, float specialMultiplier) {
        ItemStack mainHandStack = mob.getMainHandItem();
        if (!mainHandStack.isEmpty() && RANDOM.nextFloat() < 0.25F * specialMultiplier) {
            mob.setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(RANDOM, mainHandStack, (int)(5.0F + specialMultiplier * (float)RANDOM.nextInt(18)), false));
        }

    }

    public static void enchantSpawnedArmor(LivingEntity mob, float specialMultiplier, EquipmentSlot armorSlot) {
        ItemStack armorStack = mob.getItemBySlot(armorSlot);
        if (!armorStack.isEmpty() && RANDOM.nextFloat() < 0.5F * specialMultiplier) {
            mob.setItemSlot(armorSlot, EnchantmentHelper.enchantItem(RANDOM, armorStack, (int)(5.0F + specialMultiplier * (float)RANDOM.nextInt(18)), false));
        }

    }

    public static ItemStack chooseEquipment(EquipmentSlot slot, int tierIndex) {
        return switch (slot) {
            case HEAD -> new ItemStack(SORTED_HELMETS[tierIndex]);
            case CHEST -> new ItemStack(SORTED_CHESTPLATES[tierIndex]);
            case LEGS -> new ItemStack(SORTED_LEGGINGS[tierIndex]);
            case FEET -> new ItemStack(SORTED_BOOTS[tierIndex]);
            case MAINHAND -> new ItemStack(getRandomWeapon(tierIndex));
            default -> ItemStack.EMPTY;
        };
    }

    public static boolean populateDefaultEquipmentSlots(LivingEntity mob, DifficultyInstance instance) {
        boolean equipArmor = mob.getType().is(SMTags.EQUIP_ARMOR);
        boolean equipWeapon = mob.getType().is((SMTags.EQUIP_WEAPON));

        if(!equipArmor && !equipWeapon){
            return false;
        }

        if (RANDOM.nextFloat() < EQUIP_CHANCE * instance.getSpecialMultiplier()) {
            float finishEarlyChance = mob.level.getDifficulty() == Difficulty.HARD ? HARD_MODE_FINISH_EARLY_CHANCE : DEFAULT_FINISH_EARLY_CHANCE;

            int tierIndex = RANDOM.nextInt(2); // 0 or 1
            for(int tryCount = 1; tryCount <= 5; tryCount++){ // can go up to 5 or 6
                if(RANDOM.nextFloat() < TIER_UP_CHANCE){
                    ++tierIndex;
                }
            }

            boolean cannotFinishEarly = true;

            List<ItemStack> equippedItems = new ArrayList<>();
            List<EquipmentSlot> equipmentSlots = Arrays.asList(EquipmentSlot.values());
            Collections.shuffle(equipmentSlots);
            for(EquipmentSlot slot : equipmentSlots) {
                if ((slot.getType() == EquipmentSlot.Type.ARMOR && equipArmor)
                        || (slot == EquipmentSlot.MAINHAND && equipWeapon)) {
                    ItemStack currentStackInSlot = mob.getItemBySlot(slot);
                    if (!cannotFinishEarly && RANDOM.nextFloat() < finishEarlyChance) {
                        break;
                    }

                    cannotFinishEarly = false;
                    if (currentStackInSlot.isEmpty()) {
                        ItemStack chosenItem = chooseEquipment(slot, tierIndex);
                        if (!chosenItem.isEmpty()) {
                            mob.setItemSlot(slot, chosenItem);
                            equippedItems.add(chosenItem);
                        }
                    }
                }
            }
            if(!equippedItems.isEmpty()){
                SimpleMetalcraft.LOGGER.info("Equipped {} at {} with the following items: {}", mob.getType().toShortString(), mob.position(), Arrays.toString(equippedItems.toArray()));
            }
            return true;
        }
        return false;
    }
}
