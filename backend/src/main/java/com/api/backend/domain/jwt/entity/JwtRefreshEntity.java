package com.api.backend.domain.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_JWT_REFRESH")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtRefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JWT_REFRESH_ID")
    private Integer jwtRefreshId;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "JWT_REFRESH", nullable = false, length = 512)
    private String jwtRefresh;

    @CreatedDate
    @Column(name = "JWT_CREATED", updatable = false)
    private LocalDateTime jwtCreated;
}
