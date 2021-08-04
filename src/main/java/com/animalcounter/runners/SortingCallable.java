package com.animalcounter.runners;

import com.animalcounter.configs.AppConfigs;
import com.animalcounter.entities.Animal;
import com.animalcounter.parsers.AnimalParser;
import com.animalcounter.utils.FileUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Builder
@Slf4j
public class SortingCallable implements Callable<Map<String, Integer>> {

    private final Workload workload;

    private final AppConfigs appConfigs;

    private final Map<String, Predicate<Animal>> animalPredicates;

    @Override
    public Map<String, Integer> call() {

        Map<String, Integer> resultMap = initResultMap(animalPredicates.keySet());

        animalPredicates.forEach(
                (ruleName, predicate) -> {

                    int animalsFound;
                    FileUtil fileUtil = new FileUtil(appConfigs.getConfigFor(AppConfigs.PATH_TO_ANIMAL_FILE));

                    try (Stream<String> lineStream = fileUtil.getLineStream()) {

                        animalsFound = (int) lineStream
                                                .skip(workload.offSet)
                                                .limit(workload.size)
                                                .map(AnimalParser::parseString)
                                                .filter(predicate)
                                                .count();

                        log.info("Thread {} has completed its sorting for rule: {}", Thread.currentThread().getName(), ruleName);
                    } catch (IOException e) {
                        log.error("Exception while reading animal file ", e);
                        throw new RuntimeException(e);
                    }

                    resultMap.put(ruleName, animalsFound);
                }
        );

        return resultMap;
    }

    Map<String, Integer> initResultMap(Set<String> keys) {

        Map<String, Integer> resultMap = new HashMap<>();
        keys.forEach(key -> resultMap.put(key, 0));

        return resultMap;
    }


    @RequiredArgsConstructor
    @Getter
    public static class Workload {

        private final int offSet;

        private final int size;

    }

}
