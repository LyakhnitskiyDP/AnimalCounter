package com.animalcounter.runners;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.consumers.ConsoleConsumer;
import com.animalcounter.entities.Animal;
import com.animalcounter.parsers.RuleParser;
import com.animalcounter.utils.CollectionUtil;
import com.animalcounter.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * Buffered and concurrent version of AppRunner.
 *
 * @see AppRunner
 */
public class ConcurrentBufferedAppRunner implements Runner {

    private static final Logger log =
            LoggerFactory.getLogger(ConcurrentBufferedAppRunner.class);

    private  final AppConfigs appConfigs;

    private final Map<String, Predicate<Animal>> animalPredicates;
    private final int numberOfProcessors;
    private final int workLoadPerProcessor;

    private final ExecutorService executorService;

    public ConcurrentBufferedAppRunner(AppConfigs configs) {

        this.appConfigs = configs;
        this.animalPredicates = initAnimalPredicates(configs);

        int totalNumberOfAnimals = getNumberOfAnimals(appConfigs);
        log.info("Found quantity of animals: {}", totalNumberOfAnimals);

        this.numberOfProcessors = Runtime.getRuntime().availableProcessors();
        log.info("Available processors: {}", numberOfProcessors);

        this.workLoadPerProcessor = calculateWorkload(totalNumberOfAnimals, numberOfProcessors);
        log.info("Workload per processor: {}", workLoadPerProcessor);

        this.executorService = Executors.newFixedThreadPool(numberOfProcessors);
    }

    private int calculateWorkload(int totalNumberOfAnimals, int numberOfProcessors) {

        return (int) Math.ceil((double) totalNumberOfAnimals / (double) numberOfProcessors);
    }

    @Override
    public void run() {

        List<Callable<Map<String, Integer>>> tasksToExecute = initTasks();

        List<Future<Map<String, Integer>>> futureResults = submitToExecutorService(tasksToExecute);

        Map<String, Integer> totalResult = waitForTasksToComplete(futureResults);

        ConsoleConsumer.printInTable(totalResult);
    }

    private List<Callable<Map<String, Integer>>> initTasks() {

        List<Callable<Map<String, Integer>>> tasks = new ArrayList<>();

        for (int i = 0, offset = 0; i < numberOfProcessors; i++) {

            SortingCallable sortingCallable =
                    SortingCallable.builder()
                                    .workload(new SortingCallable.Workload(offset, workLoadPerProcessor))
                                    .animalPredicates(animalPredicates)
                                    .appConfigs(appConfigs)
                                   .build();

            tasks.add(
                    sortingCallable
            );

            offset += workLoadPerProcessor;
        }

        return tasks;
    }

    private List<Future<Map<String, Integer>>> submitToExecutorService(List<Callable<Map<String, Integer>>> tasks) {

        List<Future<Map<String, Integer>>> results;

        try {
            results = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {

            log.error("Error while work submitting", e);
            throw new RuntimeException(e);
        }

        return results;
    }

    private Map<String, Integer> waitForTasksToComplete(List<Future<Map<String, Integer>>> submittedTasks) {

        Map<String, Integer> totalResult = new HashMap<>();

        int completedTasks = 0;
        while (completedTasks < submittedTasks.size()) {

            for (Future<Map<String, Integer>> task : submittedTasks) {

                if (task.isDone()) {
                    try {

                        totalResult = CollectionUtil.mergeMapsSummingValues(totalResult, task.get());

                    } catch (InterruptedException interruptedException) {

                        log.error("Interrupted while getting a future", interruptedException);
                    } catch (ExecutionException e) {

                        log.error("Exception while trying to get the result of a task", e.getCause());
                    }

                    completedTasks++;
                }
            }
        }

        executorService.shutdown();

        return totalResult;
    }


    private Map<String, Predicate<Animal>> initAnimalPredicates(AppConfigs appConfigs) {

        String pathToRules = appConfigs.getConfigFor(AppConfigs.PATH_TO_RULE_FILE);
        Map<String, Predicate<Animal>> predicates = new HashMap<>();

        new FileUtil(pathToRules).readFile(
                line -> predicates.put(line, RuleParser.getPredicate(line))
        );

        return predicates;
    }

    private int getNumberOfAnimals(AppConfigs appConfigs) {

        String pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);

        return new FileUtil(pathToAnimalFile).countLines();
    }


}
