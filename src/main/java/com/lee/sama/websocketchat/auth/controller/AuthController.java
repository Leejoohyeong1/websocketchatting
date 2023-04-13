package com.lee.sama.websocketchat.auth.controller;

import com.lee.sama.websocketchat.auth.dto.JoinRequest;
import com.lee.sama.websocketchat.auth.dto.LoginRequest;
import com.lee.sama.websocketchat.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

//    @CrossOrigin
    @RequestMapping(value = "/signup" ,method = RequestMethod.POST)
    public @ResponseBody ResponseEntity signup(@RequestBody JoinRequest joinRequest) {
        return ResponseEntity.ok(authService.join(joinRequest));
    }

//    @CrossOrigin
    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public @ResponseBody ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}