@login @regression @smoke
Feature: Login

  As a customer, I should be able to successfully login with valid credentials, and see error for invalid credentials.

  Background:
    Given I am in login page

  @smoke @positive
  Scenario Outline: Login Successfully as <accountType> user
    When I login as a <accountType> user
    Then I should see inventory page
    @good
    Examples:
      | accountType |
      | standard    |
      | visual      |
    @bad
    Examples:
      | accountType |
      | problem     |
      | performance |
      | error       |

  @negative
  Scenario Outline: Login as <accountType> user and check the error message
    When I login as a <accountType> user
    Then I should see '<error>' message

    Examples:
      | accountType   | error                                       |
      | locked        | Sorry, this user has been locked out        |
      | empty         | Username is required                        |
      | passwordEmpty | Password is required                        |
      | invalid       | Username and password do not match any user |