package com.web.automation.utils.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.web.automation.utils.dataprovider.model.TestData;
import com.web.automation.utils.CSVDataUtil;
import com.web.automation.utils.CommonUtil;
import com.web.automation.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
public class JsonDataProvider {

    /**
     * Data Provider gets data from JSON based on Test executed. Test will be executed for no of data sets available.
     *
     * @param testContext (ITestNGMethod) testng ITestNGMethod has various methods to get current test method executed
     * @return data list iterator
     */
    @DataProvider(name = Constants.JSON_DATA_PROVIDER, parallel = true)
    public Iterator<Object[]> jsonDataProvider(ITestNGMethod testContext) throws IOException {
        String testName = testContext.getMethodName();
        String jsonFileName = testContext.getRealClass().getSimpleName();
        File jsonFile = new File(String.format(Constants.JSON_DATA_FILE, jsonFileName));
        List<TestData> jsonDataList = getJsonDataArray(jsonFile, testName);
        if (jsonDataList.isEmpty()) {
            jsonDataList = getJsonDataArray(jsonFile, testName);
        }
        Collection<Object[]> dataList = new ArrayList<>();
        for (TestData jsonData : jsonDataList) {
            dataList.add(new Object[]{jsonData});
        }
        return dataList.iterator();
    }

    /**
     * Reads JSON file content and returns the value for key as a List of TestData object's. Used for JSON Array data.
     * Test data can be excluded using 'excludeTest' parameter.
     *
     * @param jsonFile JSON file
     * @param key      to get a specific JSONObject from JSON file
     * @throws IOException when file not found or unable to get data
     */
    private List<TestData> getJsonDataArray(File jsonFile, String key) throws IOException {
        Map<String, Object> jsonData = CommonUtil.readJsonFile(jsonFile);
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) jsonData.get(key);
        if (Objects.isNull(dataList)) {
            log.info(String.format("Data not exists for key '%s' in file %s.", key, jsonFile));
            return Collections.emptyList();
        }
        List<TestData> testDataList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        dataList.forEach(data -> {
            // integrates accounts.csv
            if (data.containsKey(Constants.ACCOUNT_TYPE)) {
                Map<String, String> accountData = CSVDataUtil.getAccount(data.get(Constants.ACCOUNT_TYPE).toString());
                if (!accountData.isEmpty()) {
                    data.putAll(accountData);
                }
            }
            testDataList.add(objectMapper.convertValue(data, TestData.class));
        });
        return testDataList;
    }
}
