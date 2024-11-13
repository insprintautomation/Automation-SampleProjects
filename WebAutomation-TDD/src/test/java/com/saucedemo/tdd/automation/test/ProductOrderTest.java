package com.saucedemo.tdd.automation.test;

import com.web.automation.utils.dataprovider.JsonDataProvider;
import com.web.automation.utils.dataprovider.model.TestData;
import io.qameta.allure.Feature;
import org.testng.annotations.Test;

import static com.saucedemo.tdd.automation.DataConstants.*;
import static com.web.automation.utils.Constants.JSON_DATA_PROVIDER;

public class ProductOrderTest extends BaseSetup {

    @Test(groups = {SMOKE, REGRESSION, ORDER},
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
