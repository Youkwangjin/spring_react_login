package com.api.backend.code;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponseBuilder {

    public static <T> ResponseEntity<ApiSuccessResponse<T>> success(HttpStatus httpStatus, T result, String resultMsg) {
        ApiSuccessResponse<T> response = ApiSuccessResponse.of(httpStatus, result, resultMsg);

        return ResponseEntity.status(httpStatus).body(response);
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> success(ApiSuccessCode successCode, T result) {
        return success(successCode.getHttpStatus(), result, successCode.getSuccessMag());
    }

    public static <T> ResponseEntity<ApiSuccessResponse<T>> success(ApiSuccessCode successCode) {
        return success(successCode.getHttpStatus(), null, successCode.getSuccessMag());
    }
}
