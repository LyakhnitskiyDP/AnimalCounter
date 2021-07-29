package com.animalcounter.parsers;

import com.animalcounter.entities.Animal;
import com.sun.jdi.connect.Connector;

import java.util.function.Predicate;

public class RuleParser {

    public static final String LOGICAL_AND = "&&";
    public static final String LOGICAL_OR = "||";
    public static final String LOGICAL_NOT = "!";

    public static Predicate<Animal> getPredicate(String rule) {

        String[] ruleParts = rule.split(" ");
        Predicate<Animal> predicate = a -> true;

        String logicalOperand = "";

        for (String rulePart : ruleParts ) {

            if (isLogicalOperand(rulePart)) {

                logicalOperand = rulePart;

            } else {

                if (logicalOperand.isBlank() || logicalOperand.equals(LOGICAL_AND)) {

                    if (isNegated(rulePart)) {
                        predicate = predicate.and(
                                animal -> animal.getCharacteristics()
                                        .stream()
                                        .noneMatch(trait -> trait.equals(rulePart))
                        );
                    } else {
                        predicate = predicate.and(
                                animal -> animal.getCharacteristics()
                                        .stream()
                                        .anyMatch(trait -> trait.equals(rulePart))
                        );
                    }

                } else if (logicalOperand.equals(LOGICAL_OR)) {

                    if (isNegated(rulePart)) {

                        predicate = predicate.or(
                                animal -> animal.getCharacteristics()
                                                .stream()
                                                .anyMatch(trait -> trait.equals(rulePart))
                        );

                    } else {

                        predicate = predicate.or(
                                animal -> animal.getCharacteristics()
                                                .stream()
                                                .noneMatch(trait -> trait.equals(rulePart))
                        );
                    }
                }
            }
        }

        return predicate;
    }

    private static boolean isLogicalOperand(String token) {
        return token.equals(LOGICAL_AND) || token.equals(LOGICAL_OR);
    }

    private static boolean isNegated(String token) {
        return token.startsWith(LOGICAL_NOT);
    }


}
