package com.api.backend.domain.user.service;

import com.api.backend.code.ApiErrorCode;
import com.api.backend.oauth2.CustomOAuth2User;
import com.api.backend.domain.user.dto.request.UserJoinReqDTO;
import com.api.backend.domain.user.dto.request.UserUpdateReqDTO;
import com.api.backend.domain.user.entity.UserEntity;
import com.api.backend.domain.user.repository.UserRepository;
import com.api.backend.domain.user.role.UserRoleType;
import com.api.backend.domain.user.role.UserSocialProviderType;
import com.api.backend.exception.BackendException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Boolean existsUserById(Integer userId) {
        return userRepository.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserEmailAndUserLockAndUserSocial(userEmail, false, false);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with email: " + userEmail);
        }

        return User.builder()
                .username(userEmail)
                .password(userEntity.getUserPassword())
                .roles(userEntity.getUserRoleType().name())
                .accountLocked(userEntity.getUserLock())
                .build();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 부모 메서드 호출
        OAuth2User oAuth2User = super.loadUser(userRequest);

        UserSocialProviderType provider =
                UserSocialProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        String providerId;
        String email;
        String name;

        if (provider == UserSocialProviderType.NAVER) {
            Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
            providerId = String.valueOf(response.get("id"));
            email = (String) response.get("email");
            name = (String) response.get("name");

        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다.");
        }

        UserEntity user = null;

        // 1차 조회 : (provider, providerId)
        Optional<UserEntity> optionalUser =
                userRepository.findByUserSocialProviderTypeAndUserSocialId(provider, providerId);

        // 없으면 email 기반으로 보조 조회
        if (optionalUser.isEmpty() && email != null) {
            optionalUser = userRepository.findByUserEmailAndUserSocialTrue(email);
        }

        // 존재할 경우 → 정보 업데이트
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            userRepository.save(user);
        }
        // 없으면 신규 가입
        else {
            user = UserEntity.builder()
                    .userEmail(email)
                    .userPassword("")
                    .userLock(false)
                    .userSocial(true)
                    .userSocialProviderType(provider)
                    .userSocialId(providerId)
                    .userRoleType(UserRoleType.USER)
                    .userNm(name)
                    .build();

            userRepository.save(user);
        }
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRoleType().name()));

        String principalName = provider.name() + ":" + providerId;

        return new CustomOAuth2User(oAuth2User.getAttributes(), authorities, principalName);
    }

    @Transactional
    public Integer createUser(UserJoinReqDTO request) {
        final String userEmail = request.getUserEmail();
        Boolean existsUserEmail = userRepository.existsByUserEmail(userEmail);
        if (existsUserEmail) {
            throw new BackendException(ApiErrorCode.USER_ALREADY_EXISTS);
        }

        final String userPassword = request.getUserPassword();
        final String encryptedPassword = passwordEncoder.encode(userPassword);
        final String userNm = request.getUserNm();
        final Boolean userLock = false;
        final Boolean userSocial = false;
        final UserRoleType userRoleType = UserRoleType.USER;
        final LocalDateTime userCreated = LocalDateTime.now();

        UserEntity userEntity = UserEntity.builder()
                .userEmail(userEmail)
                .userPassword(encryptedPassword)
                .userNm(userNm)
                .userLock(userLock)
                .userSocial(userSocial)
                .userRoleType(userRoleType)
                .userCreated(userCreated)
                .build();

        return userRepository.save(userEntity).getUserId();
    }

    @Transactional
    public Integer updateUser(UserUpdateReqDTO request) {
        final Integer userId = request.getUserId();
        UserEntity userEntity = userRepository.findByUserIdAndUserLockAndUserSocial(userId, false, false)
                .orElseThrow(() -> new BackendException(ApiErrorCode.USER_FOUND_ERROR));

        final String existingUserPassword = userEntity.getUserPassword();
        final String userPassword = request.getUserPassword();
        if (StringUtils.isBlank(userPassword) || !passwordEncoder.matches(existingUserPassword, userPassword)) {
            throw new BackendException(ApiErrorCode.USER_PASSWORD_ERROR);
        }

        userEntity.updateUser(request);

        return userRepository.save(userEntity).getUserId();
    }
}
