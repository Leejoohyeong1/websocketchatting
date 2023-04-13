package com.lee.sama.websocketchat.auth.repositroty;

import com.lee.sama.websocketchat.entitys.RefreshToken;
import com.lee.sama.websocketchat.entitys.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepositroty extends JpaRepository<RefreshToken,Long> {
    
}
