package com.animalcounter.parsers;

import com.animalcounter.entities.Animal;
import org.junit.jupiter.api.*;

import java.util.Set;
import java.util.function.Predicate;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RuleParserTest {

    @Test
    public void should_work_with_logical_and() {

        Animal animalToTest = new Animal(Set.of(
                "ЛЕГКОЕ",
                "МАЛЕНЬКОЕ",
                "ВСЕЯДНОЕ"
        ));

        Predicate<Animal> truthyPredicate = RuleParser.getPredicate(
                "ЛЕГКОЕ && МАЛЕНЬКОЕ && ВСЕЯДНОЕ"
        );

        Predicate<Animal> falsePredicate = RuleParser.getPredicate(
                "ЛЕГКОЕ && НЕВЫСОКОЕ && ВСЕЯДНОЕ"
        );

        Assertions.assertTrue(truthyPredicate.test(animalToTest));
        Assertions.assertFalse(falsePredicate.test(animalToTest));
    }

    @Test
    public void should_work_with_logical_or() {

        Animal animalToTest = new Animal(Set.of(
            "ТЯЖЕЛОЕ",
            "МАЛЕНЬКОЕ",
            "ТРАВОЯДНОЕ"
        ));

        Predicate<Animal> truthyPredicate = RuleParser.getPredicate(
                "ТЯЖЕЛОЕ || НЕВЫСОКОЕ"
        );

        Predicate<Animal> falsePredicate = RuleParser.getPredicate(
                "ЛЕГКОЕ || НЕВЫСОКОЕ"
        );

        Assertions.assertTrue(truthyPredicate.test(animalToTest));
        Assertions.assertFalse(falsePredicate.test(animalToTest));
    }

    @Test
    public void should_work_with_logical_not() {

        Animal animalToTest = new Animal(Set.of(
                "ЛЕГКОЕ",
                "НЕВЫСОКОЕ",
                "ВСЕЯДНОЕ"
        ));

        Predicate<Animal> truthyPredicate = RuleParser.getPredicate(
                "!ТЯЖЕЛОЕ && !МАЛЕНЬКОЕ && !ПЛОТОЯДНОЕ"
        );

        Predicate<Animal> falsePredicate = RuleParser.getPredicate(
                "!ЛЕГКОЕ && !НЕВЫСОКОЕ && !ВСЕЯДНОЕ"
        );

        Assertions.assertTrue(truthyPredicate.test(animalToTest));
        Assertions.assertFalse(falsePredicate.test(animalToTest));
    }

    @Nested
    class complex_expressions {

        @Test
        public void should_work_in_combination() {

            Animal animalToTest = new Animal(Set.of(
                    "ТЯЖЕЛОЕ",
                    "ВЫСОКОЕ",
                    "ВСЕЯДНОЕ"
            ));

            Predicate<Animal> truthyPredicate = RuleParser.getPredicate(
                    "СРЕДНЕЕ || ТЯЖЕЛОЕ && !МАЛЕНЬКОЕ && ВСЕЯДНОЕ"
            );

            Predicate<Animal> falsePredicate = RuleParser.getPredicate(
                    "!ТЯЖЕЛОЕ && ВЫСОКОЕ || НЕВЫСОКОЕ"
            );

            Assertions.assertTrue(truthyPredicate.test(animalToTest));
            Assertions.assertFalse(falsePredicate.test(animalToTest));
        }

    }

}
