package com.threads.lib.listeners;

import com.threads.lib.Logger;
import com.threads.lib.listeners.models.MethodExecutions;

import org.testng.*;

import java.util.HashMap;

public class SuiteListener implements ISuiteListener {
    private final static HashMap<String, HashMap<String, MethodExecutions>> testMap = new HashMap<>();
    public static Boolean skipTests = false;
    private static long startTime;
    private static Logger.LocalLogger logger;

    private static int targetExecutionPercentage;
    private static int failThreshold;

    private static int executedCount;
    private static int totalEstimatedCount;
    private static int failedCount;
    private static float execPercentage;
    private static float failPercentage;

    @Override
    public void onStart(ISuite suite) {
        startTime = System.currentTimeMillis();
        failThreshold = Integer.parseInt(suite.getParameter("failThreshold"));
        targetExecutionPercentage = Integer.parseInt(suite.getParameter("executionTarget"));
        for (ITestNGMethod method : suite.getAllMethods()) {
            var testName = method.getXmlTest().getName();
            if (!testMap.containsKey(testName)) {
                testMap.put(testName, new HashMap<>());
            }
            testMap.get(testName).put(method.getMethodName(), new MethodExecutions());
        }
        logger = Logger.getInstance().getLocalLogger(suite.getName());
    }

    @Override
    public void onFinish(ISuite suite) {
        printResults();
    }

    public synchronized static void registerMethodExecutionCount(String testName, String methodName,
            int paramsCount) {
        testMap.get(testName).get(methodName).registerExecutionCount(paramsCount);
    }

    private static void refreshResults() {
        executedCount = 0;
        totalEstimatedCount = 0;
        failedCount = 0;
        for (var testName : testMap.keySet()) {
            var test = testMap.get(testName);
            for (var methodName : test.keySet()) {
                var method = test.get(methodName);
                totalEstimatedCount += method.getTotalEstimatedExecutionCount();
                failedCount += method.getFailedCount();
                executedCount += method.getExecutedCount();
            }
        }
        execPercentage = totalEstimatedCount == 0 ? 0
                : Math.round((float) executedCount / totalEstimatedCount * 1000) / 10.0f;
        failPercentage = totalEstimatedCount == 0 ? 0
                : Math.round((float) failedCount / totalEstimatedCount * 1000) / 10.0f;

    }

    private static void printResults() {
        calculateRemainingTime();
        logger.log("Status: " + execPercentage + "% | " + executedCount + " / " + totalEstimatedCount + " Total"
                + (failedCount > 0 ? " - \u001B[31m" + failedCount + " failed (" + failPercentage + "%)\u001B[0m"
                : ""));
    }

    public synchronized static void registerResult(ITestResult result) {
        testMap.get(result.getTestContext().getName())
                .get(result.getMethod().getMethodName())
                .recordExecution(result);
        refreshResults();
        printResults();
        if (execPercentage >= targetExecutionPercentage) {
            skipTests = true;
            var log = "Execution threshold of " + targetExecutionPercentage + "% reached, skipping rest of the tests";
            logger.log(log);
        }

        if (failPercentage > failThreshold) {
            skipTests = true;
            var log = "Fail threshold of " + failThreshold + "% has been exceeded. Aborting run";
            logger.err(log);
        }
    }

    private static void calculateRemainingTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        var log = "Elapsed: " + formatDuration(elapsedTime);

        if (execPercentage > 0) {
            long estimatedTotalTime = (long) (elapsedTime / (execPercentage / targetExecutionPercentage));
            long estimatedRemainingTime = estimatedTotalTime - elapsedTime;
            log += " - Total: " + formatDuration(estimatedTotalTime) + " - Remaining: "
                    + formatDuration(estimatedRemainingTime);
        }
        logger.log(log);
    }

    private static String formatDuration(long durationMs) {
        long minutes = durationMs / 60000;
        long seconds = (durationMs % 60000) / 1000;
        return minutes + "m " + seconds + "s";
    }
}
