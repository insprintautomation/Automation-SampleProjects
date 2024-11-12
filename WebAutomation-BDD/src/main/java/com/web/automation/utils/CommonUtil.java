package com.web.automation.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class CommonUtil {

    @Getter
    private static Properties properties;

    public static void loadProperties(String propertiesFile) {
        properties = new Properties();
        try (FileInputStream dataFile = new FileInputStream(propertiesFile)) {
            properties.load(dataFile);
        } catch (IOException e) {
            log.error("Error in loading properties file: %s".formatted(propertiesFile), e);
        }
    }

    public static String getProperty(String propertyName) {
        String value = properties.getProperty(propertyName);
        log.info("%s value is: %s".formatted(propertyName, value));
        return System.getProperty(propertyName, value);
    }

    /**
     * Reads JSON file content and returns the value as a Map object.
     *
     * @param jsonFile JSON file
     * @throws FileNotFoundException when file not found
     */
    public static Map<String, Object> readJsonFile(File jsonFile) throws FileNotFoundException {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(new FileReader(jsonFile), Map.class);
    }

    public static boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static URL getValidURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
