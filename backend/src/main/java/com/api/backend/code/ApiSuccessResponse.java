package com.api.backend.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiSuccessResponse<T> {

    private final HttpStatus httpStatus;

    private final T result;

    private final String resultMsg;

    @Builder
    public ApiSuccessResponse(HttpStatus httpStatus, T result, String resultMsg) {
        this.httpStatus = httpStatus;
        this.result = result;
        this.resultMsg = resultMsg;
    }

    public static <T> ApiSuccessResponse<T> of(HttpStatus httpStatus, T result, String resultMsg) {
        return new ApiSuccessResponse<>(httpStatus, result, resultMsg);
    }
}
