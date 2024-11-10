package com.test.automation.utils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.test.automation.utils.CommonUtil.getProperty;

@Slf4j
public class CSVDataUtil {

    private CSVDataUtil() {}

    private static List<Map<String, String>> accountsData;

    public static Map<String, String> getAccount(String accountType) {
        String environment = getProperty(Constants.ENVIRONMENT);
        List<Map<String, String>> accounts = accountsData.stream()
                .filter(entry -> entry.get(Constants.ACCOUNT_TYPE).equalsIgnoreCase(accountType)
                        && (entry.get(Constants.ENVIRONMENT).contains(environment)
                        || entry.get(Constants.ENVIRONMENT).equalsIgnoreCase("all")))
                .toList();
        return accounts.isEmpty() ? Map.of() : accounts.get(new SecureRandom().nextInt(accounts.size()));
    }

    /**
     * To load all test account's from csv file and data will be stored in a dictionary object.
     *
     * @param csvAccountFile optional - csv file path
     */
    public static void loadAccounts(String csvAccountFile) {
        try {
            File csvFile = new File(csvAccountFile);
            accountsData = readCSVFile(csvFile);
        } catch (IOException e) {
            log.warn(String.format("Exception in loading test accounts data. Error -> %s", e.getMessage()));
        }
    }

    /**
     * Reads CSV file content and returns the value as a List of Map object's.
     *
     * @param csvFile CSV file
     * @throws IOException when file not found or error in reading data
     */
    public static List<Map<String, String>> readCSVFile(File csvFile) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        MappingIterator<Map<String, String>> iterator = mapper.readerFor(Map.class)
                .with(schema)
                .readValues(csvFile);
        while (iterator.hasNext()) {
            data.add(iterator.next());
        }
        return data;
    }
}
