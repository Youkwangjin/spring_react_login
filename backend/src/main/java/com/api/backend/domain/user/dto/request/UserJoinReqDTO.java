package com.api.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserJoinReqDTO {

    @NotBlank
    private String userEmail;

    @NotBlank
    private String userPassword;

    @NotBlank
    private String userNm;
}
