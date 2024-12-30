package com.threads.lib.webdriver;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

import com.threads.lib.Logger.LocalLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.BrowserType.CHROME;
import static org.openqa.selenium.remote.BrowserType.FIREFOX;

public class WebDriverFactory {
    private static WebDriverFactory instance;

    private WebDriverFactory() {
    }

    public static WebDriverFactory getInstance() {
        if (instance == null) {
            instance = new WebDriverFactory();
        }
        return instance;
    }

    public DriverWrapper getDriver(String browser, Integer implicitTimeout, Integer pageLoadTimeout,
            String outputFilePath, String screenshotPath, LocalLogger logger) {
        var driver = switch (browser) {
            case CHROME -> initChrome(outputFilePath);
            case FIREFOX -> initFirefox(outputFilePath);
            default -> throw new UnsupportedOperationException("Driver of type '" + browser + "' not supported");
        };

        driver.manage().timeouts().implicitlyWait(implicitTimeout, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.MILLISECONDS);
        driver.manage().window().setSize(new Dimension(1280, 1024));
        return new DriverWrapper(driver, implicitTimeout, screenshotPath, logger);
    }

    private synchronized WebDriver initChrome(String logfile) {
        var options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage",
                "--dns-prefetch-disable");
        var service = new ChromeDriverService.Builder().withLogFile(new File(logfile)).build();
        try {
            service.sendOutputTo(new FileOutputStream("/dev/null"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot redirect chromedriver logs. Path /dev/null does not exist");
        }
        return new ChromeDriver(service, options);
    }

    private synchronized WebDriver initFirefox(String logfile) {
        var options = new FirefoxOptions();
        options.addArguments("-headless");
        options.setLogLevel(FirefoxDriverLogLevel.TRACE);
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, logfile);
        return new FirefoxDriver(options);
    }
}
