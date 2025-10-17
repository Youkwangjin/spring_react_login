package com.api.backend.domain.user.repository;

import com.api.backend.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Boolean existsByUserId(Integer userId);

    Boolean existsByUserEmail(String userEmail);

    Optional<UserEntity> findByUserIdAndUserLockAndUserSocial(Integer userId, Boolean userLock, Boolean userSocial);

    UserEntity findByUserEmailAndUserLockAndUserSocial(String userEmail, Boolean userLock, Boolean userSocial);
}
