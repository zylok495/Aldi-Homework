package util;

import io.restassured.response.Response;
import models.requests.responses.ErrorResponseBody;

import static constants.ErrorMessageConstants.EXPECTED_ERROR_MESSAGE_SHOULD_BE_RIGHT;
import static constants.ErrorMessageConstants.STATUS_CODE_MISMATCH;
import static org.assertj.core.api.Assertions.assertThat;

public class ValidateErrorResponseUtil {

    public static void validateErrorResponse(Response response, int statusCode, String errorMessage) {
        assertThat(response.getStatusCode()).as(STATUS_CODE_MISMATCH, statusCode).isEqualTo(statusCode);
        ErrorResponseBody err = response.as(ErrorResponseBody.class);
        assertThat(err.getError()).as(EXPECTED_ERROR_MESSAGE_SHOULD_BE_RIGHT).isEqualTo(errorMessage);
    }
}
