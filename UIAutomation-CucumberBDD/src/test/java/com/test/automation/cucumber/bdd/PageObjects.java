package com.test.automation.cucumber.bdd;

import com.test.automation.pageobjects.CartPage;
import com.test.automation.pageobjects.CheckoutPage;
import com.test.automation.pageobjects.InventoryPage;
import com.test.automation.pageobjects.LoginPage;
import com.test.automation.utils.DriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class PageObjects {

    public PageObjects() {
        initializePageObjects();
    }

    public LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected CartPage cartPage;
    protected CheckoutPage checkoutPage;

    public void initializePageObjects() {
        loginPage = PageFactory.initElements(DriverManager.getDriver(), LoginPage.class);;
        inventoryPage = PageFactory.initElements(DriverManager.getDriver(), InventoryPage.class);
        cartPage = PageFactory.initElements(DriverManager.getDriver(), CartPage.class);
        checkoutPage = PageFactory.initElements(DriverManager.getDriver(), CheckoutPage.class);
    }

}
