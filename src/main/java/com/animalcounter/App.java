package com.animalcounter;

import com.animalcounter.parsers.ArgumentParser;

import java.util.Map;

public class App {

    public static void main(String[] args) {

        Map<String, String> configs = ArgumentParser.getConfigs(args);

        long startTime = System.nanoTime();

        if (configs.containsKey("generationNumber")) {
            GenerationRunner.generateAnimals(configs);
        } else {
            AppRunner.run(configs);
        }

        long endTime = System.nanoTime();

        System.out.println("Time of execution: " + ((endTime - startTime) / 1_000_000 ) + "ms");
    }

}
