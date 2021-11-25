package com.infamous.simple_metalcraft.mixin;

import com.infamous.simple_metalcraft.crafting.anvil.TieredAnvilBlock;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private static void customAnvilDamage(BlockState blockState, CallbackInfoReturnable<BlockState> cir){
        if(blockState.getBlock() instanceof TieredAnvilBlock tieredAnvil){
            cir.setReturnValue(TieredAnvilBlock.damage(blockState, tieredAnvil));
        }
    }
}
