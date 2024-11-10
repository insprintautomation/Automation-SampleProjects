package com.petstore.api.automation.bdd;

import com.api.automation.utils.PropertiesUtil;
import io.cucumber.java.BeforeAll;

public class BaseSetup {

    @BeforeAll
    public static void beforeAll() {
        PropertiesUtil.loadProperties("config.properties");
    }
}
