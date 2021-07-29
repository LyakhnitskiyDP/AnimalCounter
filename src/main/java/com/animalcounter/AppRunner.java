package com.animalcounter;

import com.animalcounter.entities.Animal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class AppRunner {

    public static void run(Map<String, String> configs) {

        //Init predicates
        Map<String, Predicate<Animal>> animalPredicates =
                initAnimalPredicates(configs.get("pathToRules"));

        //Init animals
        List<Animal> animals = initAnimals(configs.get("pathToAnimals"));

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

        resultMap.forEach(
                (k, v) -> System.out.println(k + " : " + v)
        );

    }

    private static Map<String, Predicate<Animal>> initAnimalPredicates(String pathToRules) {


    }

    private static List<Animal> initAnimals(String path) {

    }

    private static Map<String, Integer> initResultMap(Set<String> keys) {

        Map<String, Integer> resultMap = new HashMap<>();
        keys.forEach(key -> resultMap.put(key, 0));

        return resultMap;
    }

}
