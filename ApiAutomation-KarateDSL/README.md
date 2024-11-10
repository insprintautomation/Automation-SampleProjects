API Automation Karate DSL
===============


Introduction
------------

This is a sample test automation project for automating in-sprint/functional/regression tests for the Sample **PetStore** API Application. The tests are built using **JAVA**, **Karate DSL**, and **Maven**.

**Karate DSL** is a popular open-source framework for API testing that is built on top of **Cucumber**. It allows you to write tests in a simple, readable domain-specific language (DSL) with minimal setup. Karate integrates both API testing and UI testing in one framework, offering powerful features like performance testing, mocking, and data-driven testing.

Installation
------------
**Prerequisites**

- **JDK 11+** (to run Java-based tests)
- **Maven** (for dependency management and running tests)
- **IDE** Intellij or others
- 
**Clone the Repository**

`git clone https://github.com/insprintautomation/InSprintAutomation.git
cd ApiAutomation-KarateDSL`

**Install Dependencies**

Make sure Maven is installed on your machine. Then, run the following command to install all the dependencies:
`mvn install`

Sample Tests
-------

Below is a simple example test case for testing inventory endpoint in Petstore API.


Usage
-----

Tests (or tasks) are executed from the command line using the ``robot``
command or by executing the ``robot`` module directly like ``python -m robot`` .

The basic usage is giving a path to a test (or task) file or directory as an
argument with possible command line options before the path::

    robot tests.robot
    robot --variable BROWSER:Firefox --outputdir results path/to/tests/

Additionally, there is the ``rebot`` tool for combining results and otherwise
post-processing outputs::

    rebot --name Example output1.xml output2.xml

Run ``robot --help`` and ``rebot --help`` for more information about the command
line usage. For a complete reference manual see `Robot Framework User Guide`_.

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
