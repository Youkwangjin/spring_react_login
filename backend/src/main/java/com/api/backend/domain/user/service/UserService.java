package com.api.backend.domain.user.service;

import com.api.backend.code.ApiErrorCode;
import com.api.backend.domain.user.dto.request.UserJoinReqDTO;
import com.api.backend.domain.user.dto.request.UserUpdateReqDTO;
import com.api.backend.domain.user.entity.UserEntity;
import com.api.backend.domain.user.repository.UserRepository;
import com.api.backend.domain.user.role.UserRoleType;
import com.api.backend.exception.BackendException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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
