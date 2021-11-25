package com.infamous.simple_metalcraft.capability;

import com.infamous.simple_metalcraft.SimpleMetalcraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EquipmentCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<EquipmentCapability> EQUIPMENT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final String WAS_EQUIPPED_TAG_NAME = "wasEquipped";
    public static final ResourceLocation LOCATION = new ResourceLocation(SimpleMetalcraft.MOD_ID, "equipment");

    private final LazyOptional<EquipmentCapability> instance = LazyOptional.of(EquipmentCapability.Impl::new);

    public static void register(RegisterCapabilitiesEvent event) {
        event.register(EquipmentCapability.class);
    }

    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Mob) {
            event.addCapability(LOCATION, new EquipmentCapabilityProvider());
        }
    }

    public static LazyOptional<EquipmentCapability> get(Entity entity)
    {
        return entity.getCapability(EQUIPMENT_CAPABILITY);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == EQUIPMENT_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        EquipmentCapability instance = this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(WAS_EQUIPPED_TAG_NAME, instance.getWasEquipped());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        EquipmentCapability instance = this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!"));
        instance.setWasEquipped(nbt.getBoolean(WAS_EQUIPPED_TAG_NAME));
    }
}