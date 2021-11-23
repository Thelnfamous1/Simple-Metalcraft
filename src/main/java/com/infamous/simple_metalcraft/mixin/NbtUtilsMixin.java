package com.infamous.simple_metalcraft.mixin;

import net.minecraft.nbt.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(NbtUtils.class)
public class NbtUtilsMixin {

    @Inject(at = @At("HEAD"), method = "loadUUID", cancellable = true)
    private static void allowForListNbt(Tag uuidTag, CallbackInfoReturnable<UUID> cir){
        if (uuidTag.getType() == ListTag.TYPE){
            List<Integer> intList = ((ListTag)uuidTag)
                    .stream()
                    .filter(subTag -> subTag.getType() == IntTag.TYPE)
                    .map(IntTag.class::cast)
                    .map(IntTag::getAsInt)
                    .collect(Collectors.toList());
            if (intList.size() != 4) {
                throw new IllegalArgumentException("Expected UUID-List to be of length 4, but found " + intList.size() + ".");
            } else {
                cir.setReturnValue(uuidFromList(intList));
            }
        }
    }

    private static UUID uuidFromList(List<Integer> intList) {
        return new UUID((long)intList.get(0) << 32 | (long)intList.get(1) & 4294967295L, (long)intList.get(2) << 32 | (long)intList.get(3) & 4294967295L);
    }

}
