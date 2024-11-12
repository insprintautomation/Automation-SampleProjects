package com.saucedemo.bdd.automation.test.stepdefinitions;

import com.saucedemo.bdd.automation.test.PageObjects;
import com.web.automation.utils.DriverManager;
import io.cucumber.java.*;

import static com.web.automation.utils.CSVDataUtil.loadAccounts;
import static com.web.automation.utils.CommonUtil.getProperty;
import static com.web.automation.utils.CommonUtil.loadProperties;

public class BaseSetup extends PageObjects {

    private final DriverManager driverManager;

    public BaseSetup(DriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @BeforeAll
    public static void suiteSetup() {
        loadProperties("src/test/resources/config.properties");
        String accountsCSV = getProperty("accountsCSV");
        loadAccounts(accountsCSV);
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        driverManager.initializeDriver();
    }

    @After
    public void afterScenario(Scenario scenario) {
        driverManager.stopDriver();
    }

    @AfterAll
    public static void suiteTearDown() {

    }
}
