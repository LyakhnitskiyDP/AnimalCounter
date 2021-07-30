package com.animalcounter.parsers;

import com.animalcounter.entities.Animal;

import java.util.function.Predicate;

public class RuleParser {

    public static final String LOGICAL_NOT = "!";

    public static Predicate<Animal> getPredicate(String rule) {

        Predicate<Animal> predicate = x -> true;

        String[] andRules = rule.split("&&");

        for (String andRule : andRules) {
            predicate = predicate.and(
                    getSubPredicate(andRule)
            );
        }

        return predicate;
    }

    private static Predicate<Animal> getSubPredicate(String subRule) {

        Predicate<Animal> subPredicate = x -> false;

        String[] basicRules = subRule.split("\\|\\|");
        for (String basicRule : basicRules) {
            subPredicate = subPredicate.or(getBasicPredicate(basicRule));
        }

        return subPredicate;
    }

    private static Predicate<Animal> getBasicPredicate(String basicRule) {

        Predicate<Animal> basicPredicate =
                animal -> isNegated(basicRule) ?
                            animal.lacksCharacteristic(basicRule.substring(1)) :
                            animal.hasCharacteristic(basicRule);

        return basicPredicate;
    }

    private static boolean isNegated(String token) {
        return token.startsWith(LOGICAL_NOT);
    }


}
