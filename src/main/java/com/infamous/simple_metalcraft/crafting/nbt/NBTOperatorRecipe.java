package com.infamous.simple_metalcraft.crafting.nbt;

import com.google.gson.JsonObject;
import com.infamous.simple_metalcraft.SimpleMetalcraft;
import com.infamous.simple_metalcraft.crafting.upgrading.NBTUpgradeRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Map;

public interface NBTOperatorRecipe {

    static <T extends Recipe<?> & NBTOperatorRecipe> void fromJson(JsonObject jsonObject, T recipe) {
        if(jsonObject.has(NBTOperator.JSON_OPERATION_LIST_KEY)){
            //SimpleMetalcraft.LOGGER.info("Reading operator for recipe {} from JSON", recipe.getId());
            NBTOperator operator = NBTOperator.fromJson(jsonObject);
            recipe.setOperator(operator);
        }
    }

    static <T extends Recipe<?> & NBTOperatorRecipe>  void fromNetwork(FriendlyByteBuf byteBuf, T recipe) {
        int operationCount = byteBuf.readVarInt();
        if(operationCount > 0){
            //SimpleMetalcraft.LOGGER.info("Reading operator for recipe {} from network", recipe.getId());
            NBTOperator operator = new NBTOperator();
            for(int i = 0; i < operationCount; i++){
                String tagName = byteBuf.readUtf();
                String operation = byteBuf.readUtf();
                operator.setOperation(tagName, NBTOperator.Operation.byName(operation));
            }
            recipe.setOperator(operator);
        }
    }

    static <T extends Recipe<?> & NBTOperatorRecipe>  void toNetwork(FriendlyByteBuf byteBuf, T recipe) {
        if(recipe.hasOperator()){
            //SimpleMetalcraft.LOGGER.info("Writing operator for recipe {} to network", recipe.getId());
            NBTOperator operator = recipe.getOperator();
            Map<String, NBTOperator.Operation> operations = operator.getOperations();
            int operationCount = operations.size();
            byteBuf.writeVarInt(operationCount);
            for(Map.Entry<String, NBTOperator.Operation> entry : operations.entrySet()){
                String tagName = entry.getKey();
                byteBuf.writeUtf(tagName);
                NBTOperator.Operation operation = entry.getValue();
                byteBuf.writeUtf(operation.getName());
            }
        }
    }

    boolean hasOperator();

    NBTOperator getOperator();

    void setOperator(NBTOperator operator);
}
