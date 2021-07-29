package com.animalcounter;

import com.animalcounter.entities.Animal;
import com.animalcounter.parsers.ArgumentParser;
import com.animalcounter.parsers.RuleParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class App {

    public static void main(String[] args) {

        Map<String, String> configs = ArgumentParser.getConfigs(args);

        AppRunner.run(configs);

    }

}
