package com.threads.webtest.saucedemo;

import com.threads.lib.restClient.users.UserClient;
import com.threads.lib.restClient.users.models.UserModel;
import com.threads.webtest.WebTestBase;
import com.threads.webtest.saucedemo.pages.HomePage;
import com.threads.webtest.saucedemo.pages.LoginPage;
import org.testng.annotations.BeforeSuite;

public class SauceDemoTestBase extends WebTestBase {
    protected static UserClient client;
    protected static LoginPage loginPage;
    protected static HomePage homePage;

    @Override
    protected String baseUrl() {
        return "https://www.saucedemo.com/";
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        var host = System.getenv().get("HOST");
        var port = System.getenv().get("SERVER_PORT");
        client = new UserClient("http://" + host + ":" + port + "/api");
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
        var users = client.getUsers();
        for (UserModel user : users) {
            client.deleteUser(user.id());
        }
        client.createUser("standard_user", "secret_sauce", new String[] { "valid_login" });
        client.createUser("locked_out_user", "secret_sauce", new String[] { "valid_login" });
        client.createUser("performance_glitch_user", "secret_sauce", new String[] { "valid_login" });
        client.createUser("problem_user", "secret_sauce", new String[] { "valid_login" });
        client.createUser("error_user", "secret_sauce", new String[] { "valid_login" });
        client.createUser("visual_user", "secret_sauce", new String[] { "valid_login" });
        client.createUser("test", "secret_sauce", new String[] { "invalid_login" });
        client.createUser("standard_user", "test", new String[] { "invalid_login" });
        client.createUser("", "test", new String[] { "invalid_login" });
        client.createUser("locked_out_user", "secret_sauce", new String[] { "invalid_login" });
    }
}
