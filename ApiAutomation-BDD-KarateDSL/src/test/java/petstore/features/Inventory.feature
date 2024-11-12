@smoke @regression @inventory
Feature: Get Inventory

  Background: Define URL
    Given url apiUrl

    @in-sprint @story-test123
  Scenario: Get Inventory details
    Given path 'v2/store/inventory'
    When method Get
    Then status 200
    And assert response.sold >= 1
    And assert response.pending >= 0
    And assert response.available >= 0