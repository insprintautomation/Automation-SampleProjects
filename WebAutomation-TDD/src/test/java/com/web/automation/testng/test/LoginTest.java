package com.web.automation.testng.test;

import com.web.automation.utils.dataprovider.JsonDataProvider;
import com.web.automation.utils.dataprovider.model.TestData;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.web.automation.testng.DataConstants.*;
import static com.web.automation.utils.Constants.JSON_DATA_PROVIDER;

public class LoginTest extends BaseSetup {

    @Test(groups = {SMOKE, REGRESSION, LOGIN},
            dataProvider = JSON_DATA_PROVIDER, dataProviderClass = JsonDataProvider.class)
    @Feature(LOGIN)
    public void loginSuccessTest(TestData data) {
        loginPage.login(data);
        inventoryPage.checkTitle(PRODUCTS);
    }

    @Test(groups = {SMOKE, REGRESSION, LOGIN},
            dataProvider = JSON_DATA_PROVIDER, dataProviderClass = JsonDataProvider.class)
    @Feature(LOGIN)
    public void loginFailureTest(TestData data) {
        loginPage.login(data);
        loginPage.checkError(data.getError());
    }

}
