package com.lee.sama.websocketchat.auth.service;

import com.lee.sama.websocketchat.auth.repositroty.UserRepositroty;
import com.lee.sama.websocketchat.entitys.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {


    private final UserRepositroty userRepositroty;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserInfo userInfo = userRepositroty.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 이메일입니다"));

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userInfo.getRule().toString());

        if(userInfo != null){
            return new User(
                    String.valueOf(userInfo.getUserId()),
                    userInfo.getPassword(),
                    Collections.singleton(grantedAuthority));
        }
        return null;
    }
}


