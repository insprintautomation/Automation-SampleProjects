package com.petstore.api.automation.restassured.testng;

import com.api.automation.utils.ApiUtil;
import org.testng.annotations.Test;

import static io.restassured.http.Method.GET;

public class InventoryTest extends BaseSetup {

    //  Using ApiUtil wrapper methods which uses RestAssured
    @Test
    public void inventoryTest() {
        ApiUtil apiUtil = new ApiUtil();
        apiUtil.setBaseUrl("inventory");
        apiUtil.submitRequest(GET.name());
        apiUtil.verifyResponseStatus(200);
        apiUtil.verifyResponseBody("sold", "[>=]1");
        apiUtil.verifyResponseBody("pending", "[>=]0");
    }

}
