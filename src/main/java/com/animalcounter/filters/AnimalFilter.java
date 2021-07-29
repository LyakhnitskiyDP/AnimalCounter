package com.animalcounter.filters;

import com.animalcounter.entities.Animal;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class AnimalFilter {

    private final List<Animal> animals;

    private final Map<String, Predicate<Animal>> animalPredicates;

    public Map<String, Integer> getResultMap() {

        Map<String, Integer> resultMap =
                initResultMap(animalPredicates.keySet());

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

        return resultMap;
    }

    private static Map<String, Integer> initResultMap(Set<String> keys) {

        Map<String, Integer> resultMap = new HashMap<>();
        keys.forEach(key -> resultMap.put(key, 0));

        return resultMap;
    }

}
