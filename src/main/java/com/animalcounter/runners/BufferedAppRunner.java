package com.animalcounter.runners;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.consumers.ConsoleConsumer;
import com.animalcounter.entities.Animal;
import com.animalcounter.filters.AnimalFilter;
import com.animalcounter.filters.ConcurrentAnimalFilterImpl;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.parsers.RuleParser;
import com.animalcounter.utils.CollectionUtil;
import com.animalcounter.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Buffered version of AppRunner able to load animals in batches.
 *
 * @see AppRunner
 */
public class BufferedAppRunner implements Runner {

    private static final Logger log = LoggerFactory.getLogger(BufferedAppRunner.class);

    private static final int BUFFER_SIZE = 100_000;

    Map<String, Predicate<Animal>> animalPredicates;
    AppConfigs appConfigs;

    public BufferedAppRunner(AppConfigs configs) {

        this.animalPredicates = initAnimalPredicates(configs);
        this.appConfigs = configs;
    }

    @Override
    public void run() {

        int offset = 0;

        List<Animal> animals;
        Map<String, Integer> totalResultMap = new HashMap<>();

        while ((animals = initAnimals(appConfigs, offset)).size() > 0) {

            log.info("Reading animals from position {} to {}", offset, (offset + BUFFER_SIZE));

            Map<String, Integer> resultMap = sortAnimals(animals, animalPredicates);

            totalResultMap = CollectionUtil.mergeMapsSummingValues(totalResultMap, resultMap);
            offset += BUFFER_SIZE;
        }


        ConsoleConsumer.printInTable(totalResultMap);
    }

    private Map<String, Integer> sortAnimals(
            List<Animal> animals,
            Map<String, Predicate<Animal>> predicates) {

        return new ConcurrentAnimalFilterImpl(predicates).sortAnimals(animals);
    }


    private Map<String, Predicate<Animal>> initAnimalPredicates(AppConfigs appConfigs) {

        String pathToRules = appConfigs.getConfigFor(AppConfigs.PATH_TO_RULE_FILE);
        Map<String, Predicate<Animal>> predicates = new HashMap<>();

        new FileUtil(pathToRules).readFile(
                line -> predicates.put(line, RuleParser.getPredicate(line))
        );

        return predicates;
    }

    private List<Animal> initAnimals(AppConfigs appConfigs, int offset) {

        String pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);
        List<Animal> animals = new ArrayList<>();

        new FileUtil(pathToAnimalFile).readFile(
                line -> animals.add(AnimalParser.parseString(line)),
                new FileUtil.Page(offset, BUFFER_SIZE)
        );

        return animals;
    }

}
