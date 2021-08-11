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
                    getOrPredicate(andRule)
            );
        }

        return predicate;
    }

    /**
     * Parses rules with logical OR connection.
     * RULE_1 || RULE_2
     *
     * @param orRule String with OR rules to parse.
     * @return Predicate combining a set of predicates with OR connection.
     */
    static Predicate<Animal> getOrPredicate(String orRule) {

        Predicate<Animal> orPredicate = x -> false;

        String[] atomicRules = orRule.split("\\|\\|");
        for (String atomicRule : atomicRules) {
            orPredicate = orPredicate.or(getAtomicPredicate(atomicRule));
        }

        return orPredicate;
    }

    /**
     * Parses atomic rules.
     * ATTRIBUTE_NAME -> must contain that attribute
     * !ATTRIBUTE_NAME -> must not contain that attribute
     *
     * @param atomicRule Atomic rule checking for presence or absence of an attribute.
     * @return Predicate testing for presence or absence of the specified attribute.
     */
    static Predicate<Animal> getAtomicPredicate(String atomicRule) {

        return animal -> isNegated(atomicRule) ?
                            animal.lacksCharacteristic(atomicRule.substring(1)) :
                            animal.hasCharacteristic(atomicRule);
    }

    static boolean isNegated(String token) {
        return token.startsWith(LOGICAL_NOT);
    }


}
