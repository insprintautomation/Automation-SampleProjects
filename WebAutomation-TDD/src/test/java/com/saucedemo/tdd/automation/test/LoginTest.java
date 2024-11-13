package com.saucedemo.tdd.automation.test;

import com.saucedemo.tdd.automation.DataConstants;
import com.web.automation.utils.dataprovider.JsonDataProvider;
import com.web.automation.utils.dataprovider.model.TestData;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.web.automation.utils.Constants.JSON_DATA_PROVIDER;

public class LoginTest extends BaseSetup {

    @Test(groups = {DataConstants.SMOKE, DataConstants.REGRESSION, DataConstants.LOGIN},
            dataProvider = JSON_DATA_PROVIDER, dataProviderClass = JsonDataProvider.class)
    @Feature(DataConstants.LOGIN)
    public void loginSuccessTest(TestData data) {
        loginPage.login(data);
        inventoryPage.checkTitle(DataConstants.PRODUCTS);
    }

    @Test(groups = {DataConstants.SMOKE, DataConstants.REGRESSION, DataConstants.LOGIN},
            dataProvider = JSON_DATA_PROVIDER, dataProviderClass = JsonDataProvider.class)
    @Feature(DataConstants.LOGIN)
    public void loginFailureTest(TestData data) {
        loginPage.login(data);
        loginPage.checkError(data.getError());
    }

}
