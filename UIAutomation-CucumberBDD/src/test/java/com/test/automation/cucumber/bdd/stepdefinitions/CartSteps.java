package com.test.automation.cucumber.bdd.stepdefinitions;

import com.test.automation.cucumber.bdd.PageObjects;
import io.cucumber.java.en.And;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CartSteps extends PageObjects {

    @And("I proceed to checkout")
    public void proceedToCheckout() {
        cartPage.continueToCheckout();
    }

}
