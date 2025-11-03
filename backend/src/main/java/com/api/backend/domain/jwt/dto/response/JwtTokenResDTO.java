package com.api.backend.domain.jwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenResDTO {

    private String accessToken;
    private String refreshToken;
}
