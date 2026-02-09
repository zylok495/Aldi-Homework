package tests;

import config.BaseTest;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.requests.PutTasksRequestBody;
import models.requests.builder.PutTasksRequestBodyBuilder;
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

@Feature("Update existing task")
public class PutTasksByIdTests extends BaseTest {

    @Test(testName = "Update task - happy path",
            description = "Update task - happy path",
            groups = {REGRESSION, CREATE_TASK, CLEANUP_TASK})
    public void updateTasksHappyPath() {
        PutTasksRequestBody updateTaskRequestBody = PutTasksRequestBodyBuilder.aTask().withTitle(UPDATED_TITLE)
                .withCompleted(UPDATED_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().updateTask(createdTaskId, updateTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_OK);
        TasksResponseBody responseDto = response.as(TasksResponseBody.class);

        assertThat(responseDto.getId()).as(TASK_ID_MISMATCH).isEqualTo(createdTaskId);
        assertThat(responseDto.getMessage()).as(TASK_UPDATED_MESSAGE_MISMATCH).isEqualTo(TASK_UPDATED_MESSAGE);
        assertThat(responseDto.getTitle()).as(TITLE_PROPERTY_MISMATCH).isEqualTo(UPDATED_TITLE);
        assertThat(responseDto.isCompleted()).as(COMPLETED_PROPERTY_MISMATCH).isEqualTo(UPDATED_COMPLETED);
    }

    @Test(testName = "Update task with not existing task",
            description = "Update task with not existing task",
            groups = {REGRESSION})
    public void updateTasksWithNotExistingId() {
        initializeUserToken(USER_CREDENTIALS);
        PutTasksRequestBody updateTaskRequestBody = PutTasksRequestBodyBuilder.aTask().withTitle(UPDATED_TITLE)
                .withCompleted(UPDATED_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().updateTask(NOT_EXISTING_ID, updateTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_NOT_FOUND);
        validateErrorResponse(response, SC_NOT_FOUND, TASK_ID_NOT_FOUND_MESSAGE);
    }

    @Test(testName = "Try to update task with empty taskId",
            description = "Try to update task with empty taskId",
            groups = {REGRESSION})
    public void updateTasksWithEmptyId() {
        PutTasksRequestBody updateTaskRequestBody = PutTasksRequestBodyBuilder.aTask().withTitle(UPDATED_TITLE)
                .withCompleted(UPDATED_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().updateTask(EMPTY_VALUE, updateTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_BAD_REQUEST);
        validateErrorResponse(response, SC_BAD_REQUEST, ID_CAN_NOT_BE_EMPTY);
    }

    @Test(testName = "Try to update task with null taskId",
            description = "Try to update task with null taskId",
            groups = {REGRESSION})
    public void updateTasksWithNullId() {
        PutTasksRequestBody updateTaskRequestBody = PutTasksRequestBodyBuilder.aTask().withTitle(UPDATED_TITLE)
                .withCompleted(UPDATED_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().updateTask(NULL_VALUE, updateTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_BAD_REQUEST);
        validateErrorResponse(response, SC_BAD_REQUEST, ID_CAN_NOT_BE_NULL);
    }

    @Test(testName = "Try to update task with empty title",
            description = "Try to update task with empty title",
            groups = {REGRESSION, CREATE_TASK, CLEANUP_TASK})
    public void updateTasksWithEmptyTitle() {
        PutTasksRequestBody updateTaskRequestBody = PutTasksRequestBodyBuilder.aTask().withTitle(EMPTY_VALUE)
                .withCompleted(UPDATED_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().updateTask(createdTaskId, updateTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_BAD_REQUEST);
        validateErrorResponse(response, SC_BAD_REQUEST, EXPECTED_ERROR_MESSAGE_FOR_EMPTY_TITLE_PROPERTY);
    }

    @Test(testName = "Try to update task with null title",
            description = "Try to update task with null title",
            groups = {REGRESSION, CREATE_TASK, CLEANUP_TASK})
    public void updateTasksWithNullTitle() {
        PutTasksRequestBody updateTaskRequestBody = PutTasksRequestBodyBuilder.aTask().withTitle(NULL_VALUE)
                .withCompleted(UPDATED_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().updateTask(createdTaskId, updateTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_BAD_REQUEST);
        validateErrorResponse(response, SC_BAD_REQUEST, EXPECTED_ERROR_MESSAGE_FOR_NULL_TITLE_PROPERTY);
    }

    @Test(testName = "Try to update task without token",
            description = "Try to update task without token",
            groups = {REGRESSION})
    public void updateTasksWithoutToken() {
        PutTasksRequestBody updateTaskRequestBody = PutTasksRequestBodyBuilder.aTask().withTitle(UPDATED_TITLE)
                .withCompleted(UPDATED_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().updateTask(VALID_ID, updateTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_UNAUTHORIZED);
        validateErrorResponse(response, SC_UNAUTHORIZED, UNAUTHORIZED_ERROR_MESSAGE);
    }

    @Test(testName = "Try to update task with invalid token",
            description = "Try to update task with invalid token",
            groups = {REGRESSION})
    public void updateTasksWithInvalidToken() {
        initializeSystemToken(DUMMY_SERVICE_ID);
        PutTasksRequestBody updateTaskRequestBody = PutTasksRequestBodyBuilder.aTask().withTitle(UPDATED_TITLE)
                .withCompleted(UPDATED_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().updateTask(VALID_ID, updateTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_FORBIDDEN);
        validateErrorResponse(response, SC_FORBIDDEN, FORBIDDEN_ERROR_MESSAGE);
    }
}
