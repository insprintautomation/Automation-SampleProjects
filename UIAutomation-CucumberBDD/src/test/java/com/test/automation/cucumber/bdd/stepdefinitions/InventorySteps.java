package com.test.automation.cucumber.bdd.stepdefinitions;

import com.test.automation.cucumber.bdd.DataConstants;
import com.test.automation.cucumber.bdd.PageObjects;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InventorySteps extends PageObjects {

    @Then("I should see inventory page")
    public void checkInventoryPage() {
        inventoryPage.checkTitle(DataConstants.PRODUCTS);
    }

    @When("I add {string} products to cart")
    public void iAddProductsToCart(String product) {
        String[] products = product.split(",");
        inventoryPage.addProductsToCart(products);
    }
}
