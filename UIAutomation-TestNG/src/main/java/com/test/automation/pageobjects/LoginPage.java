package com.test.automation.pageobjects;

import com.test.automation.utils.WebUtil;
import com.test.automation.utils.dataprovider.model.TestData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static org.testng.Assert.assertTrue;

public class LoginPage extends WebUtil {

    private final By userNameBox = By.id("user-name");

    private final By passwordBox = By.cssSelector("#password");

    private final By loginBtn = By.name("login-button");

    private final By error = By.cssSelector("[data-test='error'");

    @Step("Login as '{data.accountType}' user")
    public void login(TestData data) {
        typeValue(userNameBox, data.getUserName());
        typeValue(passwordBox, data.getPassword());
        click(loginBtn);
    }

    @Step("Check login page shows error '{expectedError}'")
    public void checkError(String expectedError) {
        String actualError = getText(error);
        assertTrue(actualError.contains(expectedError),
                "Check error displayed in login: '%s' has '%s'".formatted(actualError, expectedError));
    }
}
