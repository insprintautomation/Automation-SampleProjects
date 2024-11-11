API Automation Karate DSL
===============

Introduction
------------

This is a sample test automation project for automating in-sprint/functional/regression tests for the Sample **PetStore** API Application. The tests are built using **JAVA**, **Karate DSL**, and **Maven**.

**Karate DSL** is a popular open-source framework for API testing that is built on top of **Cucumber**. It allows you to write tests in a simple, readable domain-specific language (DSL) with minimal setup. Karate integrates both API testing and UI testing in one framework, offering powerful features like performance testing, mocking, and data-driven testing.

Installation
------------
**Prerequisites**

- **JDK 11+** to run Java-based tests
- **Maven** for dependency management and running tests
- **IDE** Intellij or other

**Clone the Repository**

`git clone https://github.com/insprintautomation/Automation-SampleProjects.git
cd Automation-SampleProjects/ApiAutomation-KarateDSL`

**Install Dependencies**

Make sure Maven is installed on your machine. Then, run the following command to install all the dependencies:
`mvn clean install -DskipTests`

Karate Configuration
-----
Create a karate-config.js file and configure the environment, API baseurl and base steps.
```javascript
function fn() {
  var env = karate.env; // get system property 'karate.env'
  karate.log('karate.env system property was:', env);
  if (!env) {
    env = 'dev';
  }
  var config = {
    apiUrl: 'https://petstore.swagger.io/'
  }
  let sessionId = karate.callSingle('classpath:helpers/Login.feature', config).sessionId
//  karate.configure('headers', {Authorization: 'Token ' + accessToken})
  return config;
}
```

Create a Test runner class PetstoreTest.
```java
package petstore;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetstoreTest {
    @Test
    void testParallel() {
        Results results = Runner.path("classpath:petstore")
                .outputCucumberJson(true)
                .parallel(5);
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
```

Sample Tests
-------

Below is simple test scripts for testing inventory and user endpoint in Petstore API. Refer Karate documentation for the step's usage.

**Inventory:** Performs a GET request on endpoint v2/store/inventory and validates response.
```gherkin
@smoke @regression
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
```

**User:** Performs a POST request on endpoint v2/user and validates response. Uses a custom DataGenerators JAVA class to get test data using Faker and generates request body.
```gherkin
@smoke @regression @addUser
Feature: Add User

  Background: Define URL
    * def dataGenerator = Java.type('helpers.DataGenerator')
    Given url apiUrl

  @story-test123 @in-sprint
  Scenario: Add User successfully
    Given path 'v2/user'
    * def requestJson = dataGenerator.getUserRequestJson()
    And request requestJson
    When method Post
    Then status 200
    And match response.code == 200
    And match response == {"code":200,"type":"#string","message":'#string'}
```

Data Generator
-----
DataGenerator.java uses Faker library to get random test data required to construct the request JSON for User account creation.
```java
package helpers;

import com.github.javafaker.Faker;
import net.minidev.json.JSONObject;
import java.security.SecureRandom;

public class DataGenerator {
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
  private static final int PASSWORD_LENGTH = 8;

  public static JSONObject getUserRequestJson() {
    Faker faker = new Faker();
    JSONObject json = new JSONObject();
    json.put("id", faker.number().randomNumber());
    json.put("username", faker.name().username());
    json.put("firstName", faker.name().firstName());
    json.put("lastName", faker.name().lastName());
    json.put("email", faker.internet().emailAddress());
    json.put("password", getRandomPassword());
    json.put("phone", faker.phoneNumber().cellPhone());
    json.put("userStatus", 0);
    return json;
  }

  private static String getRandomPassword() {
    SecureRandom random = new SecureRandom();
    StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      int index = random.nextInt(CHARACTERS.length());
      password.append(CHARACTERS.charAt(index));
    }
    return password.toString();
  }
}
```

Running Tests
-----
Add build step in pom.xml to run tests using maven-surefire-plugin.
```xml
<build>
        <testResources>
            <testResource>
                <directory>src/test/java</directory>
                <excludes>
                    <exclude>**/*.java</exclude>
                </excludes>
            </testResource>
        </testResources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${maven.surefire.version}</version>
                <configuration>
                    <argLine>-Dfile.encoding=UTF-8</argLine>
                </configuration>
            </plugin>
        </plugins>
</build>
```
**Using CommandLine:** Run maven command ``mvn clean test`` from the module ApiAutomation-KarateDSL.


References
-------------

- Karate Labs WebSite: https://www.karatelabs.io
- Karate Labs Github: https://github.com/karatelabs/karate
- Petstore API Swagger: https://petstore.swagger.io

Support and Contact
-------------------

- `Slack <http://slack.robotframework.org/>`_
- `Forum <https://forum.robotframework.org/>`_
- `robotframework-users
  <https://groups.google.com/group/robotframework-users/>`_ mailing list

Contributing
------------

Interested to contribute to Robot Framework? Great! In that case it is a good
start by looking at the `<CONTRIBUTING.rst>`__. If you
do not already have an issue you would like to work on, you can check
issues with `good new issue`__ and `help wanted`__ labels.

Remember also that there are many other tools and libraries in the wider
`Robot Framework ecosystem <http://robotframework.org>`_ that you can
contribute to!

__ https://github.com/robotframework/robotframework/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22
__ https://github.com/robotframework/robotframework/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22

License and Trademark
---------------------

Robot Framework is open source software provided under the `Apache License 2.0`__.
Robot Framework documentation and other similar content use the
`Creative Commons Attribution 3.0 Unported`__ license. Most libraries and tools
in the ecosystem are also open source, but they may use different licenses.

Robot Framework trademark is owned by `Robot Framework Foundation`_.

__ http://apache.org/licenses/LICENSE-2.0
__ http://creativecommons.org/licenses/by/3.0

.. |r| unicode:: U+00AE
