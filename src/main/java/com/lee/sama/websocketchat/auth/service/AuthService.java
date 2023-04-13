package com.lee.sama.websocketchat.auth.service;

import com.lee.sama.websocketchat.auth.dto.*;
import com.lee.sama.websocketchat.auth.repositroty.RefreshTokenRepositroty;
import com.lee.sama.websocketchat.auth.repositroty.UserRepositroty;
import com.lee.sama.websocketchat.entitys.UserInfo;
import com.lee.sama.websocketchat.entitys.UserRule;
import com.lee.sama.websocketchat.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepositroty refreshTokenRepositroty;
    private final PasswordEncoder passwordEncoder;
    private final UserRepositroty userRepositroty;


    @Transactional
    public JoinResponse join(JoinRequest joinRequest){
        Optional<Boolean> aBoolean = userRepositroty.existsByEmail(joinRequest.getEmail());
        aBoolean.filter(v -> !v.booleanValue())
               .orElseThrow(() -> new RuntimeException("이메일 중복 됩니다"));
        UserInfo userInfo = joinRequest.toUserInfo(passwordEncoder);
        userInfo.haveAuthority(UserRule.RULE_USER);
        UserInfo save = userRepositroty.save(userInfo);
        return save.toJoinResponse();
    }


    @Transactional
    public TokenDto login(LoginRequest loginRequest) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toUserInfo().toAuthentication();
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 PrincipalDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto token = jwtTokenProvider.createToken(authentication);

        // 4. RefreshToken 저장
        RefreshTokenDto refreshToken = RefreshTokenDto.builder()
                .key(authentication.getName())
                .value(token.getRefreshToken())
                .build();

        refreshTokenRepositroty.save(refreshToken.toRefreshToken());

        // 5. 토큰 발급
        return token;
    }



}
