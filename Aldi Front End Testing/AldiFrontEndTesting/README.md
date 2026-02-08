# Aldi Login Front End Tests Documentation

This project is an end-to-end UI test automation framework for the Aldi US website.
The framework is built with the following tools:
- **SLF4J + Logback** → logging during test execution
- **TestNG** → test runner and lifecycle management (Regression.xml is the test suite)
- **Allure Reporting** → rich HTML test reports
  - Report can be generated with: `allure serve allure-results`
- **Playwright (Java)** → browser automation
- **Maven** -> dependency management tool


### TestConfig.java
Centralized configuration class containing environment specific values such as the base URL.  
This approach keeps test data separated from test logic.

### BaseTest
The `BaseTest` class is responsible for preparing the test environment.

It provides:
- Playwright initialization
- Browser and page lifecycle handling
- Screenshot capture on test failure
- Page Object initialization

During the `@BeforeMethod` setup:
1. A new Playwright instance is created
2. A browser is launched with predefined options
3. A new page is opened and navigated to the base URL
4. Page Objects are initialized

During `@AfterMethod` teardown:
- If a test fails, a screenshot is automatically captured and attached to the Allure report
- The browser and Playwright instances are properly closed

### UserData.java

This class stores reusable test credentials used across the test suite.

The goal of this class is to separate **test data** from **test logic**, which improves:
- readability
- maintainability
- reusability

Instead of hardcoding credentials inside test methods, they are centralized in a dedicated class.

### LoginTests.java

This class contains end-to-end test scenarios related to the login functionality of the Aldi website.

The tests extend the `BaseTest` class, which provides:
- browser and page setup
- teardown and screenshot handling
- initialized Page Objects

All tests in this class belong to the **Regression** group and are executed from the `Regression.xml` TestNG suite.

### Regression.xml

This file defines the **TestNG test suite** used to execute the regression tests.

The suite is responsible for:
- selecting which tests should run
- grouping tests
- integrating Allure reporting

### logback.xml

This file configures the logging behaviour of the test automation framework.

The framework uses:
- **SLF4J** → logging API
- **Logback** → logging implementation

## Pages (Page Object Model)

The framework follows the Page Object Model (POM) design pattern.  
Each page class contains:
- Locators
- User interactions
- Assertions
- Allure steps for reporting

### DashBoardPage.java
Represents the Aldi homepage and header area.

Responsibilities:
- Opening the login modal
- Verifying the logged-in state via the "My Account" button

### LoginPage.java
Represents the login modal.

Responsibilities:
- Entering user credentials
- Submitting the login form
- Handling redirect-based login flow

## Running the tests

Run the TestNG suite:
```bash
mvn clean test
```

Generate Allure report:
```bash
allure serve allure-results
```