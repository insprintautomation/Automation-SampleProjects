package com.test.automation.utils;

public class Constants {

    public static final String ACCOUNT_TYPE = "accountType";
    public static final String JSON_DATA_PROVIDER = "JsonDataProvider";
    public static final String JSON_DATA_FILE = System.getProperty("user.dir") + "/src/test/resources/data/%s.json";
    public static final String ENVIRONMENT = "environment";

    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI
    }
}
