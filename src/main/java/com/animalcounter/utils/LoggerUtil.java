package com.animalcounter.utils;

public class LoggerUtil {

    public static int getCompletenessPercentage(int completed, int total) {

        return (int) Math.round(((double) completed / (double) total ) * 100);
    }

}
