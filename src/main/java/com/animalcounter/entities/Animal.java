package com.animalcounter.entities;

import java.util.Arrays;

public record Animal(String...characteristics) {

    public String[] getCharacteristics() {
        return this.characteristics;
    }

    public boolean hasCharacteristic(String characteristicToCheck) {
        return Arrays.asList(characteristics).contains(characteristicToCheck);
    }

    public boolean lacksCharacteristic(String characteristic) {
        return ! this.hasCharacteristic(characteristic);
    }

}
