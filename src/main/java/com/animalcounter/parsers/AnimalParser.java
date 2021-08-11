package com.animalcounter.parsers;

import com.animalcounter.entities.Animal;

public class AnimalParser {

    public static final String COMMA_DELIMITER = ",";

    public static Animal parseString(String str) {
        return new Animal(str.split(COMMA_DELIMITER));
    }

}
