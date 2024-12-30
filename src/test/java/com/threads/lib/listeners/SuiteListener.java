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
        long currentTime = System.currentTimeMillis();
        logger.log("Total duration: " + formatDuration(currentTime - startTime));
    }

    public synchronized static void registerMethodExecutionCount(String testName, String methodName,
            int paramsCount) {
        testMap.get(testName).get(methodName).registerExecutionCount(paramsCount);
    }

    public synchronized static void registerResult(ITestResult result) {
        testMap.get(result.getTestContext().getName())
                .get(result.getMethod().getMethodName())
                .recordExecution(result);
        var executedCount = 0;
        var totalEstimated = 0;
        var failedCount = 0;
        for (var testName : testMap.keySet()) {
            var test = testMap.get(testName);
            for (var methodName : test.keySet()) {
                var method = test.get(methodName);
                totalEstimated += method.getTotalEstimatedExecutionCount();
                failedCount += method.getFailedCount();
                executedCount += method.getExecutedCount();
            }
        }

        var execPercentage = totalEstimated == 0 ? 0
                : Math.round((float) executedCount / totalEstimated * 1000) / 10.0f;
        var failPercentage = totalEstimated == 0 ? 0
                : Math.round((float) failedCount / totalEstimated * 1000) / 10.0f;
        calculateRemainingTime(execPercentage, targetExecutionPercentage);
        logger.log("Status: " + execPercentage + "% | " + executedCount + " / " + totalEstimated + " Total"
                + (failedCount > 0 ? " - \u001B[31m" + failedCount + " failed (" + failPercentage + "%)\u001B[0m"
                        : ""));
        if (execPercentage >= targetExecutionPercentage) {
            logger.log(
                    "Execution threshold of " + targetExecutionPercentage + "% reached, skipping rest of the tests");
            skipTests = true;
        }

        if (failPercentage > failThreshold) {
            skipTests = true;
            var log = "Fail threshold of " + failThreshold + "% has been exceeded. Aborting run";
            logger.err(log);
            Assert.fail(log);
        }
    }

    private static void calculateRemainingTime(double currentExecPercentage, int targetExecPercentage) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        var log = "Elapsed: " + formatDuration(elapsedTime);

        if (currentExecPercentage > 0) {
            long estimatedTotalTime = (long) (elapsedTime / (currentExecPercentage / targetExecPercentage));
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
