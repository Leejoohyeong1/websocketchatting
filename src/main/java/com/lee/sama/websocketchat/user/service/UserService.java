package com.lee.sama.websocketchat.user.service;

import com.lee.sama.websocketchat.user.controller.dto.JoinRequest;
import com.lee.sama.websocketchat.user.controller.dto.JoinResponse;
import com.lee.sama.websocketchat.user.repositroty.UserRepositroty;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepositroty userRepositroty;

    public JoinResponse join(JoinRequest joinRequest){
        userRepositroty.save(null);
        return null;
    }

}
