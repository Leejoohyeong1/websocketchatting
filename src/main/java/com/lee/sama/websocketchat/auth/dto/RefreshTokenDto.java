package com.lee.sama.websocketchat.auth.dto;

import com.lee.sama.websocketchat.entitys.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class RefreshTokenDto {

    private String key;

    private String value;

    public RefreshTokenDto updateValue(String token) {
        this.value = token;
        return this;
    }

    public RefreshToken toRefreshToken(){
        return new RefreshToken( Long.parseLong(this.key),this.value);
    }

}


