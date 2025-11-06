package com.api.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserJoinReqDTO {

    @NotBlank
    @Size(max = 100)
    private String userEmail;

    @NotBlank
    private String userPassword;

    @NotBlank
    private String userNm;
}
