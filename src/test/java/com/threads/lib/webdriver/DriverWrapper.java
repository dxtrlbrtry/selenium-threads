package com.threads.lib.webdriver;

import com.threads.lib.Logger.LocalLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class DriverWrapper {
    private final WebDriver driver;
    private final LocalLogger logger;
    private final Integer implicitTimeout;
    private final String screenshotPath;

    public DriverWrapper(WebDriver driver, Integer implicitTimeout, String screenshotPath, LocalLogger logger) {
        this.driver = driver;
        this.logger = logger;
        this.implicitTimeout = implicitTimeout;
        this.screenshotPath = screenshotPath;
    }

    public void sleep(Integer timeoutMillis) {
        stepWrapper("I wait for " + timeoutMillis / 1000 + " seconds", () -> {
            try {
                Thread.sleep(timeoutMillis);
            } catch (InterruptedException e) {
                logger.err("Interrupted. Shutting down webdriver");
                driver.quit();
                Thread.currentThread().interrupt();
            }
        });
    }

    public void quit() {
        driver.quit();
    }

    public void goToUrl(String url) {
        stepWrapper("I go to url " + url, () -> this.driver.navigate().to(url));
    }

    public boolean elementExists(By by, Integer timeout) {
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
        var result = findElement(by, false);
        driver.manage().timeouts().implicitlyWait(implicitTimeout, TimeUnit.MILLISECONDS);
        return result.isPresent();
    }

    public void waitForElement(By by, Integer timeout) {
        stepWrapper("I wait for element " + by.toString() + " to be visible for " + timeout / 1000 + " seconds", () -> {
            new WebDriverWait(driver, timeout.longValue())
                    .until(ExpectedConditions.visibilityOf(findElement(by)));
        });
    }

    public void seeElement(By by) {
        stepWrapper("I see element " + by.toString(), () -> {
            findElement(by);
        });
    }

    public void fillInputField(By by, String value) {
        stepWrapper("I fill input field " + by.toString() + " with value " + value,
                () -> findElement(by).sendKeys(value));
    }

    public void fillInputField(By by, String value, Boolean hide) {
        if (hide) {
            stepWrapper("I fill input field " + by.toString() + " with value ****",
                    () -> findElement(by).sendKeys(value));
        } else {
            fillInputField(by, value);
        }
    }

    public void click(By by) {
        stepWrapper("I click on " + by.toString(), () -> {
            findElement(by).click();
        });
    }

    public void screenshot() {
        stepWrapper("Saving screenshot to: " + screenshotPath, () -> {
            var screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                Files.copy(screenshotFile.toPath(), Path.of(screenshotPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private WebElement findElement(By by) {
        return stepWrapper("I find element " + by.toString(), () -> {
            return driver.findElement(by);
        });
    }

    private Optional<WebElement> findElement(By by, boolean failOnError) {
        try {
            return Optional.of(findElement(by));
        } catch (NoSuchElementException e) {
            if (failOnError) {
                throw e;
            }
            return Optional.empty();
        }
    }

    private <TReturn> TReturn stepWrapper(String message, Callable<TReturn> fn) {
        logger.log(message);
        try {
            return fn.call();
        } catch (Exception e) {
            var log = formatErrorLog(message, e);
            logger.err(log);
            Assert.fail(log, e);
            return null;
        }
    }

    private void stepWrapper(String message, Runnable fn) {
        logger.log(message);
        try {
            fn.run();
        } catch (Exception e) {
            var log = formatErrorLog(message, e);
            logger.err(log);
            Assert.fail(log, e);
        }
    }

    private String formatErrorLog(String message, Exception e) {
        var exceptionMessage = e.getMessage().split("\n")[0];
        return "Failed executing step " + message + ". Reason: " + exceptionMessage;
    }
}
