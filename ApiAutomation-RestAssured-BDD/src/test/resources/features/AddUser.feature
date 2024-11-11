@smoke @addUser
Feature: Add User

  Background: Define URL
    Given I set baseurl for user service

  @in-sprint @story-test123
  Scenario: Add User successfully
    Given I set request JSON from getUserRequestJson
    When I submit POST request
    Then I see response status 200
    And I see response matches for fields
      | code    | 200      |
      | type    | [string] |
      | message | [string] |
