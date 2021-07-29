package com.animalcounter.consumers;

import java.util.Map;

public class ConsoleConsumer {

    public static void printInTable(Map<String, Integer> resultMap) {

        System.out.format(getHeader());

        resultMap.forEach(
                (k, v) -> {
                    System.out.print("|");
                    System.out.print(rightPadding(" " + k, 42));
                    System.out.print("|");
                    System.out.print(leftPadding(v.toString() + " ", 44));
                    System.out.print("|");
                    System.out.println();
                }
        );

        System.out.format(getFooter());

    }

    private static String getHeader() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("+");
        for (int i = 0; i < 40; i++) strBuilder.append("-");
        strBuilder.append("RESULTS");
        for (int i = 0; i < 40; i++) strBuilder.append("-");
        strBuilder.append("+\n");

        return strBuilder.toString();
    }

    private static String rightPadding(String input, int length) {

        return String.format("%" + (-length) + "s", input);
    }

    private static String leftPadding(String input, int lenth) {

        return String.format("%" + lenth + "s", input);
    }

    private static String getFooter() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("+");
        for (int i = 0; i < 87; i++) strBuilder.append("-");
        strBuilder.append("+\n");

        return strBuilder.toString();
    }

}
