package com.lee.sama.websocketchat.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinResponse {

    private Long userId;

    private String email;

    private String nickName;
}
