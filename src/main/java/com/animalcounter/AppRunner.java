package com.animalcounter;

import com.animalcounter.entities.Animal;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.parsers.RuleParser;
import com.animalcounter.utils.FileUtil;

import java.util.*;
import java.util.function.Predicate;

public class AppRunner {

    public static void run(Map<String, String> configs) {

        //Init predicates
        Map<String, Predicate<Animal>> animalPredicates =
                initAnimalPredicates(configs.get("pathToRules"));

        //Init animals
        List<Animal> animals = initAnimals(configs.get("pathToAnimals"));

        System.out.println("Animals");
        animals.forEach(System.out::println);

        Map<String, Integer> resultMap = initResultMap(animalPredicates.keySet());
        //Traverse through animals, apply each predicate, and count occurrences

        for (Animal animal : animals) {

            animalPredicates.entrySet()
                            .stream()
                            .forEach(
                                    predicateEntry -> {

                                        if (predicateEntry.getValue().test(animal)) {
                                            resultMap.put(
                                                    predicateEntry.getKey(),
                                                    resultMap.get(predicateEntry.getKey()) + 1
                                            );
                                        }

                                    }
                            );
        }

        System.out.println("Results: ");
        resultMap.forEach(
                (k, v) -> System.out.println(k + " : " + v)
        );

    }

    private static Map<String, Predicate<Animal>> initAnimalPredicates(String pathToRules) {

        Map<String, Predicate<Animal>> predicates = new HashMap<>();

        FileUtil.readFile(pathToRules,
                line -> predicates.put(line, RuleParser.getPredicate(line))
        );

        return predicates;
    }

    private static List<Animal> initAnimals(String path) {

        List<Animal> animals = new ArrayList<>();

        FileUtil.readFile(
                path,
                line -> animals.add(AnimalParser.parseString(line))
        );

        return animals;
    }

    private static Map<String, Integer> initResultMap(Set<String> keys) {

        Map<String, Integer> resultMap = new HashMap<>();
        keys.forEach(key -> resultMap.put(key, 0));

        return resultMap;
    }

}
