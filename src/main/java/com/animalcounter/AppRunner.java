package com.animalcounter;

import com.animalcounter.consumers.ConsoleConsumer;
import com.animalcounter.entities.Animal;
import com.animalcounter.filters.AnimalFilter;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.parsers.RuleParser;
import com.animalcounter.utils.FileUtil;

import java.util.*;
import java.util.function.Predicate;

public class AppRunner {

    public static void run(Map<String, String> configs) {

        Map<String, Predicate<Animal>> animalPredicates =
                initAnimalPredicates(configs.get("pathToRules"));

        List<Animal> animals =
                initAnimals(configs.get("pathToAnimals"));

        AnimalFilter animalFilter = new AnimalFilter(animals, animalPredicates);

        Map<String, Integer> resultMap = animalFilter.getResultMap();

        ConsoleConsumer.printInTable(resultMap);
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


}
