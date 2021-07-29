package com.animalcounter;

import com.animalcounter.entities.Animal;
import com.animalcounter.parsers.RuleParser;

import java.util.Set;
import java.util.function.Predicate;

public class App {

    public static void main(String[] args) {

        Animal animal = new Animal();
        animal.setCharacteristics(
                Set.of("ЛЕГКОЕ", "НЕВЫСОКОЕ", "ВСЕЯДНОЕ")
        );

        Predicate<Animal> animalPredicate = RuleParser.getPredicate("ВСЕЯДНОЕ && НЕВЫСОКОЕ && ЛЕГКОЕ");

        System.out.println(animalPredicate.test(animal));

    }

}
