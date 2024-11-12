package com.web.automation.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.web.automation.utils.DriverManager.getDriver;
import static org.testng.Assert.assertEquals;

public class InventoryPage {

    @FindBy(css = ".title")
    private WebElement title;

    @FindBy(css = "a.shopping_cart_link")
    private WebElement cartLink;

    // dynamic locator - creates locator at runtime based on product name provided
    private WebElement addToCartBtn(String productName) {
        return getDriver().findElement(By.name("add-to-cart-%s".formatted(productName)));
    }

    public void checkTitle(String expectedTitle) {
        assertEquals(title.getText(), expectedTitle);
    }

    // Adds list of products to cart and navigates to cart page
    public void addProductsToCart(String[] products) {
        for (String product : products) {
            addToCartBtn(product.trim()).click();
        }
        cartLink.click();
    }
}
