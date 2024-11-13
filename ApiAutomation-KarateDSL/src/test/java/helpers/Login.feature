Feature: Login

  Scenario: Login User
    Given url apiUrl
    And path 'v2/user/login'
    And param username = 'aldo.volkman'
    And param password = 'h%3Dy9hOEV'
    When method Get
    Then status 200
    * def sessionId = response.message.split(":")[1]
    * print "session id: " + sessionId
