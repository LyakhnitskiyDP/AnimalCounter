package com.animalcounter.filters;

import com.animalcounter.entities.Animal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AnimalFilter {

    Map<String, Integer> sortAnimals(List<Animal> animals);

    default Map<String, Integer> initResultMap(Set<String> keys) {

        Map<String, Integer> resultMap = new HashMap<>();
        keys.forEach(key -> resultMap.put(key, 0));

        return resultMap;
    }

}
