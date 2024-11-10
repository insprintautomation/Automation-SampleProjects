Feature: Add User

  Background: Define URL
    * def dataGenerator = Java.type('helpers.DataGenerator')
    Given url apiUrl

    @addUser
  Scenario: Add User successfully
    Given path 'v2/user'
    * def requestJson = dataGenerator.getUserRequestJson()
    And request requestJson
    When method Post
    Then status 200
    And match response.code == 200
    And match response == {"code":200,"type":"#string","message":'#string'}
