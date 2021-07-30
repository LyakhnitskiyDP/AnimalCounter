package com.animalcounter.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    private Set<String> characteristics;

    public boolean hasCharacteristic(String characteristic) {
        return this.characteristics.contains(characteristic);
    }

    public boolean lacksCharacteristic(String characteristic) {
        return ! this.hasCharacteristic(characteristic);
    }

}
