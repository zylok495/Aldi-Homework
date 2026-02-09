package models.requests.responses;

public class ErrorResponseBody {

    private final String error;

    public ErrorResponseBody(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
