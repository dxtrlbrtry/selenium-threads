package com.threads.lib.listeners.models;

import org.testng.ITestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class MethodExecutions {
    private final static List<Integer> paramCounts = new ArrayList<>();
    private static int avgParamCount;

    private final HashMap<Integer, Optional<Boolean>> executions = new HashMap<>();
    private boolean isEstimation = true;

    public int getExecutedCount() {
        return !isEstimation
                ? (int) executions.values().stream().filter(Optional::isPresent).count()
                : 0;
    }

    public int getFailedCount() {
        return !isEstimation
                ? (int) executions.values().stream().filter(e -> e.isPresent() && !e.get()).count()
                : 0;
    }

    public int getTotalEstimatedExecutionCount() {
        return !isEstimation ? executions.values().size() : avgParamCount;
    }

    public void registerExecutionCount(Integer paramCount) {
        IntStream.range(0, paramCount).forEach(i -> executions.put(i, Optional.empty()));
        isEstimation = false;
        paramCounts.add(paramCount);
        avgParamCount = (int) Math.round(paramCounts.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0));
    }

    public void recordExecution(ITestResult result) {
        for (Integer key : executions.keySet()) {
            if (executions.get(key).isEmpty()) {
                executions.put(key, Optional.of(result.isSuccess()));
                break;
            }
        }
    }
}
