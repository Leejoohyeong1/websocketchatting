package com.lee.sama.websocketchat.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.sama.websocketchat.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달

    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDto message) {
        if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        template.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
//    ./chat/messagejkm,                 j
//    {"type":"ENTER","roomId":"1","sender":"Lee","message":"hihi"}

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDto message){

        log.info("message => {}",message);

        template.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
}
