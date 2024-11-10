package com.api.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    protected static final Properties PROPERTIES = new Properties();

    private void setProperties(String propertiesFile) {
        try (InputStream data = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            PROPERTIES.load(data);
        } catch (IOException e) {
            throw new RuntimeException("Error in loading properties file: %s".formatted(propertiesFile), e);
        }
    }

    public static void loadProperties(String propertiesFile) {
        new PropertiesUtil().setProperties(propertiesFile);
    }

    public static String getProperty(String propertyName) {
        return System.getProperty(propertyName, PROPERTIES.getProperty(propertyName));
    }
}
