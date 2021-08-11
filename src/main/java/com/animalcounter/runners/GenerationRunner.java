package com.animalcounter.runners;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.utils.FileUtil;
import com.animalcounter.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class GenerationRunner implements Runner {

    private static final Logger log = LoggerFactory.getLogger(GenerationRunner.class);

    private static final int BUFFER_SIZE = 100_000;

    private static final String[] WEIGHT = {"ЛЕГКОЕ", "СРЕДНЕЕ", "ТЯЖЕЛОЕ"};
    private static final String[] HEIGHT = {"МАЛЕНЬКОЕ", "НЕВЫСОКОЕ", "ВЫСОКОЕ"};
    private static final String[] TYPE = {"ТРАВОЯДНОЕ", "ПЛОТОЯДНОЕ", "ВСЕЯДНОЕ"};

    private final String pathToAnimalFile;
    private final Integer numberOfAnimalsToGenerate;

    public GenerationRunner(AppConfigs appConfigs) {

        pathToAnimalFile = appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE);

        String generationNumberStr = appConfigs.getConfigFor(AppConfigs.GENERATION_NUMBER);
        numberOfAnimalsToGenerate = Integer.parseInt(generationNumberStr);
    }

    @Override
    public void run() {
        generateAnimals();
    }

    public void generateAnimals() {

        int animalsGenerated = 0;

        while (animalsGenerated < numberOfAnimalsToGenerate) {

            int load = Math.min(numberOfAnimalsToGenerate - animalsGenerated, BUFFER_SIZE);

            new FileUtil(pathToAnimalFile).writeToFile(
                    getAnimals(load)
            );

            animalsGenerated += load;

            log.info("Animals generated: {} / {} - {}% ",
                    animalsGenerated,
                    numberOfAnimalsToGenerate,
                    LoggerUtil.getCompletenessPercentage(animalsGenerated, numberOfAnimalsToGenerate)
            );
        }
    }



    public static String getAnimals(int number) {

            StringBuilder animalsStrBuilder = new StringBuilder();

            for (int i = 0; i < number; i++) {
                animalsStrBuilder.append(
                        String.format(
                                "%s,%s,%s%s",
                                getRandomElement(WEIGHT),
                                getRandomElement(HEIGHT),
                                getRandomElement(TYPE),
                                System.lineSeparator()
                        )
                );
            }

            return animalsStrBuilder.toString();
    }

    private static String getRandomElement(String[] elements) {

        Random random = new Random();
        return elements[random.nextInt(elements.length)];
    }


}
