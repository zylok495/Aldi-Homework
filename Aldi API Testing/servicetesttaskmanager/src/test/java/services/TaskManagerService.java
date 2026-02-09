package services;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.requests.PostTasksRequestBody;
import models.requests.PutTasksRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class TaskManagerService {
    
    private static final String TASKS = "/tasks";
    private static final String ID = "id";

    protected static final Logger log = LoggerFactory.getLogger(TaskManagerService.class);

    public Response getTaskById(String id) {
        log.info("GET /tasks/{} endpoint called", id);
        return given().contentType(ContentType.JSON)
                .pathParam(ID, id)
                .when().get(TASKS + "/{id}")
                .then().extract().response();
    }

    public Response createTask(PostTasksRequestBody postTasksRequestBody) {
        log.info("POST /tasks endpoint called");
        return given().contentType(ContentType.JSON)
                .body(postTasksRequestBody)
                .when().post(TASKS)
                .then().extract().response();
    }

    public Response updateTask(String id, PutTasksRequestBody putTasksRequestBody) {
        log.info("PUT /tasks/{} endpoint called", id);
        return given().contentType(ContentType.JSON)
                .pathParam(ID, id)
                .body(putTasksRequestBody)
                .when().put(TASKS + "/{id}")
                .then().extract().response();
    }

    public Response deleteTask(String id) {
        log.info("DELETE /tasks/{} endpoint called", id);
        return given()
                .pathParam(ID, id)
                .when().delete(TASKS + "/{id}")
                .then().extract().response();
    }
}
