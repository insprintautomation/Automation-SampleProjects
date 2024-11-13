package com.web.automation.pageobjects;

import com.web.automation.utils.WebUtil;
import com.web.automation.utils.dataprovider.model.CustomerInfo;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static org.testng.Assert.assertEquals;

public class CheckoutPage extends WebUtil {

    private final By firstName = By.id("first-name");

    private final By lastName = By.cssSelector("input[data-test='lastName']");

    private final By zipCode = By.xpath("//input[@data-test='postalCode']");

    private final By continueBtn = By.className("submit-button");

    private final By finishBtn = By.name("finish");

    private final By title = By.className("title");

    private final By completeImg = By.cssSelector("[data-test='pony-express']");

    private final By completeHeader = By.className("complete-header");

    private final By completeDesc = By.className("complete-text");

    @Step("Enter Customer Information in Checkout page")
    public void enterCustomerInfo(CustomerInfo data) {
        typeValue(firstName, data.getFirstName());
        typeValue(lastName, data.getLastName());
        typeValue(zipCode, data.getZipCode());
        click(continueBtn);
    }

    @Step("Submit Order")
    public void submitOrder() {
        click(finishBtn);
    }

    @Step("Check Order Confirmation page")
    public void checkOrderConfirmation() {
        assertEquals(getText(title), "Checkout: Complete!");
        assertEquals(getValue(completeImg, "alt"), "Pony Express");
        assertEquals(getText(completeHeader), "Thank you for your order!");
        assertEquals(getText(completeDesc), "Your order has been dispatched, and will arrive just as fast as the pony can get there!");
        getScreenshot();
    }
}
