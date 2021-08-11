package com.animalcounter.parsers;

import com.animalcounter.entities.Animal;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.function.Predicate;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RuleParserTest {

    @Test
    public void negation_should_work() {

        String traitBig = "BIG";
        String negatedTraitBig = RuleParser.LOGICAL_NOT + traitBig;


        Assertions.assertTrue(RuleParser.isNegated(negatedTraitBig));

        Animal bigAnimal = new Animal("BIG", "YELLOW");

        Assertions.assertTrue(
                RuleParser.getAtomicPredicate(traitBig).test(bigAnimal)
        );

        Assertions.assertFalse(
                RuleParser.getAtomicPredicate(negatedTraitBig).test(bigAnimal)
        );
    }

    @Test
    public void logical_or_should_work() {

        String rule = "BIG||YELLOW";

        Animal[] bigOrYellowAnimals = {
                new Animal("BIG"),
                new Animal("YELLOW"),
                new Animal("BIG", "YELLOW")
        };

        Arrays.stream(bigOrYellowAnimals)
              .forEach(
                      animal -> Assertions.assertTrue(
                              RuleParser.getOrPredicate(rule).test(animal)
                      )
              );

    }

    @Test
    public void should_work_with_logical_and() {

        Animal animalToTest = new Animal("ЛЕГКОЕ", "МАЛЕНЬКОЕ", "ВСЕЯДНОЕ");

        Predicate<Animal> truthyPredicate = RuleParser.getPredicate(
                "ЛЕГКОЕ&&МАЛЕНЬКОЕ&&ВСЕЯДНОЕ"
        );

        Predicate<Animal> falsePredicate = RuleParser.getPredicate(
                "ЛЕГКОЕ&&НЕВЫСОКОЕ&&ВСЕЯДНОЕ"
        );

        Assertions.assertTrue(truthyPredicate.test(animalToTest));
        Assertions.assertFalse(falsePredicate.test(animalToTest));
    }

    @Test
    public void should_work_with_logical_or() {

        Animal animalToTest = new Animal("ТЯЖЕЛОЕ", "МАЛЕНЬКОЕ", "ТРАВОЯДНОЕ");

        Predicate<Animal> truthyPredicate = RuleParser.getPredicate(
                "ТЯЖЕЛОЕ||НЕВЫСОКОЕ"
        );

        Predicate<Animal> falsePredicate = RuleParser.getPredicate(
                "ЛЕГКОЕ||НЕВЫСОКОЕ"
        );

        Assertions.assertTrue(truthyPredicate.test(animalToTest));
        Assertions.assertFalse(falsePredicate.test(animalToTest));
    }

    @Test
    public void should_work_with_logical_not() {

        Animal animalToTest = new Animal("ЛЕГКОЕ", "НЕВЫСОКОЕ", "ВСЕЯДНОЕ");

        Predicate<Animal> truthyPredicate = RuleParser.getPredicate(
                "!ТЯЖЕЛОЕ&&!МАЛЕНЬКОЕ&&!ПЛОТОЯДНОЕ"
        );

        Predicate<Animal> falsePredicate = RuleParser.getPredicate(
                "!ЛЕГКОЕ&&!НЕВЫСОКОЕ&&!ВСЕЯДНОЕ"
        );

        Assertions.assertTrue(truthyPredicate.test(animalToTest));
        Assertions.assertFalse(falsePredicate.test(animalToTest));
    }

    @Nested
    class complex_expressions {

        @Test
        public void should_work_in_combination() {

            Animal animalToTest = new Animal("ТЯЖЕЛОЕ", "ВЫСОКОЕ", "ВСЕЯДНОЕ");

            Predicate<Animal> truthyPredicate = RuleParser.getPredicate(
                    "СРЕДНЕЕ||ТЯЖЕЛОЕ&&!МАЛЕНЬКОЕ&&ВСЕЯДНОЕ"
            );

            Predicate<Animal> falsePredicate = RuleParser.getPredicate(
                    "!ТЯЖЕЛОЕ&&ВЫСОКОЕ||НЕВЫСОКОЕ"
            );

            Assertions.assertTrue(truthyPredicate.test(animalToTest));
            Assertions.assertFalse(falsePredicate.test(animalToTest));
        }

    }

}
