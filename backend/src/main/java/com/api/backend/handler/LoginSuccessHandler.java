package com.api.backend.handler;

import com.api.backend.domain.jwt.service.JwtService;
import com.api.backend.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Qualifier("LoginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String userEmail = authentication.getName();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();

        // JWT 발급
        String accessToken = jwtUtil.createJWT(userEmail, userRole, true);
        String refreshToken = jwtUtil.createJWT(userEmail, userRole, false);

        // 발급한 Refresh 토큰 DB 테이블에 저장
        jwtService.addRefresh(userEmail, refreshToken);

        // 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format("{\"access_token\":\"%s\",\"refresh_token\":\"%s\"}", accessToken, refreshToken);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
