package com.infamous.simple_metalcraft.capability;

public interface EquipmentCapability {

    boolean getWasEquipped();

    void setWasEquipped(boolean wasSpawned);

    class Impl implements EquipmentCapability {
        private boolean wasSpawned;


        @Override
        public boolean getWasEquipped() {
            return this.wasSpawned;
        }

        @Override
        public void setWasEquipped(boolean wasSpawned) {
            this.wasSpawned = wasSpawned;
        }
    }
}
