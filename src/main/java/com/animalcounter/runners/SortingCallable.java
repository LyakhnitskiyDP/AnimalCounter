package com.animalcounter.runners;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.consumers.ConsoleConsumer;
import com.animalcounter.entities.Animal;
import com.animalcounter.filters.ConcurrentAnimalFilterImpl;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.parsers.RuleParser;
import com.animalcounter.utils.CollectionUtil;
import com.animalcounter.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class SortingCallable implements Callable<Map<String, Integer>> {

    private static final Logger log = LoggerFactory.getLogger(SortingCallable.class);

    private static final int BUFFER_SIZE = 250_000;

    private final int offset;

    private final int workloadSize;

    private final AppConfigs appConfigs;

    @Override
    public Map<String, Integer> call() {

        ConcurrentAnimalFilterImpl animalFilter =
                new ConcurrentAnimalFilterImpl(initAnimalPredicates(appConfigs));

        int animalsFiltered = 0;

        Map<String, Integer> result = new HashMap<>();

        while (animalsFiltered < workloadSize) {

            List<Animal> animals = initAnimals(appConfigs, offset + animalsFiltered);

            log.info("{} is sorting {} animals", Thread.currentThread().getName(), animals.size());

            if (animals.size() == 0) break;

            Map<String, Integer> partialResult = animalFilter.sortAnimals(animals);

            result = CollectionUtil.mergeMapsSummingValues(partialResult, result);

            animalsFiltered += animals.size();
        }
        result.forEach((k, v) -> System.out.println("key: " + k + " value: " + v));

        return result;
    }

    private List<Animal> initAnimals(AppConfigs appConfigs, int offset) {

        String pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);
        List<Animal> animals = new ArrayList<>();

        new FileUtil(pathToAnimalFile).readFile(
                line -> animals.add(AnimalParser.parseString(line)),
                new FileUtil.Page(offset, Math.min(workloadSize, BUFFER_SIZE))
        );

        return animals;
    }

    private Map<String, Predicate<Animal>> initAnimalPredicates(AppConfigs appConfigs) {

        String pathToRules = appConfigs.getConfigFor(AppConfigs.PATH_TO_RULE_FILE);
        Map<String, Predicate<Animal>> predicates = new HashMap<>();

        new FileUtil(pathToRules).readFile(
                line -> predicates.put(line, RuleParser.getPredicate(line))
        );

        return predicates;
    }

}
