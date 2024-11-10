package com.test.automation.cucumber.bdd.stepdefinitions;

import com.test.automation.cucumber.bdd.PageObjects;
import com.test.automation.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import static com.test.automation.cucumber.bdd.DataConstants.custInfoData;

@Slf4j
public class CheckoutSteps extends PageObjects {

    @And("I enter customer information")
    public void enterCustomerInfo() {
        checkoutPage.enterCustomerInfo(custInfoData);
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
