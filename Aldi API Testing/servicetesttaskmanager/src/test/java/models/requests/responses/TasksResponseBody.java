package models.requests.responses;

public class TasksResponseBody {

    private String id;
    private String message;
    private String title;
    private boolean completed;

    public TasksResponseBody(String id, String message, String title, boolean completed) {
        this.id = id;
        this.message = message;
        this.title = title;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}
