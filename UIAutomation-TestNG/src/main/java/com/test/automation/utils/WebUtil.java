package com.test.automation.utils;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebUtil extends DriverManager {

    public WebElement getElement(By locator) {
        return getDriver().findElement(locator);
    }

    @Step("Type value '{value}' in element '{element}'")
    public void typeValue(By element, String value) {
        getElement(element).sendKeys(value);
    }

    @Step("Click on element '{element}'")
    public void click(By element) {
        getElement(element).click();
    }

    @Step("Get text value of element '{element}'")
    public String getText(By element) {
        return getElement(element).getText();
    }

    @Step("Get '{attribute}' attribute value of element '{element}'")
    public String getValue(By element, String attribute) {
        return getElement(element).getAttribute(attribute);
    }

}
