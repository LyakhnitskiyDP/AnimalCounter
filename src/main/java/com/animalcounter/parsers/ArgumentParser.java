package com.animalcounter.parsers;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {

    public static Map<String, String> getConfigs(String...args) {

        Map<String, String> configs = new HashMap<>();

        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {

                case "-animals":
                    configs.put("pathToAnimals", args[++i]);
                    break;
                case "-rules":
                    configs.put("pathToRules", args[++i]);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument " + args[i]);
            }
        }

        return configs;
    }

}
