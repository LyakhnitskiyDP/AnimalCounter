package com.animalcounter;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.parsers.ArgumentParser;
import com.animalcounter.runners.BufferedAppRunner;
import com.animalcounter.runners.ConcurrentBufferedAppRunner;
import com.animalcounter.runners.GenerationRunner;
import com.animalcounter.runners.MetricProxyRunner;
import com.animalcounter.runners.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;


public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

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

        Runner sortingAppRunner;

        if (appConfigs.hasConfigFor(AppConfigs.CONCURRENT_MODE)) {
            sortingAppRunner = new ConcurrentBufferedAppRunner(appConfigs);
        } else {
            sortingAppRunner = new BufferedAppRunner(appConfigs);
        }

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
