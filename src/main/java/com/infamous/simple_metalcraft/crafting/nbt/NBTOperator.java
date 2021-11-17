package com.infamous.simple_metalcraft.crafting.nbt;

import com.google.gson.JsonObject;
import com.infamous.simple_metalcraft.crafting.nbt.functions.EnchantmentNBTFunctions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.GsonHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NBTOperator {

    public static final NBTFunction NONE = ((baseTag, additiveTag) -> baseTag);
    public static final String ENCHANTMENTS_TAG_NAME = "Enchantments";
    private static final Map<String, Map<Operation, NBTFunction>> FUNCTIONS = new ConcurrentHashMap<>();

    public static final String JSON_OPERATION_LIST_KEY = "operations";
    public static final String JSON_TAG_KEY = "tag";
    public static final String JSON_OPERATION_KEY = "operation";

    private final Map<String, Operation> operations = new HashMap<>();

    static {
        buildEnchantmentsFunctions();
    }

    private static void buildEnchantmentsFunctions() {
        Map<Operation, NBTFunction> enchantmentFunctions = getOrCreateFunctions(ENCHANTMENTS_TAG_NAME);
        enchantmentFunctions.put(Operation.APPEND, EnchantmentNBTFunctions.APPEND_TO_BASE_ENCHANTMENTS);
        enchantmentFunctions.put(Operation.REPLACE, EnchantmentNBTFunctions.REPLACE_WITH_ADDITIVE_ENCHANTMENTS);
        enchantmentFunctions.put(Operation.MERGE, EnchantmentNBTFunctions.MERGE_TO_BASE_ENCHANTMENTS);
    }

    public static Map<Operation, NBTFunction> getOrCreateFunctions(String tagName){
        return FUNCTIONS.computeIfAbsent(tagName, key -> {
            Map<Operation, NBTFunction> operationMap = new ConcurrentHashMap<>();
            operationMap.put(Operation.REPLACE, NONE);
            operationMap.put(Operation.APPEND, NONE);
            operationMap.put(Operation.MERGE, NONE);
            operationMap.put(Operation.NONE, NONE);
            return operationMap;
        });
    }

    public NBTOperator(){
    }

    public static NBTOperator fromJson(JsonObject jsonObject){
        NBTOperator operator = new NBTOperator();
        GsonHelper.getAsJsonArray(jsonObject, JSON_OPERATION_LIST_KEY).forEach(
                jsonElement -> {
                    String tagName = GsonHelper.getAsString(jsonElement.getAsJsonObject(), JSON_TAG_KEY);
                    String operationName = GsonHelper.getAsString(jsonElement.getAsJsonObject(), JSON_OPERATION_KEY);
                    Operation operation = Operation.byName(operationName);
                    operator.setOperation(tagName, operation);
                }
        );
        return operator;
    }

    public NBTOperator withOperation(String tagName, Operation operation){
        this.setOperation(tagName, operation);
        return this;
    }

    public void setOperation(String tagName, Operation operation){
        this.operations.put(tagName, operation);
    }

    public Map<String, Operation> getOperations() {
        return this.operations;
    }

    public CompoundTag operate(CompoundTag baseTag, CompoundTag additiveTag) {

        for(Map.Entry<String, Operation> entry : this.operations.entrySet()){
            String tagName = entry.getKey();
            //SimpleMetalcraft.LOGGER.info("Checking operation for tag {}", tagName);
            Operation operation = entry.getValue();
            //SimpleMetalcraft.LOGGER.info("Operating on tag {} with operation {}", tagName, operation);
            getOrCreateFunctions(tagName).getOrDefault(operation, NONE).call(baseTag, additiveTag);
        }
        return baseTag;
    }

    public enum Operation{
        REPLACE("replace"),
        APPEND("add"),
        MERGE("merge"),
        NONE("none");

        private final String name;

        Operation(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Operation byName(String name){
            return switch (name.toLowerCase()) {
                case "replace" -> REPLACE;
                case "append" -> APPEND;
                case "merge" -> MERGE;
                default -> NONE;
            };
        }
    }
}
