package com.api.backend.domain.jwt.repository;

import com.api.backend.domain.jwt.entity.JwtRefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<JwtRefreshEntity, Integer> {

    Boolean existsByJwtRefresh(String jwtRefresh);

    void deleteByJwtRefresh(String jwtRefresh);

    void deleteByUserEmail(String userEmail);
}
