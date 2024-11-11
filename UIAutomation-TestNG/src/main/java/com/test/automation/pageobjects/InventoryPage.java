package com.test.automation.pageobjects;

import com.test.automation.utils.WebUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class InventoryPage extends WebUtil {

    private By title = By.cssSelector(".title");

    private By cartLink = By.cssSelector("a.shopping_cart_link");

    private WebElement addToCartBtn(String productName) {
        return getDriver().findElement(By.name("add-to-cart-%s".formatted(productName)));
    }

    @Step("Check value inventory page title is '{expectedTitle}'")
    public void checkTitle(String expectedTitle) {
        assertEquals(getText(title), expectedTitle);
    }

    public void addProductsToCart(List<String> products) {
        for (String product : products) {
            addToCartBtn(product.trim()).click();
        }
        click(cartLink);
    }
}
