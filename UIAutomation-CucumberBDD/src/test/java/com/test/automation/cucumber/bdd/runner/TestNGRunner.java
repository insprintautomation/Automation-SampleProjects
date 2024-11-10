package com.test.automation.cucumber.bdd.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "classpath:features",
        glue = "com.test.automation.cucumber.bdd.stepdefinitions",
        tags = "@smoke",
        monochrome = true,
        dryRun = false,
        plugin = {"html:reports/cucumber/cucumber.html",
                "json:reports/cucumber/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "rerun:reports/cucumber/failed_scenarios.txt",
                "pretty"},
        publish = true
)
public class TestNGRunner extends AbstractTestNGCucumberTests {

    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
