package com.threads.lib.listeners;

import org.testng.*;

import java.util.ArrayList;
import java.util.Iterator;

public class DataProviderInterceptor implements IDataProviderInterceptor {
    @Override
    public Iterator<Object[]> intercept(Iterator<Object[]> original, IDataProviderMethod dataProviderMethod,
            ITestNGMethod method, ITestContext context) {
        var list = new ArrayList<Object[]>();
        original.forEachRemaining(list::add);
        SuiteListener.registerMethodExecutionCount(context.getName(), method.getMethodName(), list.size());
        return list.iterator();
    }
}
