package com.saucedemo.bdd.automation.test.stepdefinitions;

import com.saucedemo.bdd.automation.test.PageObjects;
import io.cucumber.java.en.And;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CartSteps extends PageObjects {

    @And("I proceed to checkout")
    public void proceedToCheckout() {
        cartPage.continueToCheckout();
    }

}
