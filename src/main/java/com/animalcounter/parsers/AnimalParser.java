package com.animalcounter.parsers;

import com.animalcounter.entities.Animal;

import java.util.Set;

public class AnimalParser {

    public static final String COMMA_DELIMITER = ",";

    public static Animal parseString(String str) {
        return new Animal(Set.of(
            str.split(COMMA_DELIMITER)
        ));
    }

}
