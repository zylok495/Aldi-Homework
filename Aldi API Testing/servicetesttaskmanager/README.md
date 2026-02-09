# Task Manager API Test Automation

This project demonstrates a **REST API test automation framework** built
with:

-   Java\
-   REST Assured\
-   TestNG\
-   AssertJ\
-   Allure reporting

------------------------------------------------------------------------

# Project Goal

The test suite validates the following endpoints:

Method   Endpoint        Description
  -------- --------------- ----------------------
POST     `/tasks`        Create a new task
GET      `/tasks/{id}`   Retrieve task by id
PUT      `/tasks/{id}`   Update existing task
DELETE   `/tasks/{id}`   Delete task

------------------------------------------------------------------------

# Package Structure

## config

Contains test configuration and lifecycle management.

### BaseTest

Responsible for: - setting base URI - request/response logging - test
setup & cleanup hooks - creating test data via TestNG groups

TestNG groups used: - CREATE_TASK → creates test data before test -
CLEANUP_TASK → removes test data after test

------------------------------------------------------------------------

## services

### TaskManagerService

API client layer wrapping REST Assured calls.

Provides methods for: - createTask() - getTaskById() - updateTask() -
deleteTask()

------------------------------------------------------------------------

## models

### Request DTOs

Used to serialize request bodies with Jackson.

### Response DTOs

Used to deserialize API responses.

------------------------------------------------------------------------

## Builders

Implements **Builder Pattern** for test data creation.

------------------------------------------------------------------------

## tests

Contains TestNG test classes grouped by endpoint:

-   PostTasksTests → POST /tasks
-   GetTasksByIdTests → GET /tasks/{id}
-   PutTasksByIdTests → PUT /tasks/{id}
-   DeleteTasksByIdTests → DELETE /tasks/{id}

Each endpoint includes: - happy path tests - validation tests -
authorization tests - negative scenarios

------------------------------------------------------------------------

## testdata

Centralized test data used across tests.

------------------------------------------------------------------------

## util

Helper utilities used across the framework

------------------------------------------------------------------------

# Running the Tests

Run all tests with Maven:

mvn clean test

------------------------------------------------------------------------

# Reporting (Allure)

Generate Allure report:

mvn allure:serve

------------------------------------------------------------------------
