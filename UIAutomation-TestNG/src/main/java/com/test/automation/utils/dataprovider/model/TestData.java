package com.test.automation.utils.dataprovider.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class TestData {
    private String accountType;
    private String userName;
    private String password;
    private String environment;
    private String error;
    private List<String> products;
    private CustomerInfo customerInfo;
}
