package com.test.automation.pageobjects;

import com.test.automation.utils.WebUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CartPage extends WebUtil {

    private final By checkoutBtn = By.id("checkout");

    @Step("Continue to Checkout Page")
    public void continueToCheckout() {
        click(checkoutBtn);
    }
}
