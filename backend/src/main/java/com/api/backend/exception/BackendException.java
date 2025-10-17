package com.api.backend.exception;

import com.api.backend.code.ApiErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BackendException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final String errorCode;

    private final String errorMsg;

    public BackendException(ApiErrorCode errorCode) {
        super(errorCode.getErrorMsg());
        this.httpStatus = errorCode.getHttpStatus();
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = errorCode.getErrorMsg();
    }
}
