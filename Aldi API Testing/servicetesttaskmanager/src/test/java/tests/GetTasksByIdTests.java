package tests;

import config.BaseTest;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.requests.responses.TasksResponseBody;
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

@Feature("Get tasks by id")
public class GetTasksByIdTests extends BaseTest {

    @Test(testName = "Get tasks - happy path",
            description = "Get tasks - happy path",
            groups = {REGRESSION, CREATE_TASK, CLEANUP_TASK})
    public void getTasksHappyPath() {
        Response response = testManagerBench.getTaskManagerService().getTaskById(createdTaskId);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_OK);
        TasksResponseBody responseDto = response.as(TasksResponseBody.class);

        assertThat(responseDto.getId()).as(TASK_ID_MISMATCH).isEqualTo(createdTaskId);
        assertThat(responseDto.getTitle()).as(TITLE_PROPERTY_MISMATCH).isEqualTo(DEFAULT_TITLE);
        assertThat(responseDto.isCompleted()).as(COMPLETED_PROPERTY_MISMATCH).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test(testName = "Get tasks with non-existing id",
            description = "Get tasks with non-existing id",
            groups = {REGRESSION})
    public void getTasksWithNotExistingId() {
        initializeUserToken(USER_CREDENTIALS);
        Response response = testManagerBench.getTaskManagerService().getTaskById(NOT_EXISTING_ID);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_NOT_FOUND);
        validateErrorResponse(response, SC_NOT_FOUND, TASK_ID_NOT_FOUND_MESSAGE);
    }

    @Test(testName = "Get tasks with empty id",
            description = "Get tasks with empty id",
            groups = {REGRESSION})
    public void getTasksWithEmptyId() {
        initializeUserToken(USER_CREDENTIALS);
        Response response = testManagerBench.getTaskManagerService().getTaskById(EMPTY_VALUE);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_BAD_REQUEST);
        validateErrorResponse(response, SC_BAD_REQUEST, ID_CAN_NOT_BE_EMPTY);
    }

    @Test(testName = "Get tasks with null id",
            description = "Get tasks with null id",
            groups = {REGRESSION})
    public void getTasksWithNullId() {
        initializeUserToken(USER_CREDENTIALS);
        Response response = testManagerBench.getTaskManagerService().getTaskById(NULL_VALUE);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_BAD_REQUEST);
        validateErrorResponse(response, SC_BAD_REQUEST, ID_CAN_NOT_BE_NULL);
    }

    @Test(testName = "Get tasks without token",
            description = "Get tasks without token",
            groups = {REGRESSION})
    public void getTasksWithoutToken() {
        Response response = testManagerBench.getTaskManagerService().getTaskById(VALID_ID);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_UNAUTHORIZED);
        validateErrorResponse(response, SC_UNAUTHORIZED, UNAUTHORIZED_ERROR_MESSAGE);
    }

    @Test(testName = "Get tasks with invalid token",
            description = "Get tasks with invalid token",
            groups = {REGRESSION})
    public void getTasksWithInvalidToken() {
        initializeSystemToken(DUMMY_SERVICE_ID);
        Response response = testManagerBench.getTaskManagerService().getTaskById(VALID_ID);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_FORBIDDEN);
        validateErrorResponse(response, SC_FORBIDDEN, FORBIDDEN_ERROR_MESSAGE);
    }

}
