package com.animalcounter;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.parsers.ArgumentParser;


public class App {

    public static void main(String[] args) {

        AppConfigs appConfigs = ArgumentParser.getConfigs(args);

        if (appConfigs.hasConfigFor(AppConfigs.GENERATION_NUMBER)) {
            runGenerator(appConfigs);
        }

        runSortingApp(appConfigs);
    }

    private static void runGenerator(AppConfigs appConfigs) {
        long startTime = System.nanoTime();

        new GenerationRunner(appConfigs).generateAnimals();

        long endTime = System.nanoTime();
        System.out.println("Time of animal generation: " + getPeriodInMs(startTime, endTime) + "ms");
    }

    private static void runSortingApp(AppConfigs appConfigs) {
        long startTime = System.nanoTime();

        new AppRunner(appConfigs).run();

        long endTime = System.nanoTime();
        System.out.println("Time of execution: " + getPeriodInMs(startTime, endTime) + "ms");
    }

    private static long getPeriodInMs(long startTime, long endTime) {
        return (endTime - startTime) / 1_000_000;
    }

}
