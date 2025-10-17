package com.api.backend.domain.jwt.service;

import com.api.backend.domain.jwt.entity.JwtRefreshEntity;
import com.api.backend.domain.jwt.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshRepository refreshRepository;

    // 소셜 로그인 후 쿠기(Refresh) -> 헤더 방식으로 응답

    // Refresh 토큰으로 Access 토큰 재발급 로직

    // JWT Refresh 토큰 발급 후 저장 메서드
    @Transactional
    public void addRefresh(String userEmail, String refreshToken) {
        JwtRefreshEntity jwtRefreshEntity = JwtRefreshEntity.builder()
                .userEmail(userEmail)
                .jwtRefresh(refreshToken)
                .build();

        refreshRepository.save(jwtRefreshEntity);
    }

    // JWT Refresh 존재 확인 메서드
    public Boolean existRefresh(String jwtRefresh) {
        return refreshRepository.existsByJwtRefresh(jwtRefresh);
    }

    // JWT Refresh 토큰 삭제 메서드
    @Transactional
    public void deleteRefresh(String jwtRefresh) {
        refreshRepository.deleteByJwtRefresh(jwtRefresh);
    }

    // 특정 유저 Refresh 토큰 모두 삭제 (회원 탈퇴)
    @Transactional
    public void deleteRefreshByUserEmail(String userEmail) {
        refreshRepository.deleteByUserEmail(userEmail);
    }
}
