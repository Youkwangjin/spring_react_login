package com.api.backend.domain.user.entity;

import com.api.backend.domain.user.dto.request.UserUpdateReqDTO;
import com.api.backend.domain.user.role.UserRoleType;
import com.api.backend.domain.user.role.UserSocialProviderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_USER")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_PASSWORD", unique = true, nullable = false, updatable = false)
    private String userPassword;

    @Column(name = "USER_NM")
    private String userNm;

    @Column(name = "USER_LOCK", nullable = false)
    private Boolean userLock;

    @Column(name = "USER_SOCIAL", nullable = false)
    private Boolean userSocial;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_SOCIAL_PROVIDER_TYPE")
    private UserSocialProviderType userSocialProviderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", nullable = false)
    private UserRoleType userRoleType;

    @CreatedDate
    @Column(name = "USER_CREATED", updatable = false)
    private LocalDateTime userCreated;

    @LastModifiedDate
    @Column(name = "USER_UPDATED")
    private LocalDateTime userUpdated;

    public void updateUser(UserUpdateReqDTO request) {
        this.userNm = request.getUserNm();
    }
}
