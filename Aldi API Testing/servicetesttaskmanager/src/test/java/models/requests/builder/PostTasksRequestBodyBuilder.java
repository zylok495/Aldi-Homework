package models.requests.builder;

import models.requests.PostTasksRequestBody;

public class PostTasksRequestBodyBuilder {

    private String title = "Default task";
    private boolean completed = false;

    public PostTasksRequestBodyBuilder() {

    }

    public static PostTasksRequestBodyBuilder aTask() {
        return new PostTasksRequestBodyBuilder();
    }

    public PostTasksRequestBodyBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public PostTasksRequestBodyBuilder withCompleted(boolean completed) {
        this.completed = completed;
        return this;
    }

    public PostTasksRequestBody build() {
        PostTasksRequestBody body = new PostTasksRequestBody();
        body.setTitle(title);
        body.setCompleted(completed);
        return body;
    }



}
