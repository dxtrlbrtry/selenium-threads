package com.threads.webtest.saucedemo.tests;

import com.threads.webtest.saucedemo.SauceDemoTestBase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = { "@SD-S0001" })
public class SauceDemoTests extends SauceDemoTestBase {
  @DataProvider(name = "valid_login", parallel = true)
  public Object[][] createValidLoginData() {
    var users = client.getUsers(new String[] { "valid_login" });
    return users.stream()
        .map(user -> new Object[] { user.name(), user.password() })
        .toArray(Object[][]::new);
  }

  @DataProvider(name = "invalid_login", parallel = true)
  public Object[][] createInvalidLoginData() {
    var users = client.getUsers(new String[] { "invalid_login" });
    return users.stream()
        .map(user -> new Object[] { user.name(), user.password() })
        .toArray(Object[][]::new);
  }

  @Test(dataProvider = "valid_login", groups = { "valid_login", "@SD-T0001" })
  public void login_test(String username, String password) {
    driver().goToUrl(baseUrl());
    loginPage.iAmOnPage();
    loginPage.enterUsername(username);
    driver().sleep(3000);
    loginPage.enterPassword(password);
    driver().sleep(3000);
    loginPage.clickLogin();
    homePage.seeProducts();
    driver().sleep(3000);
    homePage.iAmOnPage();
  }

  @Test(dataProvider = "invalid_login", groups = { "bad_login", "@SD-T0002" })
  public void invalid_login_test(String username, String password) {
    driver().goToUrl(baseUrl());
    loginPage.iAmOnPage();
    loginPage.enterUsername(username);
    driver().sleep(3000);
    loginPage.enterPassword(password);
    driver().sleep(3000);
    loginPage.clickLogin();
    driver().sleep(3000);
    loginPage.validateLoginError();
  }

  @Test(dataProvider = "invalid_login", groups = { "bad_login", "@SD-T0003" })
  public void invalid_login_test2(String username, String password) {
    driver().goToUrl(baseUrl());
    loginPage.iAmOnPage();
    loginPage.enterUsername(username);
    driver().sleep(3000);
    loginPage.enterPassword(password);
    driver().sleep(3000);
    loginPage.clickLogin();
    driver().sleep(3000);
    loginPage.validateLoginError();
  }

  @Test(dataProvider = "invalid_login", groups = { "bad_login", "@SD-T0004" })
  public void invalid_login_test3(String username, String password) {
    driver().goToUrl(baseUrl());
    loginPage.iAmOnPage();
    loginPage.enterUsername(username);
    driver().sleep(3000);
    loginPage.enterPassword(password);
    driver().sleep(3000);
    loginPage.clickLogin();
    driver().sleep(3000);
    loginPage.validateLoginError();
  }

  @Test(dataProvider = "invalid_login", groups = { "bad_login", "@SD-T0005" })
  public void invalid_login_test4(String username, String password) {
    driver().goToUrl(baseUrl());
    loginPage.iAmOnPage();
    loginPage.enterUsername(username);
    driver().sleep(3000);
    loginPage.enterPassword(password);
    driver().sleep(3000);
    loginPage.clickLogin();
    driver().sleep(3000);
    loginPage.validateLoginError();
  }

  @Test(dataProvider = "valid_login", groups = { "valid_login", "@SD-T0006" })
  public void login_test2(String username, String password) {
    driver().goToUrl(baseUrl());
    loginPage.iAmOnPage();
    loginPage.enterUsername(username);
    driver().sleep(3000);
    loginPage.enterPassword(password);
    driver().sleep(3000);
    loginPage.clickLogin();
    driver().sleep(3000);
    homePage.seeProducts();
    driver().sleep(3000);
    homePage.iAmOnPage();
  }

  @Test(dataProvider = "valid_login", groups = { "valid_login", "@SD-T0007" })
  public void login_test3(String username, String password) {
    driver().goToUrl(baseUrl());
    loginPage.iAmOnPage();
    loginPage.enterUsername(username);
    driver().sleep(3000);
    loginPage.enterPassword(password);
    driver().sleep(3000);
    loginPage.clickLogin();
    driver().sleep(3000);
    homePage.seeProducts();
    driver().sleep(3000);
    homePage.iAmOnPage();
  }

  @Test(dataProvider = "valid_login", groups = { "valid_login", "@SD-T0008" })
  public void login_test4(String username, String password) {
    driver().goToUrl(baseUrl());
    loginPage.iAmOnPage();
    loginPage.enterUsername(username);
    driver().sleep(3000);
    loginPage.enterPassword(password);
    driver().sleep(3000);
    loginPage.clickLogin();
    driver().sleep(3000);
    homePage.seeProducts();
    driver().sleep(3000);
    homePage.iAmOnPage();
  }
}
