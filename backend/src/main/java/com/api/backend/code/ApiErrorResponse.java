package com.api.backend.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiErrorResponse {

    private final HttpStatus httpStatus;

    private final String errorCode;

    private final String errorMsg;

    @Builder
    public ApiErrorResponse(HttpStatus httpStatus, String errorCode, String errorMsg) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
