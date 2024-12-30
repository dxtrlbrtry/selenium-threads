package com.threads.webtest;

import org.openqa.selenium.By;

import com.threads.lib.webdriver.DriverWrapper;

public abstract class PageBase {
    protected ThreadLocal<DriverWrapper> driver;

    protected DriverWrapper driver() {
        return driver.get();
    }

    protected abstract By pageTitle();

    protected PageBase(ThreadLocal<DriverWrapper> driver) {
        this.driver = driver;
    }

    public void iAmOnPage() {
        driver().seeElement(pageTitle());
    }
}
