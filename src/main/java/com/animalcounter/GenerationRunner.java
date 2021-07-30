package com.animalcounter;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.utils.FileUtil;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class GenerationRunner {

    private static final String[] WEIGHT = {"ЛЕГКОЕ", "СРЕДНЕЕ", "ТЯЖЕЛОЕ"};
    private static final String[] HEIGHT = {"МАЛЕНЬКОЕ", "НЕВЫСОКОЕ", "ВЫСОКОЕ"};
    private static final String[] TYPE = {"ТРАВОЯДНОЕ", "ПЛОТОЯДНОЕ", "ВСЕЯДНОЕ"};

    private final String pathToAnimalFile;
    private final Integer generationNumber;

    public GenerationRunner(AppConfigs appConfigs) {

        pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);

        String generationNumberStr = appConfigs.getConfigFor(AppConfigs.GENERATION_NUMBER);
        generationNumber = Integer.parseInt(generationNumberStr);
    }

    public void generateAnimals() {

        FileUtil.writeToFile(
                pathToAnimalFile,
                getAnimalSupplier(generationNumber)
        );
    }

    public static Supplier<String> getAnimalSupplier(int number) {
        return () -> {
            StringBuilder animalsStrBuilder = new StringBuilder();

            for (int i = 0; i < number; i++) {

                animalsStrBuilder.append(getRandomElement(WEIGHT));
                animalsStrBuilder.append(",");
                animalsStrBuilder.append(getRandomElement(HEIGHT));
                animalsStrBuilder.append(",");
                animalsStrBuilder.append(getRandomElement(TYPE));
                animalsStrBuilder.append("\n");
            }

            return animalsStrBuilder.toString();
        };
    }

    private static String getRandomElement(String[] elements) {

        Random random = new Random();
        return elements[random.nextInt(elements.length)];
    }


}
