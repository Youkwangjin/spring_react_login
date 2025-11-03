package com.api.backend.domain.jwt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRefreshReqDTO {

    @NotBlank
    private String refreshToken;
}
