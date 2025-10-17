package com.api.backend.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateReqDTO {

    @Positive
    private Integer userId;

    @NotBlank
    private String userPassword;

    @NotBlank
    private String userNm;
}
