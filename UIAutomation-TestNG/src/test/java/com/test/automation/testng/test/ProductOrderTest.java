package com.test.automation.testng.test;

import com.test.automation.utils.dataprovider.JsonDataProvider;
import com.test.automation.utils.dataprovider.model.TestData;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.test.automation.testng.DataConstants.*;
import static com.test.automation.utils.Constants.JSON_DATA_PROVIDER;

public class ProductOrderTest extends BaseSetup {

    @Test(groups = {SMOKE, REGRESSION, LOGIN},
            dataProvider = JSON_DATA_PROVIDER, dataProviderClass = JsonDataProvider.class)
    @Feature(PRODUCT_ORDER)
    public void productOrderTest(TestData data) {
        loginPage.login(data);
        inventoryPage.addProductsToCart(data.getProducts());
        cartPage.continueToCheckout();
        checkoutPage.enterCustomerInfo(data.getCustomerInfo());
        checkoutPage.submitOrder();
        checkoutPage.checkOrderConfirmation();
    }

}
