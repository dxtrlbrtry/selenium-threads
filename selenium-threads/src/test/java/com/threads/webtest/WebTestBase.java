package com.threads.webtest;

import com.threads.lib.Logger;
import com.threads.lib.Logger.LocalLogger;
import com.threads.lib.listeners.SuiteListener;
import com.threads.lib.webdriver.DriverWrapper;
import com.threads.lib.webdriver.WebDriverFactory;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Level;

public abstract class WebTestBase {
    protected final static ThreadLocal<DriverWrapper> driver = new ThreadLocal<>();
    private final static ThreadLocal<LocalLogger> logger = new ThreadLocal<>();
    private final static String outputPath = "output/webdriver/";

    protected abstract String baseUrl();

    protected static DriverWrapper driver() {
        return driver.get();
    }

    protected static LocalLogger logger() {
        return logger.get();
    }

    @Parameters({ "browser", "implicitTimeout", "pageLoadTimeout" })
    @BeforeMethod(alwaysRun = true)
    public void beforeMethodWebBase(String browser, Integer implicitTimeout, Integer pageLoadTimeout, Method method) {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        logger.set(Logger.getInstance().getLocalLogger(browser + "-" + method.getName()));
        logger().log("---Initializing " + browser + " driver");
        var execId = System.nanoTime();
        var driverOutputPath = outputPath + Thread.currentThread().getId() + "-" + browser + "-"
                + method.getName() + "-" + execId + ".log";
        var screenshotPath = outputPath + Thread.currentThread().getId() + "-" + browser + "-"
                + method.getName() + "-" + execId + ".png";
        driver.set(WebDriverFactory.getInstance().getDriver(browser, implicitTimeout, pageLoadTimeout,
                driverOutputPath, screenshotPath, logger()));
        logger().log("---Starting Test");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethodWebBase(ITestResult result) {
        if (result.isSuccess()) {
            logger().log("---Passed");
        } else {
            logger().log("---Failed");
            driver().screenshot();
        }
        SuiteListener.registerResult(result);
        driver().quit();
        driver.remove();
        logger().close();
        logger.remove();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuiteBase() {
        var outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        } else {
            deleteDir(outputDir);
        }
    }

    private void deleteDir(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }
}