package com.lee.sama.websocketchat.auth.repositroty;

import com.lee.sama.websocketchat.entitys.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositroty extends JpaRepository<UserInfo,Long> {
    Optional<Boolean> existsByEmail(String email);
    Optional<UserInfo> findByEmail(String email);
}
