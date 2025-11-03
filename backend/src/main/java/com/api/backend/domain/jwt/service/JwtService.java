package com.api.backend.domain.jwt.service;

import com.api.backend.code.ApiErrorCode;
import com.api.backend.domain.jwt.dto.request.JwtRefreshReqDTO;
import com.api.backend.domain.jwt.dto.response.JwtTokenResDTO;
import com.api.backend.domain.jwt.entity.JwtRefreshEntity;
import com.api.backend.domain.jwt.repository.RefreshRepository;
import com.api.backend.exception.BackendException;
import com.api.backend.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtService {

    private JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    // 소셜 로그인 후 쿠기(Refresh) -> 헤더 방식으로 응답
    @Transactional
    public JwtTokenResDTO cookieHeader(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BackendException(ApiErrorCode.COOKIE_NOT_FOUND);
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            throw new BackendException(ApiErrorCode.REFRESH_TOKEN_MISSING);
        }

        Boolean isValid = jwtUtil.isValid(refreshToken, false);
        if (!isValid) {
            throw new BackendException(ApiErrorCode.REFRESH_TOKEN_INVALID);
        }

        String username = jwtUtil.getUsername(refreshToken);
        String userRole = jwtUtil.getRole(refreshToken);

        String newAccessToken = jwtUtil.createJWT(username, userRole, true);
        String newRefreshToken = jwtUtil.createJWT(username, userRole, false);

        // 기존 Refresh 토큰 DB 삭제 후 신규 추가
        JwtRefreshEntity newRefreshEntity = JwtRefreshEntity.builder()
                .userEmail(username)
                .jwtRefresh(newRefreshToken)
                .build();

        deleteRefresh(refreshToken);
        refreshRepository.flush(); // 같은 트랜잭션 내부라 : 삭제 -> 생성 문제 해결
        refreshRepository.save(newRefreshEntity);

        // 기존 쿠키 제거
        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        response.addCookie(refreshCookie);

        return new JwtTokenResDTO(newAccessToken, newRefreshToken);
    }

    // Refresh 토큰으로 Access 토큰 재발급 로직
    @Transactional
    public JwtTokenResDTO refreshRotate(JwtRefreshReqDTO request) {
        final String refreshToken = request.getRefreshToken();

        // 토큰 검증
        Boolean isValid = jwtUtil.isValid(refreshToken, false);
        if (!isValid) {
            throw new BackendException(ApiErrorCode.REFRESH_TOKEN_INVALID);
        }

        // RefreshEntity 존재 확인 (화이트리스트)
        if (!existRefresh(refreshToken)) {
            throw new BackendException(ApiErrorCode.REFRESH_TOKEN_INVALID);
        }

        String username = jwtUtil.getUsername(refreshToken);
        String userRole = jwtUtil.getRole(refreshToken);

        String newAccessToken = jwtUtil.createJWT(username, userRole, true);
        String newRefreshToken = jwtUtil.createJWT(username, userRole, false);

        // 기존 Refresh 토큰 DB 삭제 후 신규 추가
        JwtRefreshEntity newRefreshEntity = JwtRefreshEntity.builder()
                .userEmail(username)
                .jwtRefresh(newRefreshToken)
                .build();

        deleteRefresh(refreshToken);
        refreshRepository.flush(); // 같은 트랜잭션 내부라 : 삭제 -> 생성 문제 해결
        refreshRepository.save(newRefreshEntity);

        return new JwtTokenResDTO(newAccessToken, newRefreshToken);
    }

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
