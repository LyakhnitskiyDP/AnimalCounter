package com.animalcounter.parsers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ArgumentParserTest {


    @Test
    public void should_throw_exception_on_unknown_argument() {

        Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> ArgumentParser.getConfigs(
                    "-animals", "resources/animals.csv",
                    "-rules", "resources/rules.txt",
                    "-nonExistingArgument", "some/path.txt"
                ));

    }
}
