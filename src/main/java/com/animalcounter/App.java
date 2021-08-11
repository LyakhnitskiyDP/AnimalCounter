package com.animalcounter;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.parsers.ArgumentParser;
import com.animalcounter.runners.ConcurrentBufferedAppRunner;
import com.animalcounter.runners.GenerationRunner;
import com.animalcounter.runners.MetricProxyRunner;
import com.animalcounter.runners.Runner;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;


@Slf4j
public class App {

    public static void main(String[] args) {

        AppConfigs appConfigs = ArgumentParser.getConfigs(args);

        if (appConfigs.hasConfigFor(AppConfigs.GENERATION_NUMBER)) {
            runGenerator(appConfigs);
        }

        runSortingApp(appConfigs);
    }

    private static void runGenerator(AppConfigs appConfigs) {

        GenerationRunner generationRunner = new GenerationRunner(appConfigs);
        Consumer<Long> metricsConsumer =
                timePassed -> log.info("Time of animal generation: {}ms", timePassed);

        runWithTimeMeasuring(generationRunner, metricsConsumer);
    }

    private static void runSortingApp(AppConfigs appConfigs) {

        Runner sortingAppRunner = new ConcurrentBufferedAppRunner(appConfigs);

        Consumer<Long> metricsConsumer =
                timePassed -> log.info("Time of sorting app execution: {}ms", timePassed);

        runWithTimeMeasuring(sortingAppRunner, metricsConsumer);
    }

    private static void runWithTimeMeasuring(
            Runner runner,
            Consumer<Long> timePassedConsumer) {

        MetricProxyRunner metricProxyRunner =
                new MetricProxyRunner(runner, timePassedConsumer);
        metricProxyRunner.run();
    }


}
