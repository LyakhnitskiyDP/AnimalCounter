package com.animalcounter.filters;

import com.animalcounter.entities.Animal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Deprecated
public class AnimalFilterImpl implements AnimalFilter {

    private final Map<String, Predicate<Animal>> animalPredicates;
    private final Map<String, Integer> resultMap;

    public AnimalFilterImpl(Map<String, Predicate<Animal>> animalPredicates) {

        this.animalPredicates = animalPredicates;
        this.resultMap = initResultMap(animalPredicates.keySet());
    }

    public Map<String, Integer> sortAnimals(List<Animal> animals) {

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


}
