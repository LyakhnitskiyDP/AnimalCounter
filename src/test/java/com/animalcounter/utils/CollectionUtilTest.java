package com.animalcounter.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CollectionUtilTest {

    @Test
    public void should_merge_maps() {

        Map<String, Integer> leftMap = Map.of("a", 1, "b", 2);
        Map<String, Integer> rightMap = Map.of("a", 3, "b", 4);

        Map<String, Integer> resultMap = CollectionUtil.mergeMapsSummingValues(leftMap, rightMap);
        Map<String, Integer> expectedMap = Map.of("a", 4, "b", 6);

        Assertions.assertThat(
                resultMap
        ).containsExactlyInAnyOrderEntriesOf(
                expectedMap
        );
    }

}
