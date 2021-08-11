package com.animalcounter.entities;

import lombok.Data;
import lombok.ToString;

import java.util.Arrays;

@Data
@ToString
public class Animal {

    private String[] characteristics;


    public Animal(String...characteristics) {

        this.characteristics = characteristics;
    }

    public boolean hasCharacteristic(String characteristicToCheck) {
        return Arrays.asList(characteristics).contains(characteristicToCheck);
    }

    public boolean lacksCharacteristic(String characteristic) {
        return ! this.hasCharacteristic(characteristic);
    }

}
