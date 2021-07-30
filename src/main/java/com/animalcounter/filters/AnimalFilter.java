package com.animalcounter.filters;

import com.animalcounter.entities.Animal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class AnimalFilter {

    private final List<Animal> animals;
    private final Map<String, Predicate<Animal>> animalPredicates;
    private final Map<String, Integer> resultMap;

    public AnimalFilter(
            List<Animal> animals,
            Map<String, Predicate<Animal>> animalPredicates) {

        this.animals = animals;
        this.animalPredicates = animalPredicates;
        this.resultMap = initResultMap(animalPredicates.keySet());
    }

    public Map<String, Integer> sortAnimals() {

        for (Animal animal : animals) {

            animalPredicates.forEach(
                    (key, value) -> {
                        if (value.test(animal)) {
                            resultMap.put(key, resultMap.get(key) + 1);
                        }
                    }
            );

        }

        return resultMap;
    }

    private static Map<String, Integer> initResultMap(Set<String> keys) {

        Map<String, Integer> resultMap = new HashMap<>();
        keys.forEach(key -> resultMap.put(key, 0));

        return resultMap;
    }

}
