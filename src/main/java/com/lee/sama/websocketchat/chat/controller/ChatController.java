package com.lee.sama.websocketchat.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    ArrayList<String> users = new ArrayList<String>();
//    http://localhost:8080/ws
//    /topic/chat/room/1
//    /chat/enterapp
//    {"type":"ENTER","roomId":"1","sender":"Lee","message":"hihi"}


    // 새로운 사용자가 웹 소켓을 연결할 때 실행됨
    // @EventListener은 한개의 매개변수만 가질 수 있다.
    @EventListener
    public void handleWebSocketConnecListener(SessionConnectEvent event) throws JsonProcessingException {
        StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());

        List<String> nativeHeader = headerAccesor.getNativeHeader("username");

//        for (String header : nativeHeader) {
//            log.info("header =>{}",header);
//        }
//        System.out.println("auth:" + headerAccesor.getNativeHeader("Authorization"));
        log.info("Received a new web socket connection");
        log.info("새로운 웹 소켓 연결을 받았습니다.");
    }

    // 사용자가 웹 소켓 연결을 끊으면 실행됨
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccesor.getSessionAttributes().get("username");

        if(username != null) {
            log.info("User Disconnected : " + username);
            log.info("연결이 종료된 유저: {}" + username);

            users.remove(username);
        }
    }

}