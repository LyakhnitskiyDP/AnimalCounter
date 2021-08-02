package com.animalcounter.runners;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.consumers.ConsoleConsumer;
import com.animalcounter.entities.Animal;
import com.animalcounter.filters.AnimalFilterImpl;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.parsers.RuleParser;
import com.animalcounter.utils.FileUtil;

import java.util.*;
import java.util.function.Predicate;

@Deprecated
public class AppRunner implements Runner {

    private final AnimalFilterImpl animalFilterImpl;
    private  final List<Animal> animals;

    public AppRunner(AppConfigs configs) {

        Map<String, Predicate<Animal>> animalPredicates = initAnimalPredicates(configs);
        animals = initAnimals(configs);

        this.animalFilterImpl = new AnimalFilterImpl(animalPredicates);
    }

    @Override
    public void run() {

        Map<String, Integer> resultMap = animalFilterImpl.sortAnimals(animals);

        ConsoleConsumer.printInTable(resultMap);
    }


    private Map<String, Predicate<Animal>> initAnimalPredicates(AppConfigs appConfigs) {

        String pathToRules = appConfigs.getConfigFor(AppConfigs.PATH_TO_RULE_FILE);
        Map<String, Predicate<Animal>> predicates = new HashMap<>();

        new FileUtil(pathToRules).readFile(
                line -> predicates.put(line, RuleParser.getPredicate(line))
        );

        return predicates;
    }

    private List<Animal> initAnimals(AppConfigs appConfigs) {

        String pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);
        List<Animal> animals = new ArrayList<>();

        new FileUtil(pathToAnimalFile).readFile(
                line -> animals.add(AnimalParser.parseString(line))
        );

        return animals;
    }


}
