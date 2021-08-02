package com.animalcounter.runners;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.entities.Animal;
import com.animalcounter.filters.ConcurrentAnimalFilterImpl;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.utils.CollectionUtil;
import com.animalcounter.utils.FileUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@RequiredArgsConstructor
@Builder
public class SortingCallable implements Callable<Map<String, Integer>> {

    private static final Logger log =
            LoggerFactory.getLogger(SortingCallable.class);

    private static final int BUFFER_SIZE = 250_000;

    private final Workload workload;

    private final AppConfigs appConfigs;

    private final Map<String, Predicate<Animal>> animalPredicates;

    @Override
    public Map<String, Integer> call() {

        ConcurrentAnimalFilterImpl animalFilter =
                new ConcurrentAnimalFilterImpl(animalPredicates);

        int animalsFiltered = 0;

        Map<String, Integer> result = new HashMap<>();

        while (animalsFiltered < workload.size) {

            List<Animal> animals = initAnimals(appConfigs, workload.offSet + animalsFiltered);

            log.info("This thread is sorting {} animals", animals.size());

            if (animals.size() == 0) break;

            Map<String, Integer> partialResult = animalFilter.sortAnimals(animals);

            result = CollectionUtil.mergeMapsSummingValues(partialResult, result);

            animalsFiltered += animals.size();
        }

        return result;
    }

    private List<Animal> initAnimals(AppConfigs appConfigs, int offset) {

        String pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);
        List<Animal> animals = new ArrayList<>();

        new FileUtil(pathToAnimalFile).readFile(
                line -> animals.add(AnimalParser.parseString(line)),
                new FileUtil.Page(offset, Math.min(workload.size, BUFFER_SIZE))
        );

        return animals;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Workload {

        private final int offSet;

        private final int size;

    }

}
