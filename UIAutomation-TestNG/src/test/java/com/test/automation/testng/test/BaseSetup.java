package com.test.automation.testng.test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.test.automation.testng.PageObjects;
import com.test.automation.utils.DriverManager;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import static com.test.automation.utils.CSVDataUtil.loadAccounts;
import static com.test.automation.utils.CommonUtil.getProperty;
import static com.test.automation.utils.CommonUtil.loadProperties;
import static com.test.automation.utils.DriverManager.loadUrl;

public class BaseSetup extends PageObjects {

    private final DriverManager driverManager;
    private static ExtentReports extentReport;

    public BaseSetup() {
        driverManager = new DriverManager();
    }

    @BeforeSuite
    public static void suiteSetup() {
        extentReport = new ExtentReports();
        ExtentSparkReporter extentSparkReport = new ExtentSparkReporter("reports/extent/extentReport.html");
        extentReport.attachReporter(extentSparkReport);
        loadProperties("src/test/resources/config.properties");
        String accountsCSV = getProperty("accountsCSV");
        loadAccounts(accountsCSV);
    }

    @BeforeMethod
    public void beforeMethod(ITestContext testContext) {
        extentReport.createTest(testContext.getName());
        driverManager.initializeDriver();
        loadUrl();
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        driverManager.stopDriver();
    }

    @AfterSuite
    public static void suiteTearDown() {
        extentReport.flush();
    }
}
