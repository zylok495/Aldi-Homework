package models.requests;

public class PostTasksRequestBody {

    private String title;
    private boolean completed;

    public PostTasksRequestBody() {
    }

    public PostTasksRequestBody(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
