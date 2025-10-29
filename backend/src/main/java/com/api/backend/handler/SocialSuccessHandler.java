package com.api.backend.handler;

import com.api.backend.domain.jwt.service.JwtService;
import com.api.backend.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Qualifier("SocialSuccessHandler")
@RequiredArgsConstructor
public class SocialSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String userEmail = authentication.getName();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();

        String refreshToken = jwtUtil.createJWT(userEmail, userRole, false);

        jwtService.addRefresh(userEmail, refreshToken);

        // 응답
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(10);

        response.addCookie(refreshCookie);
        response.sendRedirect("http://localhost:5173/cookie");
    }
}
