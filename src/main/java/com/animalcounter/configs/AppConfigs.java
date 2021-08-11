package com.animalcounter.configs;

import java.util.Map;

public class AppConfigs {

    public static final String PATH_TO_ANIMAL_FILE = "pathToAnimals";
    public static final String PATH_TO_RULE_FILE = "pathToRules";
    public static final String GENERATION_NUMBER = "generationNumber";

    private final Map<String, String> configMap;

    public AppConfigs(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public boolean hasConfigFor(String key) {

        return this.configMap.containsKey(key);
    }

    public String getConfigFor(String key) {

        return this.configMap.get(key);
    }


}
