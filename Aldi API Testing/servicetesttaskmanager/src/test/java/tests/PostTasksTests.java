package tests;

import config.BaseTest;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import models.requests.PostTasksRequestBody;
import models.requests.builder.PostTasksRequestBodyBuilder;
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

@Feature("User creates task")
public class PostTasksTests extends BaseTest {

    @Test(testName = "Create task - happy path",
    description = "Create task - happy path",
    groups = {REGRESSION, CLEANUP_TASK})
    public void postTasksHappyPath() {
        initializeUserToken(USER_CREDENTIALS);
        PostTasksRequestBody createTaskRequestBody = PostTasksRequestBodyBuilder.aTask().withTitle(DEFAULT_TITLE)
                .withCompleted(DEFAULT_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().createTask(createTaskRequestBody);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_CREATED);
        TasksResponseBody responseDto = response.as(TasksResponseBody.class);
        createdTaskId = responseDto.getId();
        assertThat(responseDto.getId()).isNotNull();
        assertThat(responseDto.getMessage()).as(TASK_CREATED_MESSAGE_MISMATCH).isEqualTo(TASK_CREATED_MESSAGE);
        assertThat(responseDto.getTitle()).as(TITLE_PROPERTY_MISMATCH).isEqualTo(DEFAULT_TITLE);
        assertThat(responseDto.isCompleted()).as(COMPLETED_PROPERTY_MISMATCH).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test(testName = "Create task with null title - Bad Request",
            description = "Create task with null title - Bad Request",
            groups = {REGRESSION})
    public void postTasksWithNullTitleShouldBeBadRequest() {
        initializeUserToken(USER_CREDENTIALS);
        PostTasksRequestBody createTaskRequestBody = PostTasksRequestBodyBuilder.aTask()
                .withTitle(NULL_VALUE).withCompleted(DEFAULT_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().createTask(createTaskRequestBody);
        validateErrorResponse(response, SC_BAD_REQUEST, EXPECTED_ERROR_MESSAGE_FOR_NULL_TITLE_PROPERTY);
    }

    @Test(testName = "Create task with empty title - Bad Request",
            description = "Create task with empty title - Bad Request",
            groups = {REGRESSION})
    public void postTasksWithEmptyTitleShouldBeBadRequest() {
        initializeUserToken(USER_CREDENTIALS);
        PostTasksRequestBody createTaskRequestBody = PostTasksRequestBodyBuilder.aTask()
                .withTitle(EMPTY_VALUE).withCompleted(DEFAULT_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().createTask(createTaskRequestBody);
        validateErrorResponse(response, SC_BAD_REQUEST, EXPECTED_ERROR_MESSAGE_FOR_EMPTY_TITLE_PROPERTY);
    }

    // assumption that two same task with same title can not be existed.
    @Test(testName = "Create task with duplicate request - Bad Request",
            description = "Create task with duplicate request - Bad Request",
            groups = {REGRESSION, CREATE_TASK, CLEANUP_TASK})
    public void postTasksDuplicates() {
        PostTasksRequestBody secondCreateTaskRequestBody = PostTasksRequestBodyBuilder.aTask()
                .withTitle(DEFAULT_TITLE).withCompleted(DEFAULT_COMPLETED).build();
        Response secondResponse = testManagerBench.getTaskManagerService().createTask(secondCreateTaskRequestBody);
        validateErrorResponse(secondResponse, SC_BAD_REQUEST, EXPECTED_ERROR_MESSAGE_FOR_DUPLICATE_CREATION);
    }

    @Test(testName = "Invalid token test",
            description = "Invalid token test",
            groups = {REGRESSION})
    public void postTasksWithInvalidToken() {
        initializeSystemToken(DUMMY_SERVICE_ID);
        PostTasksRequestBody createTaskRequestBody = PostTasksRequestBodyBuilder.aTask()
                .withTitle(DEFAULT_TITLE).withCompleted(DEFAULT_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().createTask(createTaskRequestBody);
        validateErrorResponse(response, SC_FORBIDDEN, FORBIDDEN_ERROR_MESSAGE);
    }

    @Test(testName = "Missing token test",
            description = "Missing token test",
            groups = {REGRESSION})
    public void postTasksWithoutToken() {
        PostTasksRequestBody createTaskRequestBody = PostTasksRequestBodyBuilder.aTask()
                .withTitle(DEFAULT_TITLE).withCompleted(DEFAULT_COMPLETED).build();
        Response response = testManagerBench.getTaskManagerService().createTask(createTaskRequestBody);
        validateErrorResponse(response, SC_UNAUTHORIZED, UNAUTHORIZED_ERROR_MESSAGE);
    }
}
