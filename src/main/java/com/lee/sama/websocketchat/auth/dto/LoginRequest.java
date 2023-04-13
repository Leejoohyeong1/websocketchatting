package com.lee.sama.websocketchat.auth.dto;


import com.lee.sama.websocketchat.entitys.UserInfo;
import lombok.Getter;

@Getter
public class LoginRequest {

    private String email;

    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserInfo toUserInfo(){
        return new UserInfo(this.email,this.password);
    }
}
