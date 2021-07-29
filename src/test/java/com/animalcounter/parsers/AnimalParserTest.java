package com.animalcounter.parsers;

import com.animalcounter.entities.Animal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AnimalParserTest {

    @Test
    public void should_parse_csv_string() {

        String csv = "ЛЕГКОЕ,МАЛЕНЬКОЕ,ВСЕЯДНОЕ";

        Animal animalToTest = AnimalParser.parseString(csv);

        Animal expectedAnimal = new Animal(Set.of(
                "ЛЕГКОЕ",
                "МАЛЕНЬКОЕ",
                "ВСЕЯДНОЕ"
        ));

        Assertions.assertThat(
               expectedAnimal.getCharacteristics()
        ).containsExactlyInAnyOrderElementsOf(
                animalToTest.getCharacteristics()
        );
    }


}
