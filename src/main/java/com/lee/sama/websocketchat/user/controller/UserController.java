package com.lee.sama.websocketchat.user.controller;

import com.lee.sama.websocketchat.user.controller.dto.JoinRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {


    @RequestMapping(method = RequestMethod.GET,path = "/home")
    public String home(){
        return "home3";
    }


    @RequestMapping(value = "/join" ,method = RequestMethod.GET)
    public @ResponseBody ResponseEntity signup(@RequestBody JoinRequest joinRequest) {
        return null;
//        return ResponseEntity.ok(authService.signup(memberRequestVO));
    }





}