package com.api.backend.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiSuccessCode {

    USER_REGISTER_SUCCESS(HttpStatus.CREATED,   "0001", "회원가입이 정상적으로 완료되었습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK,          "0002", "그동안 이용해 주셔서 감사합니다."),
    USER_UPDATE_SUCCESS(HttpStatus.OK,          "0003", "회원정보가 성공적으로 수정되었습니다."),

    LOGIN_SUCCESS(HttpStatus.OK,                "0000", "로그인이 정상적으로 완료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String successCode;
    private final String successMag;

    ApiSuccessCode(HttpStatus httpStatus, String successCode, String successMag) {
        this.httpStatus = httpStatus;
        this.successCode = successCode;
        this.successMag = successMag;
    }
}