package com.animalcounter.parsers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ArgumentParserTest {

    @Test
    public void should_produce_valid_configs() {

        Map<String, String> expectedConfigs = Map.of(
                "pathToAnimals", "resources/animals.csv",
                "pathToRules", "resources/rules.txt"
        );

        Map<String, String> actualConfigs = ArgumentParser.getConfigs(
                "-animals", "resources/animals.csv",
                "-rules", "resources/rules.txt"
        );

        Assertions.assertThat(
                expectedConfigs
        ).containsExactlyInAnyOrderEntriesOf(
                actualConfigs
        );

    }

    @Test
    public void should_throw_exception_on_unknown_argument() {

        org.junit.jupiter.api.Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> ArgumentParser.getConfigs(
                    "-animals", "resources/animals.csv",
                    "-rules", "resources/rules.txt",
                    "-nonExistingArgument", "some/path.txt"
                ));

    }
}
