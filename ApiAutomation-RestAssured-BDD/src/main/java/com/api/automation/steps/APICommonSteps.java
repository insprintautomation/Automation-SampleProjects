package com.api.automation.steps;

import com.api.automation.utils.LogUtil;
import com.api.automation.utils.ApiUtil;
import com.api.automation.utils.DataGenerator;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class APICommonSteps {

    private ApiUtil apiUtil;

    protected Scenario scenario;


    @Given("I set baseurl for {} service")
    public void setBaseurl(String apiName) {
        apiUtil = new ApiUtil();
        LogUtil.logStep("Given I set baseurl for %s service".formatted(apiName));
        apiUtil.setBaseUrl(apiName); }

    @And("I set {} parameters")
    public void setParameters(String parameterType, DataTable params) {
        LogUtil.logStep("Given I set %s parameters".formatted(parameterType));
        Map<String, String> inputParams = Objects.isNull(params)
                ? new HashMap<>()
                : new HashMap<>(params.asMap());
        apiUtil.setParams(parameterType, new HashMap<>(inputParams));
    }

    @And("I set headers")
    public void setHeaders(DataTable headers) {
        LogUtil.logStep("Given I set headers %s".formatted(headers));
        apiUtil.setHeaders(new HashMap<>(headers.asMap()));
    }

    @And("I set request JSON")
    public void setRequestBodyAsTable(DataTable requestBody) {
        LogUtil.logStep("Given I set request JSON %s".formatted(requestBody));
        apiUtil.setRequestJson(new HashMap<>(requestBody.asMap()));
    }

    @When("I submit {} request")
    public void submitRequest(String method) {
        LogUtil.logStep("When I submit %s request".formatted(method));
        apiUtil.submitRequest(method);
    }

    @Then("I see response status {}")
    public void verifyResponseStatus(Integer statusCode) {
        LogUtil.logStep("Then I see response status %s".formatted(statusCode));
        apiUtil.verifyResponseStatus(statusCode);
    }

    @Then("I see response matches for fields")
    public void verifyResponseBody(DataTable data) {
        LogUtil.logStep("Then I see matches for fields %s".formatted(data));
        Map<String, String> responseValidation = Objects.isNull(data)
                ? new HashMap<>()
                : new HashMap<>(data.asMap());
        apiUtil.verifyResponseBody(responseValidation);
    }

    @Then("I see response header value matches for headers")
    public void verifyResponseHeaders(DataTable data) {
        LogUtil.logStep("Then I see response header value matches for headers %s".formatted(data));
        apiUtil.verifyResponseHeaders(new HashMap<>(data.asMap()));
    }

    @Then("I check for response header names")
    public void verifyHeaderNames(DataTable data) {
        LogUtil.logStep("Then I check for response header names %s".formatted(data));
        apiUtil.verifyHeaderNames(new HashMap<>(data.asMap()));
    }

    @Then("I check all {} items")
    public void verifyResponseBodyAllItems(String jsonPath, DataTable data) {
        LogUtil.logStep("Then I check all %s items %s".formatted(jsonPath, data));
        apiUtil.verifyResponseAllItems(jsonPath, new HashMap<>(data.asMap()));
    }

    @Then("I save {} as {} from response")
    public void saveResponse(String jsonPath, String name) {
        LogUtil.logStep("Then I save %s as %s from response".formatted(jsonPath, name));
        apiUtil.saveResponse(jsonPath, name, false);
    }

    @Then("I get random {} from response & save as {}")
    public void saveResponseRandom(String jsonPath, String name) {
        LogUtil.logStep("Then I get random %s from response & save as %s".formatted(jsonPath, name));
        apiUtil.saveResponse(jsonPath, name, true);
    }

    @Then("I see response matches json schema {}")
    public void verifyJsonSchema(String fileName) {
        LogUtil.logStep("Then I see response matches json schema %s".formatted(fileName));
        apiUtil.verifyJsonSchema(fileName);
    }

    @And("I set request JSON string")
    public void setRequestBodyAsString(String requestBody) {
        LogUtil.logStep("Given I set request JSON string %n %s".formatted(requestBody));
        apiUtil.setRequestJson(requestBody);
    }

    @Given("I set request JSON from {}")
    public void setRequestBody(String requestJsonMethod) {
        LogUtil.logStep("Given I set request JSON from %s".formatted(requestJsonMethod));
        try {
            Method dataMethod = DataGenerator.class.getMethod(requestJsonMethod);
            Object requestJson = dataMethod.invoke(new DataGenerator());
            setRequestBodyAsString(String.valueOf(requestJson));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    @And("I set {} as {}")
    public void setValue(String name, String value) {
        LogUtil.logStep("Given I set %s as %s".formatted(name, value));
        apiUtil.saveValue(name, value);
    }

}