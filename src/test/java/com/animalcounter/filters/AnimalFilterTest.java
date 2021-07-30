package com.animalcounter.filters;

import com.animalcounter.entities.Animal;
import com.animalcounter.parsers.RuleParser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AnimalFilterTest {
    private static String[] WEIGHT = {"ЛЕГКОЕ", "СРЕДНЕЕ", "ТЯЖЕЛОЕ"};
    private static String[] HEIGHT = {"МАЛЕНЬКОЕ", "НЕВЫСОКОЕ", "ВЫСОКОЕ"};
    private static String[] TYPE = {"ТРАВОЯДНОЕ", "ПЛОТОЯДНОЕ", "ВСЕЯДНОЕ"};


    @Test
    public void should_return_9_9_9() {

        List<Animal> animals = new ArrayList<>();

        for (int i = 0; i < WEIGHT.length; i++) {
            for (int j = 0; j < HEIGHT.length; j++) {
                for (int z = 0; z < TYPE.length; z++) {
                    animals.add(
                            new Animal(Set.of(
                                    WEIGHT[i],
                                    HEIGHT[j],
                                    TYPE[z]
                            ))
                    );
                }
            }
        }

        String[] rules = {
                "ПЛОТОЯДНОЕ",
                "СРЕДНЕЕ",
                "ВЫСОКОЕ"
        };

        Map<String, Predicate<Animal>> animalPredicates = Map.of(
                rules[0], RuleParser.getPredicate(rules[0]),
                rules[1], RuleParser.getPredicate(rules[1]),
                rules[2], RuleParser.getPredicate(rules[2])
        );
        AnimalFilter animalFilter = new AnimalFilter(animals, animalPredicates);
        Map<String, Integer> resultMap = animalFilter.sortAnimals();

        Map<String, Integer> expectedMap = Map.of(
                rules[0], 9,
                rules[1], 9,
                rules[2], 9
        );

        Assertions.assertThat(
                resultMap
        ).containsExactlyInAnyOrderEntriesOf(
                expectedMap
        );
    }

    @Test
    public void should_return_18_18_18() {

        List<Animal> animals = new ArrayList<>();

        for (int i = 0; i < WEIGHT.length; i++) {
            for (int j = 0; j < HEIGHT.length; j++) {
                for (int z = 0; z < TYPE.length; z++) {
                    animals.add(
                            new Animal(Set.of(
                                    WEIGHT[i],
                                    HEIGHT[j],
                                    TYPE[z]
                            ))
                    );
                }
            }
        }

        String[] rules = {
                "ПЛОТОЯДНОЕ || ТРАВОЯДНОЕ",
                "СРЕДНЕЕ || ЛЕГКОЕ",
                "ВЫСОКОЕ || МАЛЕНЬКОЕ"
        };

        Map<String, Predicate<Animal>> animalPredicates = Map.of(
                rules[0], RuleParser.getPredicate(rules[0]),
                rules[1], RuleParser.getPredicate(rules[1]),
                rules[2], RuleParser.getPredicate(rules[2])
        );
        AnimalFilter animalFilter = new AnimalFilter(animals, animalPredicates);
        Map<String, Integer> resultMap = animalFilter.sortAnimals();

        Map<String, Integer> expectedMap = Map.of(
                rules[0], 18,
                rules[1], 18,
                rules[2], 18
        );

        Assertions.assertThat(
                resultMap
        ).containsExactlyInAnyOrderEntriesOf(
                expectedMap
        );
    }

    @Test
    public void should_return_9_3_9() {

        List<Animal> animals = new ArrayList<>();

        for (int i = 0; i < WEIGHT.length; i++) {
            for (int j = 0; j < HEIGHT.length; j++) {
                for (int z = 0; z < TYPE.length; z++) {
                    animals.add(
                            new Animal(Set.of(
                                    WEIGHT[i],
                                    HEIGHT[j],
                                    TYPE[z]
                            ))
                    );
                }
            }
        }

        System.out.println(animals);

        String[] rules = {
                "!ПЛОТОЯДНОЕ && !ТРАВОЯДНОЕ",
                "СРЕДНЕЕ && НЕВЫСОКОЕ",
                "!ВЫСОКОЕ && !МАЛЕНЬКОЕ"
        };

        Map<String, Predicate<Animal>> animalPredicates = Map.of(
                rules[0], RuleParser.getPredicate(rules[0]),
                rules[1], RuleParser.getPredicate(rules[1]),
                rules[2], RuleParser.getPredicate(rules[2])
        );
        AnimalFilter animalFilter = new AnimalFilter(animals, animalPredicates);
        Map<String, Integer> resultMap = animalFilter.sortAnimals();

        Map<String, Integer> expectedMap = Map.of(
                rules[0], 9,
                rules[1], 3,
                rules[2], 9
        );

        Assertions.assertThat(
                resultMap
        ).containsExactlyInAnyOrderEntriesOf(
                expectedMap
        );
    }

   @Test
   public void should_return_100_of_each_type() {

       List<Animal> animals = new ArrayList<>();
       for (int i = 0; i < 100; i++)
           animals.add(new Animal(
                   Set.of("ТРАВОЯДНОЕ", "ЛЕГКОЕ", "МАЛЕНЬКОЕ")
           ));
       Map<String, Predicate<Animal>> animalPredicates = Map.of(
               "ТРАВОЯДНОЕ", RuleParser.getPredicate("ТРАВОЯДНОЕ"),
               "ЛЕГКОЕ", RuleParser.getPredicate("ЛЕГКОЕ"),
               "МАЛЕНЬКОЕ", RuleParser.getPredicate("МАЛЕНЬКОЕ")
       );
       AnimalFilter animalFilter = new AnimalFilter(animals, animalPredicates);
       Map<String, Integer> resultMap = animalFilter.sortAnimals();

       Map<String, Integer> expectedMap = Map.of(
               "ТРАВОЯДНОЕ", 100,
               "ЛЕГКОЕ", 100,
               "МАЛЕНЬКОЕ", 100
       );

       Assertions.assertThat(
               resultMap
       ).containsExactlyInAnyOrderEntriesOf(
               expectedMap
       );
   }

    @Test
    public void should_return_33_of_each_type() {

        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < 33; i++) {
            animals.add(new Animal(
                    Set.of("ТРАВОЯДНОЕ", "ЛЕГКОЕ", "МАЛЕНЬКОЕ")
            ));
        }
        for (int i = 0; i < 33; i++) {
            animals.add(new Animal(
                    Set.of("ТРАВОЯДНОЕ", "ТЯЖЕЛОЕ", "МАЛЕНЬКОЕ")
            ));
        }
        for (int i = 0; i < 33; i++) {
            animals.add(new Animal(
                    Set.of("ТРАВОЯДНОЕ", "ЛЕГКОЕ", "ВЫСОКОЕ")
            ));
        }

        String[] rules = {
                "ТРАВОЯДНОЕ && ЛЕГКОЕ && МАЛЕНЬКОЕ",
                "ЛЕГКОЕ && ТРАВОЯДНОЕ && ВЫСОКОЕ",
                "МАЛЕНЬКОЕ && ТЯЖЕЛОЕ && ТРАВОЯДНОЕ"
        };

        Map<String, Predicate<Animal>> animalPredicates = Map.of(
                rules[0], RuleParser.getPredicate(rules[0]),
                rules[1], RuleParser.getPredicate(rules[1]),
                rules[2], RuleParser.getPredicate(rules[2])
        );
        AnimalFilter animalFilter = new AnimalFilter(animals, animalPredicates);
        Map<String, Integer> resultMap = animalFilter.sortAnimals();

        Map<String, Integer> expectedMap = Map.of(
                rules[0], 33,
                rules[1], 33,
                rules[2], 33
        );

        Assertions.assertThat(
                resultMap
        ).containsExactlyInAnyOrderEntriesOf(
                expectedMap
        );
    }

    @Test
    public void should_return_75_25_75() {

        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            animals.add(new Animal(
                    Set.of("ТРАВОЯДНОЕ", "ЛЕГКОЕ", "МАЛЕНЬКОЕ")
            ));
        }
        for (int i = 0; i < 25; i++) {
            animals.add(new Animal(
                    Set.of("ВСЕЯДНОЕ", "ТЯЖЕЛОЕ", "НЕВЫСОКОЕ")
            ));
        }
        for (int i = 0; i < 25; i++) {
            animals.add(new Animal(
                    Set.of("ПЛОТОЯДНОЕ", "СРЕДНЕЕ", "ВЫСОКОЕ")
            ));
        }

        String[] rules = {
                "ПЛОТОЯДНОЕ || ВСЕЯДНОЕ && !МАЛЕНЬКОЕ",
                "ВСЕЯДНОЕ && ТЯЖЕЛОЕ && НЕВЫСОКОЕ",
                "!ПЛОТОЯДНОЕ && !СРЕДНЕЕ && !ВЫСОКОЕ"
        };

        Map<String, Predicate<Animal>> animalPredicates = Map.of(
                rules[0], RuleParser.getPredicate(rules[0]),
                rules[1], RuleParser.getPredicate(rules[1]),
                rules[2], RuleParser.getPredicate(rules[2])
        );
        AnimalFilter animalFilter = new AnimalFilter(animals, animalPredicates);
        Map<String, Integer> resultMap = animalFilter.sortAnimals();

        Map<String, Integer> expectedMap = Map.of(
                rules[0], 50,
                rules[1], 25,
                rules[2], 75
        );

        Assertions.assertThat(
                resultMap
        ).containsExactlyInAnyOrderEntriesOf(
                expectedMap
        );
    }

    @Test
    public void should_return_0_0_0() {

        List<Animal> animals = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            animals.add(new Animal(
                    Set.of("ТРАВОЯДНОЕ", "ЛЕГКОЕ", "МАЛЕНЬКОЕ")
            ));
        }
        for (int i = 0; i < 25; i++) {
            animals.add(new Animal(
                    Set.of("ВСЕЯДНОЕ", "ТЯЖЕЛОЕ", "НЕВЫСОКОЕ")
            ));
        }
        for (int i = 0; i < 25; i++) {
            animals.add(new Animal(
                    Set.of("ПЛОТОЯДНОЕ", "СРЕДНЕЕ", "ВЫСОКОЕ")
            ));
        }

        String[] rules = {
                "ПЛОТОЯДНОЕ || ВСЕЯДНОЕ && !ТЯЖЕЛОЕ && !СРЕДНЕЕ",
                "ВСЕЯДНОЕ && ТЯЖЕЛОЕ && !НЕВЫСОКОЕ",
                "ПЛОТОЯДНОЕ && !СРЕДНЕЕ && !ВЫСОКОЕ"
        };

        Map<String, Predicate<Animal>> animalPredicates = Map.of(
                rules[0], RuleParser.getPredicate(rules[0]),
                rules[1], RuleParser.getPredicate(rules[1]),
                rules[2], RuleParser.getPredicate(rules[2])
        );
        AnimalFilter animalFilter = new AnimalFilter(animals, animalPredicates);
        Map<String, Integer> resultMap = animalFilter.sortAnimals();

        Map<String, Integer> expectedMap = Map.of(
                rules[0], 0,
                rules[1], 0,
                rules[2], 0
        );

        Assertions.assertThat(
                resultMap
        ).containsExactlyInAnyOrderEntriesOf(
                expectedMap
        );
    }

}
