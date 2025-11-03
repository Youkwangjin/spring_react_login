package com.api.backend.domain.jwt.controller;

import com.api.backend.domain.jwt.dto.request.JwtRefreshReqDTO;
import com.api.backend.domain.jwt.dto.response.JwtTokenResDTO;
import com.api.backend.domain.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JwtApiController {

    private final JwtService jwtService;

    @PostMapping(value = "/jwt/exchange", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JwtTokenResDTO jwtExchangeApi(HttpServletRequest request, HttpServletResponse response) {

        return jwtService.cookieHeader(request, response);
    }

    @PostMapping(value = "/jwt/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JwtTokenResDTO jwtRefreshApi(@Valid @RequestBody JwtRefreshReqDTO request) {

        return jwtService.refreshRotate(request);
    }
}
