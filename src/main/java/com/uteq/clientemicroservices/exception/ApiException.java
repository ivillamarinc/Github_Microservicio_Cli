package com.uteq.clientemicroservices.exception;

public class ApiException extends RuntimeException {
    private final ApiErrorCode code;

    public ApiException(ApiErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ApiErrorCode getCode() {
        return code;
    }
}
