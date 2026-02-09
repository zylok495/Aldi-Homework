package models.requests.builder;

import models.requests.PutTasksRequestBody;

public class PutTasksRequestBodyBuilder {

    private String title = "Default task";
    private boolean completed = false;

    public PutTasksRequestBodyBuilder() {

    }

    public static PutTasksRequestBodyBuilder aTask() {
        return new PutTasksRequestBodyBuilder();
    }

    public PutTasksRequestBodyBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PutTasksRequestBodyBuilder withCompleted(boolean completed) {
        this.completed = completed;
        return this;
    }

    public PutTasksRequestBody build() {
        PutTasksRequestBody body = new PutTasksRequestBody();
        body.setTitle(title);
        body.setCompleted(completed);
        return body;
    }

}
