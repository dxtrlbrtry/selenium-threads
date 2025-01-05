package com.threads.webtest.saucedemo.pages;

import com.threads.webtest.PageBase;
import com.threads.lib.webdriver.DriverWrapper;

import org.openqa.selenium.By;

public class LoginPage extends PageBase {
    private final By userName = By.id("user-name");
    private final By password = By.id("password");
    private final By loginBtn = By.id("login-button");
    private final By loginError = By.xpath("//h3[@data-test='error']");

    @Override
    protected By pageTitle() {
        return By.xpath("//div[text()='Swag Labs']");
    }

    public LoginPage(ThreadLocal<DriverWrapper> driver) {
        super(driver);
    }

    public void enterUsername(String value) {
        driver().fillInputField(userName, value);
    }

    public void enterPassword(String value) {
        driver().fillInputField(password, value, true);
    }

    public void clickLogin() {
        driver().click(loginBtn);
    }

    public void validateLoginError() {
        driver().seeElement(loginError);
    }
}
