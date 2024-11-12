package com.saucedemo.bdd.automation.test.stepdefinitions;

import com.saucedemo.bdd.automation.test.DataConstants;
import com.saucedemo.bdd.automation.test.PageObjects;
import com.web.automation.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckoutSteps extends PageObjects {

    @And("I enter customer information")
    public void enterCustomerInfo() {
        checkoutPage.enterCustomerInfo(DataConstants.custInfoData);
    }

    @And("I review & submit the order")
    public void iReviewSubmitTheOrder() {
        checkoutPage.submitOrder();
    }

    @Then("I should see order confirmation")
    public void iShouldSeeOrderConfirmation() {
        checkoutPage.checkOrderConfirmation();
        DriverManager.getScreenshot();
    }
}
