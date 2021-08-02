package com.animalcounter.filters;

import com.animalcounter.entities.Animal;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Concurrent version of AnimalFilter able to sort animals in parallel.
 *
 * @see AnimalFilter
 */
public class ConcurrentAnimalFilterImpl implements AnimalFilter {

    private final Map<String, Predicate<Animal>> animalPredicates;

    public ConcurrentAnimalFilterImpl(Map<String, Predicate<Animal>> animalPredicates) {

        this.animalPredicates = animalPredicates;
    }

    public Map<String, Integer> sortAnimals(List<Animal> animals) {


        Map<String, Integer> resultMap = initResultMap(animalPredicates.keySet());

        animalPredicates.forEach(
                (ruleName, predicate) -> {

                    int animalsFound = (int) animals.parallelStream()
                                                    .filter(predicate)
                                                    .count();

                    resultMap.put(ruleName, animalsFound);
                }
        );

        return resultMap;
    }

}
