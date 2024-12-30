package com.threads.webtest.saucedemo.pages;

import com.threads.webtest.PageBase;
import com.threads.lib.webdriver.DriverWrapper;

import org.openqa.selenium.By;

public class LoginPage extends PageBase {
    By userName = By.id("user-name");
    By password = By.id("password");
    By loginBtn = By.id("login-button");
    By loginError = By.xpath("//h3[@data-test='error']");

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
