package com.animalcounter.consumers;

import java.util.Map;

public class ConsoleConsumer {


    public static void printInTable(Map<String, Integer> resultMap) {

        resultMap.forEach(
                (k, v) -> {
                    System.out.printf(
                            """
                                    ------------------------------------------
                                    Rule: %s
                                    Animals found: %s
                                    """,
                            k, v
                    );
                }
        );

        System.out.println("------------------------------------------");

    }

}
