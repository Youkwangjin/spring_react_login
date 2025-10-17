package com.api.backend.exception;

import com.api.backend.code.ApiErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(BackendException.class)
    public ResponseEntity<ApiErrorResponse> handleBackendException(BackendException e) {

        ApiErrorResponse response = ApiErrorResponse.builder()
                .httpStatus(e.getHttpStatus())
                .errorCode(e.getErrorCode())
                .errorMsg(e.getErrorMsg())
                .build();

        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
}
