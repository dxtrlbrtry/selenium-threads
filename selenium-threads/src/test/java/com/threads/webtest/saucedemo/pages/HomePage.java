package com.threads.webtest.saucedemo.pages;

import com.threads.webtest.PageBase;
import com.threads.lib.webdriver.DriverWrapper;

import org.openqa.selenium.By;

public class HomePage extends PageBase {
    By products = By.xpath("//span[@data-test='title']");

    @Override
    protected By pageTitle() {
        return products;
    }

    public HomePage(ThreadLocal<DriverWrapper> driver) {
        super(driver);
    }

    public void seeProducts() {
        driver().waitForElement(products, 10000);
    }
}
