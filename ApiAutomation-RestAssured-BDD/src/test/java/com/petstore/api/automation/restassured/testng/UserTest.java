package com.petstore.api.automation.restassured.testng;

import com.api.automation.utils.DataGenerator;
import com.api.automation.utils.PropertiesUtil;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.empty;

public class UserTest extends BaseSetup {

    //  Using RestAssured directly
    @Test(groups = {"user", "in-sprint"})
    public void userTest() {
        String requestBody = DataGenerator.getUserRequestJson();
        String baseUri = PropertiesUtil.getProperty("prod.api.url");
        RestAssured.given()
                .log().all()
                .baseUri(baseUri)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("v2/user")
                .then()
                .log().all()
                .statusCode(200)
                .body("code", equalTo(200))
                .body("type", not(empty()))
                .body("message", not(empty()));
    }

}
