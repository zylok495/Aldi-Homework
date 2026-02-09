package config;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import models.requests.PostTasksRequestBody;
import models.requests.builder.PostTasksRequestBodyBuilder;
import models.requests.responses.TasksResponseBody;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static config.TestConfig.CLEANUP_TASK;
import static config.TestConfig.CREATE_TASK;
import static constants.ErrorMessageConstants.*;
import static org.apache.hc.core5.http.HttpStatus.SC_CREATED;
import static org.apache.hc.core5.http.HttpStatus.SC_NO_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static testdata.TaskTestData.*;
import static testdata.UserTestData.USER_CREDENTIALS;
import static util.TokenGeneratorUtil.initializeUserToken;

public class BaseTest {

    protected TaskManagerBench testManagerBench;
    protected String createdTaskId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = TestConfig.BASE_URL;
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
        testManagerBench = new TaskManagerBench();
    }

    @AfterMethod(alwaysRun = true, onlyForGroups = CLEANUP_TASK)
    public void cleanupTask() {

        if (createdTaskId == null) {
            return;
        }

        Response response = testManagerBench.getTaskManagerService().deleteTask(createdTaskId);
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH).isEqualTo(SC_NO_CONTENT);
        createdTaskId = null;
    }

    @BeforeMethod(alwaysRun = true, onlyForGroups = CREATE_TASK)
    public void createTask() {
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

}
