package com.api.backend.filter;

import com.api.backend.domain.user.dto.request.UserLoginReqDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomUserJsonLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String HTTP_METHOD_POST = "POST";

    private static final String CONTENT_TYPE = "application/json";

    private static final RequestMatcher DEFAULT_AUTH_MATCHER = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/login");

    private final ObjectMapper objectMapper;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    public CustomUserJsonLoginFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper, AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(DEFAULT_AUTH_MATCHER, authenticationManager);
        this.objectMapper = objectMapper;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (request.getContentType() == null || !CONTENT_TYPE.equals(request.getContentType())) {
            throw new AuthenticationServiceException("Unsupported content type: " + request.getContentType());
        }

        if (!request.getMethod().equals(HTTP_METHOD_POST)) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        UserLoginReqDTO loginReqData = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(),
                StandardCharsets.UTF_8), UserLoginReqDTO.class);

        String username = loginReqData.getUserEmail();
        String password = loginReqData.getUserPassword();

        if (username == null || password == null) {
            throw new AuthenticationServiceException("Missing required fields");
        }

        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        authenticationSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    }
}
