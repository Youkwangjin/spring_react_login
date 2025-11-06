package com.api.backend.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiErrorCode {

    USER_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED,     "1000", "이메일 또는 비밀번호가 올바르지 않습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT,                "1001", "이미 가입한 사용자입니다."),
    USER_PASSWORD_ERROR(HttpStatus.BAD_REQUEST,             "1002", "비밀번호가 일치하지 않습니다."),
    USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT,              "1003", "이미 사용 중인 이메일입니다."),
    USER_FOUND_ERROR(HttpStatus.NOT_FOUND,                  "2000", "존재하지 않는 사용자 입니다."),

    JWT_TOKEN_MISSING(HttpStatus.UNAUTHORIZED,              "3000", "JWT 토큰이 존재하지 않습니다."),
    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,              "3001", "유효하지 않은 JWT 토큰입니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,              "3002", "JWT 토큰이 만료되었습니다."),
    REFRESH_TOKEN_MISSING(HttpStatus.BAD_REQUEST,           "3003", "리프레시 토큰이 요청에 포함되어 있지 않습니다."),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED,          "3004", "유효하지 않은 리프레시 토큰입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,          "3005", "리프레시 토큰이 만료되었습니다."),
    COOKIE_NOT_FOUND(HttpStatus.BAD_REQUEST,                "3006", "쿠키가 존재하지 않습니다."),

    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST,               "9001", "잘못된 요청입니다."),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED,             "9002", "로그인 후 이용 가능합니다."),
    FORBIDDEN_ERROR(HttpStatus.FORBIDDEN,                   "9003", "접근 권한이 없습니다."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND,                   "9004", "요청하신 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED_ERROR(HttpStatus.METHOD_NOT_ALLOWED, "9005", "허용되지 않은 요청 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "9999", "서버 내부 오류가 발생하였습니다. 관리자에게 문의하세요."),
    ;

    private final HttpStatus httpStatus;

    private final String errorCode;

    private final String errorMsg;

    ApiErrorCode(HttpStatus httpStatus, String errorCode, String errorMsg) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
