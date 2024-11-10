package com.test.automation.pageobjects;

import com.test.automation.utils.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.testng.Assert.assertEquals;

public class InventoryPage extends DriverManager {

//    public InventoryPage() {
//        PageFactory.initElements(getDriver(), this);
//    }

    @FindBy(css = ".title")
    private WebElement title;

    @FindBy(css = "a.shopping_cart_link")
    private WebElement cartLink;

    private WebElement addToCartBtn(String productName) {
        return getDriver().findElement(By.name("add-to-cart-%s".formatted(productName)));
    }

    public void checkTitle(String expectedTitle) {
        assertEquals(title.getText(), expectedTitle);
    }

    public void addProductsToCart(String[] products) {
        for (String product : products) {
            addToCartBtn(product.trim()).click();
//            product.
        }
        cartLink.click();
    }
}
