package tests;

import config.BaseTest;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static config.TestConfig.*;
import static constants.ErrorMessageConstants.*;
import static org.apache.hc.core5.http.HttpStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static testdata.TaskTestData.*;
import static testdata.UserTestData.USER_CREDENTIALS;
import static util.TokenGeneratorUtil.initializeSystemToken;
import static util.TokenGeneratorUtil.initializeUserToken;
import static util.ValidateErrorResponseUtil.validateErrorResponse;

@Feature("Delete task by id")
public class DeleteTasksByIdTests extends BaseTest {

    @Test(testName = "Delete task - happy path",
            description = "Delete task - happy path",
            groups = {REGRESSION, CREATE_TASK})
    public void deleteTasksHappyPath() {
        Response response = testManagerBench.getTaskManagerService().deleteTask(createdTaskId);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_NO_CONTENT);
        //Here DB check can be added to check if entity was deleted, or status changed to "DELETED" (soft delete / hard delete)
    }

    @Test(testName = "Try to delete task twice",
            description = "Try to delete task twice",
            groups = {REGRESSION, CREATE_TASK})
    public void deleteTasksTwice() {
        Response response = testManagerBench.getTaskManagerService().deleteTask(createdTaskId);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_NO_CONTENT);

        Response secondResponse = testManagerBench.getTaskManagerService().deleteTask(createdTaskId);
        assertThat(secondResponse.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_NOT_FOUND);
        validateErrorResponse(secondResponse, SC_NOT_FOUND, TASK_ID_NOT_FOUND_MESSAGE);
    }

    @Test(testName = "Try to delete a task with non-existing id",
            description = "Try to delete a task with non-existing id",
            groups = {REGRESSION})
    public void deleteTasksWithNonExistingId() {
        initializeUserToken(USER_CREDENTIALS);
        Response response = testManagerBench.getTaskManagerService().deleteTask(NOT_EXISTING_ID);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_NOT_FOUND);
        validateErrorResponse(response, SC_NOT_FOUND, TASK_ID_NOT_FOUND_MESSAGE);
    }

    @Test(testName = "Try to delete a task with empty id",
            description = "Try to delete a task with empty id",
            groups = {REGRESSION})
    public void deleteTasksWithEmptyId() {
        initializeUserToken(USER_CREDENTIALS);
        Response response = testManagerBench.getTaskManagerService().deleteTask(EMPTY_VALUE);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_BAD_REQUEST);
        validateErrorResponse(response, SC_BAD_REQUEST, ID_CAN_NOT_BE_EMPTY);
    }

    @Test(testName = "Try to delete a task with null id",
            description = "Try to delete a task with null id",
            groups = {REGRESSION})
    public void deleteTasksWithNullId() {
        initializeUserToken(USER_CREDENTIALS);
        Response response = testManagerBench.getTaskManagerService().deleteTask(NULL_VALUE);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_BAD_REQUEST);
        validateErrorResponse(response, SC_BAD_REQUEST, ID_CAN_NOT_BE_NULL);
    }

    @Test(testName = "Try to delete without token",
            description = "Try to delete a task without token",
            groups = {REGRESSION})
    public void deleteTasksWithoutToken() {
        Response response = testManagerBench.getTaskManagerService().deleteTask(VALID_ID);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_UNAUTHORIZED);
        validateErrorResponse(response, SC_UNAUTHORIZED, UNAUTHORIZED_ERROR_MESSAGE);
    }

    @Test(testName = "Try to delete with invalid token",
            description = "Try to delete a task with invalid token",
            groups = {REGRESSION})
    public void deleteTasksWithInvalidToken() {
        initializeSystemToken(DUMMY_SERVICE_ID);
        Response response = testManagerBench.getTaskManagerService().deleteTask(VALID_ID);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_FORBIDDEN);
        validateErrorResponse(response, SC_FORBIDDEN, FORBIDDEN_ERROR_MESSAGE);
    }
}
