package com.animalcounter;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.consumers.ConsoleConsumer;
import com.animalcounter.entities.Animal;
import com.animalcounter.filters.AnimalFilter;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.parsers.RuleParser;
import com.animalcounter.utils.FileUtil;

import java.util.*;
import java.util.function.Predicate;

public class AppRunner {

    private final AnimalFilter animalFilter;

    public AppRunner(AppConfigs configs) {

        Map<String, Predicate<Animal>> animalPredicates = initAnimalPredicates(configs);
        List<Animal> animals = initAnimals(configs);

        this.animalFilter = new AnimalFilter(animals, animalPredicates);
    }

    public void run() {

        Map<String, Integer> resultMap = animalFilter.sortAnimals();

        ConsoleConsumer.printInTable(resultMap);
    }


    private Map<String, Predicate<Animal>> initAnimalPredicates(AppConfigs appConfigs) {

        String pathToRules = appConfigs.getConfigFor(AppConfigs.PATH_TO_RULE_FILE);
        Map<String, Predicate<Animal>> predicates = new HashMap<>();

        FileUtil.readFile(
                pathToRules,
                line -> predicates.put(line, RuleParser.getPredicate(line))
        );

        return predicates;
    }

    private List<Animal> initAnimals(AppConfigs appConfigs) {

        String pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);
        List<Animal> animals = new ArrayList<>();

        FileUtil.readFile(
                pathToAnimalFile,
                line -> animals.add(AnimalParser.parseString(line))
        );

        return animals;
    }


}
