package com.test.automation.testng;

import com.test.automation.pageobjects.CartPage;
import com.test.automation.pageobjects.CheckoutPage;
import com.test.automation.pageobjects.InventoryPage;
import com.test.automation.pageobjects.LoginPage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PageObjects {

    protected LoginPage loginPage = new LoginPage();
    protected InventoryPage inventoryPage = new InventoryPage();
    protected CartPage cartPage = new CartPage();
    protected CheckoutPage checkoutPage = new CheckoutPage();

}
