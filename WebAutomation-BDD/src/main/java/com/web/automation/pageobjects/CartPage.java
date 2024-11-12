package com.web.automation.pageobjects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CartPage {

    @FindBy(id = "checkout")
    private WebElement checkoutBtn;

    public void continueToCheckout() {
        checkoutBtn.click();
    }
}
