package com.animalcounter.utils;

import java.util.HashMap;
import java.util.Map;

public class CollectionUtil {

    /**
     * Merges two maps of type arguments <String, Integer> summing values if keys are equal.
     *
     * @param leftMap left map to merge.
     * @param rightMap right map to merge.
     * @return Merged map.
     */
    public static Map<String, Integer> mergeMapsSummingValues(
            Map<String, Integer> leftMap,
            Map<String, Integer> rightMap) {

        Map<String, Integer> resultMap = new HashMap<>(leftMap);

        rightMap.forEach(
                (k, v) -> resultMap.merge(k, v, Integer::sum)
        );

        return resultMap;
    }

}
