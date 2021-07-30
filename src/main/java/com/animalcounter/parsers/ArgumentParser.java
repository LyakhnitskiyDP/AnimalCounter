package com.animalcounter.parsers;

import com.animalcounter.configs.AppConfigs;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

    public static AppConfigs getConfigs(String...args) {

        Map<String, String> configMap = new HashMap<>();

        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {
                case "-animals" -> configMap.put(AppConfigs.PATH_TO_ANIMAL_FILE, getAbsolutePath() + args[++i]);

                case "-rules" -> configMap.put(AppConfigs.PATH_TO_RULE_FILE, getAbsolutePath() + args[++i]);

                case "-generate" -> configMap.put(AppConfigs.GENERATION_NUMBER, args[++i]);

                default -> throw new IllegalArgumentException("Unknown argument " + args[i]);
            }
        }

        return new AppConfigs(configMap);
    }

    private static String getAbsolutePath() {
        return System.getProperty("user.dir") + System.getProperty("file.separator");
    }

}
