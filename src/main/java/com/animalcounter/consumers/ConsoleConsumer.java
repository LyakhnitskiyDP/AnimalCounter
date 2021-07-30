package com.animalcounter.consumers;

import java.util.Map;

public class ConsoleConsumer {

    public static void printInTable(Map<String, Integer> resultMap) {
       printInTable(resultMap,90);
    }

    public static void printInTable(Map<String, Integer> resultMap, int width) {

        System.out.format(getHeader(width));

        resultMap.forEach(
                (k, v) -> {
                    int ruleWidth = (int) Math.round(width * 0.70);
                    System.out.print("|");
                    System.out.print(rightPadding(" " + k, ruleWidth));
                    System.out.print("|");
                    System.out.print(leftPadding(v.toString() + " ", width - ruleWidth - 3));
                    System.out.print("|");
                    System.out.println();
                }
        );

        System.out.format(getFooter(width));

    }

    private static String getHeader(int width) {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("+");
        for (int i = 0; i < ( (width / 2) - 4); i++) strBuilder.append("-");
        strBuilder.append("RESULTS");
        for (int i = 0; i < ( (width / 2) - 5); i++) strBuilder.append("-");
        strBuilder.append("+\n");

        return strBuilder.toString();
    }

    private static String rightPadding(String input, int length) {

        return String.format("%" + (-length) + "s", input);
    }

    private static String leftPadding(String input, int lenth) {

        return String.format("%" + lenth + "s", input);
    }

    private static String getFooter(int width) {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("+");
        for (int i = 0; i < width - 2; i++) strBuilder.append("-");
        strBuilder.append("+\n");

        return strBuilder.toString();
    }

}
