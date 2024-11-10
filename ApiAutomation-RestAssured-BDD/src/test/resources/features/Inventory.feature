@smoke
Feature: Get Inventory

  Background: Define URL
    Given I set baseurl for inventory service

  Scenario: Get Inventory details
    When I submit GET request
    Then I see response status 200
    And I see response matches for fields
      | sold      | [>=]1 |
      | pending   | [>=]0 |
      | available | [>=]0 |