package com.threads.lib.listeners;

import org.testng.*;

public class MethodListener implements IInvokedMethodListener {
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        if (SuiteListener.skipTests) {
            result.setStatus(ITestResult.SKIP);
        }
    }
}
