package com.animalcounter;

import com.animalcounter.utils.FileUtil;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class GenerationRunner {

    private static String[] WEIGHT = {"ЛЕГКОЕ", "СРЕДНЕЕ", "ТЯЖЕЛОЕ"};
    private static String[] HEIGHT = {"МАЛЕНЬКОЕ", "НЕВЫСОКОЕ", "ВЫСОКОЕ"};
    private static String[] TYPE = {"ТРАВОЯДНОЕ", "ПЛОТОЯДНОЕ", "ВСЕЯДНОЕ"};

    public static void generateAnimals(Map<String, String> configs) {

        FileUtil.writeToFile(
                configs.get("pathToAnimals"),
                getAnimalSupplier(Integer.parseInt(configs.get("generationNumber")))
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
