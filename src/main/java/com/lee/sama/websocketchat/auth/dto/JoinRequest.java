package com.lee.sama.websocketchat.auth.dto;

import com.lee.sama.websocketchat.entitys.UserInfo;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class JoinRequest {

    private String email;

    private String nickName;

    private String password;

    public JoinRequest(String email, String nickName, String password) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
    }

    public UserInfo toUserInfo(PasswordEncoder passwordEncoder){
        return new UserInfo(this.email,this.nickName,passwordEncoder.encode(this.password));
    }
}
