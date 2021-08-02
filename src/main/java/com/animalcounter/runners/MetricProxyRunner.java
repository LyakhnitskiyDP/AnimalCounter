package com.animalcounter.runners;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class MetricProxyRunner implements Runner {

    private final Runner runnerToMeasure;
    private final Consumer<Long> executionTimeConsumer;

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        runnerToMeasure.run();
        long end = System.currentTimeMillis();

        executionTimeConsumer.accept(end - start);
    }
}
