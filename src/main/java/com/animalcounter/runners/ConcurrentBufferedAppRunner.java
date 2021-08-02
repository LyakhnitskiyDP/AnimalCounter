package com.animalcounter.runners;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.consumers.ConsoleConsumer;
import com.animalcounter.entities.Animal;
import com.animalcounter.filters.ConcurrentAnimalFilterImpl;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.parsers.RuleParser;
import com.animalcounter.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

public class ConcurrentBufferedAppRunner implements Runner {

    private static final Logger log =
            LoggerFactory.getLogger(ConcurrentBufferedAppRunner.class);

    private static final int BUFFER_SIZE = 100_000;

    Map<String, Predicate<Animal>> animalPredicates;
    AppConfigs appConfigs;

    public ConcurrentBufferedAppRunner(AppConfigs configs) {

        this.animalPredicates = initAnimalPredicates(configs);
        this.appConfigs = configs;
    }

    @Override
    public void run() {

        //Move it to the constructor if possible
        int numberOfAnimals = getNumberOfAnimals(appConfigs);
        log.info("Found quantity of animals: {}", numberOfAnimals);

        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        log.info("Available processors: {}", numberOfProcessors);

        int workLoad = (int) Math.ceil((double) numberOfAnimals / (double) numberOfProcessors);
        log.info("Workload per processor: {}", workLoad);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfProcessors);

        ////////////////////////



        List<Callable<Map<String, Integer>>> tasks = new ArrayList<>();

        for (int i = 0, offset = 0; i < numberOfProcessors; i++) {
            tasks.add(
                    new SortingCallable(offset, workLoad, appConfigs)
            );
            offset += workLoad;
        }

        System.out.println("Task creation completed");

        List<Future<Map<String, Integer>>> results = null;
        try {
            results = executorService.invokeAll(tasks);
            System.out.println("Tasks submitted");
        } catch (InterruptedException e) {

            log.error("Error while work submitting", e);
            throw new RuntimeException(e);
        }


        Map<String, Integer> totalResult = new HashMap<>();

        int completedTasks = 0;

        while (completedTasks < tasks.size()) {

            for (Future<Map<String, Integer>> result : results) {

                if (result.isDone()) {
                    try {
                        totalResult = mergeMaps(totalResult, result.get());
                    } catch (Exception e) { log.error("PLEASE REFACTOR IT"); }

                    completedTasks++;
                }

            }

        }

        ConsoleConsumer.printInTable(totalResult);

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

    private int getNumberOfAnimals(AppConfigs appConfigs) {

        String pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);

        return new FileUtil(pathToAnimalFile).countLines();
    }

    /**
     * Merges two maps of argument types <String, Integer> summing values with equal keys
     */
    static Map<String, Integer> mergeMaps(
            Map<String, Integer> leftMap,
            Map<String, Integer> rightMap) {

        Map<String, Integer> resultMap = new HashMap<>(leftMap);

        rightMap.forEach(
                (k, v) -> resultMap.merge(k, v, Integer::sum)
        );

        return resultMap;
    }

}
