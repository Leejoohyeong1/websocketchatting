package com.lee.sama.websocketchat.user.repositroty;

import com.lee.sama.websocketchat.entitys.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositroty extends JpaRepository<UserInfo,Long> {

}
