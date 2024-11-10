package com.test.automation.cucumber.bdd.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "@reports/cucumber/failed_scenarios.txt",
        glue = "com.test.automation.cucumber.bdd.stepdefinitions",
        monochrome = true,
        plugin = {"html:reports/cucumber/cucumber.html",
        "json:reports/cucumber/cucumber.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
        "pretty"}
)
public class TestNGRunnerReRunFailed extends AbstractTestNGCucumberTests {

    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
