package com.petstore.api.automation.restassured.testng;

import com.api.automation.utils.PropertiesUtil;
import org.testng.annotations.BeforeSuite;

public class BaseSetup {

    @BeforeSuite
    public static void beforeSuite() {
        PropertiesUtil.loadProperties("config.properties");
    }
}
