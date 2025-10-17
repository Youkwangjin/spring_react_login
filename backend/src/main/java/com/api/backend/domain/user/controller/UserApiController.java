package com.api.backend.domain.user.controller;

import com.api.backend.code.ApiErrorResponse;
import com.api.backend.code.ApiResponseBuilder;
import com.api.backend.code.ApiSuccessCode;
import com.api.backend.code.ApiSuccessResponse;
import com.api.backend.domain.user.dto.request.UserJoinReqDTO;
import com.api.backend.domain.user.dto.request.UserUpdateReqDTO;
import com.api.backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping(value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> createUser(@Valid @RequestBody UserJoinReqDTO request) {
        Integer userId = userService.createUser(request);

        return ApiResponseBuilder.success(ApiSuccessCode.USER_REGISTER_SUCCESS, userId);
    }

    @PatchMapping(value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse<Object>> updateUser(@Valid @RequestBody UserUpdateReqDTO request) {
        Integer userId = userService.updateUser(request);

        return ApiResponseBuilder.success(ApiSuccessCode.USER_UPDATE_SUCCESS, userId);
    }
}
