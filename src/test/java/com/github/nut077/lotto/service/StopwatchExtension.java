package com.github.nut077.lotto.service;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.engine.TestDescriptor;

import java.time.Duration;
import java.time.LocalTime;

public class StopwatchExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        getStore(context).getOrComputeIfAbsent("begin", k -> LocalTime.now());
        TestDescriptor testDescriptor = (TestDescriptor) FieldUtils.readField(context.getRoot(),
                "testDescriptor",
                true);
        getStore(context).getOrComputeIfAbsent("testClassCount", k -> testDescriptor.getChildren().size());
    }

    @Override
    public synchronized void afterAll(ExtensionContext context) {

        int testClassCount = (int) getStore(context).get("testClassCount") - 1;

        if (testClassCount <= 0) {
            System.out.println("\n>> Execution Time = " + Duration.between((LocalTime) getStore(context).get("begin"), LocalTime.now()).toMillis() + " ms\n\n");
        }

        getStore(context).put("testClassCount", testClassCount);
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(ExtensionContext.Namespace.create("com.github.nut077.lotto"));
    }
}
