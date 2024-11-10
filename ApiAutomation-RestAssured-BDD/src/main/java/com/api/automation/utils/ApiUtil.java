package com.api.automation.utils;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.api.automation.utils.PropertiesUtil.getProperty;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.testng.Assert.*;

@Slf4j
public class ApiUtil {

    private static final String JSON_SCHEMA_PATH = System.getProperty("user.dir") + "/src/test/resources/data/schema/%s.json";
    private static final String FORM = "form";
    private static final String QUERY = "query";
    private static final String PATH = "path";
    private static final String CONDITION_REGEX = "\\[(.*?)\\]";
    private static final String PARAMETER_REGEX = "\\{(.*?)\\}";
    private static final String JSONPATH_REGEX = "\\$\\{(.*?)\\}";
    private static final ThreadLocal<Map<String, String>> RUN_TIME_DATA = new ThreadLocal<>();

    private final RequestSpecification request;
    private final QueryableRequestSpecification requestQuery;
    private Response response;
    private JsonPath responseJsonPath;

    public ApiUtil() {
        PrintStream logFile = LogUtil.getLogFile();
        request = isNull(logFile) ?
                RestAssured.given().filters(new RequestLoggingFilter(), new ErrorLoggingFilter()) :
                RestAssured.given().filters(RequestLoggingFilter.logRequestTo(logFile), ErrorLoggingFilter.logErrorsTo(logFile));
        requestQuery = SpecificationQuerier.query(request);
    }

    /**
     * Adding the baseUri Property from the RequestSpecification Interface and also Returns the RequestLogSpecification
     * that allows you to log different parts of the RequestSpecification
     * <p>
     * {@link IOException}
     */
    public void setBaseUrl(String apiName) {
        LogUtil.log("-------------- Creating Request -----------------------");
        String basePath = getProperty(apiName);
        if (StringUtils.isEmpty(basePath)) {
            String error = "Base Path not exists for api name " + apiName;
            throw new IllegalArgumentException(error);
        }
        String environment = getProperty("environment");
        String apiUrl = getProperty(environment + ".api.url") + basePath;
        LogUtil.log("API Request URL: " + apiUrl);
        assertTrue(isUrlValid(apiUrl), "Invalid request url: '%s'.".formatted(apiUrl));
        request.baseUri(apiUrl);
    }

    /**
     * Gets the request url
     *
     * @return request url
     */
    public String getRequestUrl() {
        return requestQuery.getURI();
    }

    public boolean isUrlValid(String url) {
        return new UrlValidator().isValid(url);
    }

    /**
     * Adding the baseUrl Property from the RequestSpecification Interface
     *
     * @param baseUrl is the API URL
     */
    public void setBaseUri(String baseUrl) {
        request.baseUri(baseUrl);
    }

    /**
     * Adding the basepath Property from the RequestSpecification Interface
     *
     * @param basePath is the API endpoint
     */
    public void setBasePath(String basePath) {
        request.basePath(basePath);
    }

    /**
     * To add the header to the request specification
     *
     * @param name  is the key of the query parameter
     * @param value is the value of the query parameter
     */
    public void setHeader(String name, String value) {
        LogUtil.log(String.format("Adding request header: %s = %s", name, value));
        request.header(name, value);
    }

    /**
     * To add a list of headers to the request specification
     *
     * @param params is the key and value of the headers
     */
    public void setHeaders(Map<String, String> params) {
        params.entrySet().stream()
                .filter(v -> isNotEmpty(v.getValue()) && v.getValue().contains("{"))
                .forEach(v -> v.setValue(getParamValue(v.getValue())));
        LogUtil.log(String.format("Adding request headers: %s", params));
        request.headers(params);
    }

    /**
     * To add the query param/form param/path param to the request specification
     *
     * @param name  is the key of the query parameter
     * @param value is the value of the query parameter
     * @param type  is the type of the query/form/path parameter
     *              {@link IllegalArgumentException}
     */
    public void setParam(String type, String name, String value) throws IllegalArgumentException {
        LogUtil.log(String.format("Adding request %s parameter: %s = %s", type, name, value));
        switch (type.toLowerCase()) {
            case FORM -> request.formParam(name, value);
            case QUERY -> request.queryParam(name, value);
            case PATH -> request.pathParam(name, value);
            default -> throw new IllegalArgumentException("Invalid Parameter type" + type);
        }
    }

    /**
     * To add a list of query param/form param/path param to the request specification
     *
     * @param params is the key and value of the query/form/path parameters
     * @param type   is the type of the query/form/path parameters
     *               {@link IllegalArgumentException}
     */
    public void setParams(String type, Map<String, String> params) throws IllegalArgumentException {
        if (!params.isEmpty()) {
            params.entrySet().stream() // gets parameterized values
                    .filter(v -> isNotEmpty(v.getValue()) && v.getValue().contains("{"))
                    .forEach(v -> v.setValue(getParamValue(v.getValue())));
            LogUtil.log(String.format("Adding request %s parameters: %s", type, params));
            switch (type.toLowerCase()) {
                case FORM -> request.formParams(params);
                case QUERY -> request.queryParams(params);
                case PATH -> request.pathParams(params);
                default -> throw new IllegalArgumentException("Invalid Parameters type" + type);
            }
        } else {
            LogUtil.log(String.format("No request %s parameters added.", type));
        }
    }

    /**
     * To add request json body to the Rest Assured Request object.
     *
     * @param requestMap is the key and value of the request json
     */
    public void setRequestJson(Map<String, String> requestMap) {
        requestMap.entrySet().stream()
                .filter(v -> isNotEmpty(v.getValue()) && v.getValue().contains("{"))
                .forEach(v -> v.setValue(getParamValue(v.getValue())));
        String requestJson = new Gson().toJson(requestMap);
        LogUtil.log(String.format("Adding request JSON: %s", requestJson));
        request.header("Content-Type", "application/json");
        request.body(requestJson);
    }

    /**
     * To add request json body to the Rest Assured Request object.
     *
     * @param requestJson is the key and value of the request json
     */
    public void setRequestJson(String requestJson) {
        LogUtil.log(String.format("Adding request JSON: %s", requestJson));
        request.header("Content-Type", "application/json");
        request.body(requestJson);
    }

    /**
     * We are sending the request for different CRUD operations like POST, GET, PUT , DELETE, PATCH using RequestSpecifcation Interfaace
     * and extracting the response using the Response Interface
     *
     * @param method to evaluate the CRUD operations
     *               {@link IllegalArgumentException}
     */
    public void submitRequest(String method) throws IllegalArgumentException {
        LogUtil.log(String.format("Request URI: %s", requestQuery.getURI()));
        LogUtil.log(String.format("Submitting %s request", method.toUpperCase()));
        if (LogUtil.isConsoleLogEnabled()) {
            request.log().all();
        }
        response = switch (method.toUpperCase()) {
            case "GET" -> RestAssured.given(request).get();
            case "POST" -> RestAssured.given(request).post();
            case "PUT" -> RestAssured.given(request).put();
            case "DELETE" -> RestAssured.given(request).delete();
            case "PATCH" -> RestAssured.given(request).patch();
            default -> throw new IllegalArgumentException("Invalid method type" + method);
        };
        LogUtil.log("-------------- Response -----------------------");
        LogUtil.log(response.asPrettyString());
        if (LogUtil.isConsoleLogEnabled()) {
            response.then().log().all();
        }
        LogUtil.log("Response Time: " + response.getTime() + " ms");
        responseJsonPath = response.jsonPath();
    }

    /**
     * To check the response status code of the api request.
     *
     * @param statusCode Status code expected
     */
    public void verifyResponseStatus(int statusCode) {
        LogUtil.log("-------------- Response Validation -----------------------");
        LogUtil.log(String.format("Response status assertion: Actual = %s; Expected = %s", response.getStatusCode(), statusCode));
        assertEquals(response.getStatusCode(), statusCode, "Response Status Assertion:");
    }

    /**
     * To check the response time of the api request.
     *
     * @param thresholdTime Response time threshold
     */
    public void verifyResponseTime(int thresholdTime) {
        long responseTime = response.getTime();
        String message = "Response Time Assertion: Actual = %s; Expected = %s".formatted(responseTime, thresholdTime);
        LogUtil.log(message);
        assertTrue(responseTime <= thresholdTime, message);
    }

    /**
     * To get the response time of the api request.
     *
     * @return response time
     */
    public long getResponseTime() {
        return response.getTime();
    }

    /**
     * Extracting JSON Response with Rest Assured. To parse a JSON body, we shall use the JSONPath class and utilize the methods of this class
     * to obtain the value of a specific attribute
     *
     * @param data JSONPath value
     *             {@link JsonPathException}
     */
    public void verifyResponseBody(Map<String, String> data) throws JsonPathException {
        data.forEach((path, expected) -> {
            Object actual = getJsonPathValue(path);
            assertValue(actual, expected, "Response body assertion for jsonpath '%s':".formatted(path));
        });
    }

    /**
     * Extracting Response headers key and values with Rest Assured. Getting key and value of the response headers.
     *
     * @param headers headers value
     */
    public void verifyResponseHeaders(Map<String, String> headers) {
        headers.forEach((header, expected) -> {
            Object actual = response.getHeaders().getValues(header).toString();
            assertEquals(actual, expected, "Response assertion for header - '%s':".formatted(header));
        });
    }

    /**
     * Extracting Response header names with Rest Assured and checking if header names are present
     *
     * @param headerNames names
     */
    public void verifyHeaderNames(Map<String, String> headerNames) {
        headerNames.forEach((headerName, expected) -> {
            boolean actual = response.getHeaders().hasHeaderWithName(headerName);
            assertValue(actual, expected, "Response assertion for header - '%s':".formatted(headerName));
        });
    }

    /**
     * Extracts list of values from Response based on jsonPath provided, and validates fields value.
     *
     * @param jsonPath JSONPath value
     * @param data     field and expected value
     *                 {@link JsonPathException}
     */
    public void verifyResponseAllItems(String jsonPath, Map<String, String> data) throws JsonPathException {
        Object value = getJsonPathValue(jsonPath);
        assertNotNull(value, "Check value of json path %s is not null.".formatted(jsonPath));
        List<Object> items = value instanceof List ? (List<Object>) value : new ArrayList<>(List.of(value));
        int i = 0;
        for (Object item : items) {
            LogUtil.log(String.format("Validating => %s[%s]", jsonPath, i++));
            Map<String, Object> values = item instanceof Map ? (HashMap<String, Object>) item : new HashMap<>();
            data.forEach((path, expected) -> {
                Object actual = getValue(values, path);
                assertValue(actual, expected, "Item: %s. Response body assertion for jsonpath '%s':".formatted(items.indexOf(item), path));
            });
        }
    }

    private Object getValue(Map<String, Object> data, String jsonPath) {
        String[] paths = jsonPath.split("\\.");
        Object value = null;
        for (String path : paths) {
            Object tempData = data.get(path);
            if (tempData instanceof Map) {
                data = (Map<String, Object>) tempData;
            } else {
                value = data.get(path);
            }
        }
        return value;
    }

    /**
     * Asserts actual and expected values based on condition provide.
     *
     * @param actual   value from response
     * @param expected value contains conditions
     */
    private void assertValue(Object actual, String expected, String message) {
        expected = isNull(expected) ? "[null]" : getParamValue(expected);
        List<String> conditions = getRegexValue(expected, CONDITION_REGEX);
        boolean isActualNull = isNull(actual);
        String actualStr = isActualNull ? null : String.valueOf(actual);
        String updatedMessage = "%s  Expected = %s; Actual = %s.".formatted(message, expected, actual);
        expected = expected.replace("[contains]", "")
                .replace("[~]", "")
                .replace("[>=]", "")
                .replace("[<=]", "")
                .replace("[>]", "");
        LogUtil.log(message);
        if (!conditions.isEmpty()) {
            switch (conditions.get(0).toLowerCase()) {
                case "null" -> assertTrue(isActualNull, updatedMessage);
                case "notnull" -> assertFalse(isActualNull, updatedMessage);
                case "empty" -> assertTrue(StringUtils.isEmpty(actualStr) && actualStr.equals("[]"), updatedMessage);
                case "notempty" -> assertTrue(isNotEmpty(actualStr) && !actualStr.equals("[]"), updatedMessage);
                case "contains", "~" -> assertTrue((isActualNull ? "null" : actual.toString()).contains(expected), updatedMessage);
                case ">=" -> assertTrue((Integer) actual >= Integer.parseInt(expected), updatedMessage);
                case "<=" -> assertTrue((Integer) actual <= Integer.parseInt(expected), updatedMessage);
                case ">" -> assertTrue((Integer) actual > Integer.parseInt(expected), updatedMessage);
                case "integer" -> assertTrue(actual instanceof Integer, "%s. Checking value is Integer type.".formatted(updatedMessage));
                case "string" -> assertTrue(actual instanceof String, "%s. Checking value is String type.".formatted(updatedMessage));
                case "boolean" -> assertTrue(actual instanceof Boolean, "%s. Checking value is Boolean type.".formatted(updatedMessage));
                case "long" -> assertTrue(actual instanceof Long, "%s. Checking value is Long type.".formatted(updatedMessage));
                case "float" -> assertTrue(actual instanceof Float, "%s. Checking value is Float type".formatted(updatedMessage));
                case "array" -> assertTrue(actual instanceof List, "%s. Checking value is Array type.".formatted(updatedMessage));
                default -> assertEquals(actualStr, expected, message);
            }
        } else {
            assertEquals(actualStr, expected, message);
        }
    }

    /**
     * Will get the parameterized values.
     *
     * @param value value with parameters
     * @return parameterized value
     */
    public String getParamValue(String value) {
        return getParamValue(value, PARAMETER_REGEX);
    }

    public String getParamValue(String value, String regEx) {
        List<String> paramList = getRegexValue(value, regEx);
        String paramValue;
        for (String param : paramList) {
            paramValue = getRunTimeData(param);
            if (param.equalsIgnoreCase("randomint")) {
                paramValue = String.valueOf(new SecureRandom().nextInt());
            }
            if (nonNull(paramValue)) {
                value = value.replace(regEx.contains("\\$") ? "${" + param + "}" : "{" + param + "}", paramValue);
            }
        }
        return value;
    }

    /**
     * Extracting JSON Response with Rest Assured. To parse a JSON body, we shall use the JSONPath class and utilize the methods of this class
     * to obtain the value of a specific attribute
     *
     * @param jsonPath JSONPath value
     */
    public Object getJsonPathValue(String jsonPath) {
        return getJsonPathValue(jsonPath, Boolean.FALSE);
    }

    public Object getJsonPathValue(String jsonPath, boolean random) {
        assertTrue(isNotEmpty(jsonPath), "Check Json path: %s is not empty".formatted(jsonPath));
        jsonPath = getParamValue(jsonPath, JSONPATH_REGEX);
        String[] jsonPaths = jsonPath.split("/");
        String basePath = getBasePath(jsonPaths[0]);
        Object value = null;
        for (int i = 0; i < jsonPaths.length; i++) {
            jsonPath = i == 0 ? jsonPaths[i] : basePath + jsonPaths[i];
            LogUtil.log(String.format("Getting value of JSON path: %s ", jsonPath));
            value = responseJsonPath.get(jsonPath);
            if (random && value instanceof List) {
                var values = ((List<?>) value).stream()
                        .filter(v -> nonNull(v) && !v.toString().isEmpty())
                        .toList();
                value = values.isEmpty() ? null : values.get(new SecureRandom().nextInt(values.size()));
                value = value instanceof List ? ((List<?>) value).get(0) : value;
            }
            if (value != null) {
                break;
            }
        }
        LogUtil.log(String.format("Value for jsonpath: '%s' is: '%s'", jsonPath, value));
        return value;
    }

    private String getBasePath(String jsonPath) {
        String[] paths = jsonPath.split("\\.");
        StringBuilder basePath = new StringBuilder();
        for (int i = 0; i < paths.length - 1; i++) {
            basePath.append("%s.".formatted(paths[i]));
        }
        return basePath.toString();
    }

    /**
     * API request consists of BaseURL, Query Params and Submit Requests methods
     *
     * @param data API requests key and values
     */
    public void apiRequest(Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            final String key = entry.getKey();
            switch (key.toLowerCase()) {
                case "baseurl" -> setBaseUri((String) data.get(key));
                case "basepath" -> setBasePath((String) data.get(key));
                case QUERY -> setParams(key, (Map) data.get(key));
                default -> log.info(key + " is not used in apiRequest");
            }
        }
        submitRequest((String) data.get("method"));
    }

    /**
     * Gets value of field from the JSON response using JSONpath and stores in a field name.
     *
     * @param jsonPath  is the JSON path to extract value from response
     * @param fieldName is the value saved in Runtime
     * @param random    is the flag to get random value
     */
    public void saveResponse(String jsonPath, String fieldName, boolean random) {
        Object value = getJsonPathValue(jsonPath, random);
        setRunTimeData(fieldName, value == null ? null : value.toString());
    }

    /**
     * Saves a value in a key, to retrieve and use it in multiple places.
     *
     * @param name  to save and retrieve the value
     * @param value to be saved
     */
    public void saveValue(String name, String value) {
        setRunTimeData(name, value);
    }

    /**
     * To validate the JSON Schema file
     *
     * @param fileName is the JSON Schema name
     */
    public void verifyJsonSchema(String fileName) {
        LogUtil.log("Comparing Response with schema file: " + fileName);
        String pathName = String.format(JSON_SCHEMA_PATH, fileName);
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(new File(pathName)));
    }

    /**
     * Gets matching values based on regular expression
     *
     * @param value String value
     * @param regex String regex
     * @return List of values
     */
    private List<String> getRegexValue(final String value, final String regex) {
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(value);
        List<String> paramList = new ArrayList<>();
        while (matcher.find()) {
            paramList.add(matcher.group(1));
        }
        return paramList;
    }

    public static Map<String, String> getRunTimeData() {
        return RUN_TIME_DATA.get();
    }

    public static String getRunTimeData(String key) {
        return RUN_TIME_DATA.get() == null ? null : RUN_TIME_DATA.get().get(key);
    }

    public static void setRunTimeData(String key, String value) {
        RUN_TIME_DATA.get().put(key, value);
    }

    public static void removeRunTimeData(String key) {
        if (RUN_TIME_DATA.get() != null) {
            RUN_TIME_DATA.get().remove(key);
        }
    }

    public static boolean hasRunTimeData(String key) {
        return RUN_TIME_DATA.get() != null && RUN_TIME_DATA.get().containsKey(key);
    }

    public static void createRunTimeData() {
        RUN_TIME_DATA.set(new HashMap<>());
    }

    public static void deleteRunTimeData() {
        if (RUN_TIME_DATA.get() != null) {
            RUN_TIME_DATA.remove();
        }
    }
}