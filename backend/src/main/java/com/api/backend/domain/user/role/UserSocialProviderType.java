package com.api.backend.domain.user.role;

import lombok.Getter;

@Getter
public enum UserSocialProviderType {

    NAVER("네이버"),
    GOOGLE("구글"),
    ;

    private final String description;

    UserSocialProviderType(String description) {
        this.description = description;
    }
}

