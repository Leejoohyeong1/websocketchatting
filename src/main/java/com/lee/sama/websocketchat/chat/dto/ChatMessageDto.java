package com.lee.sama.websocketchat.chat.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type;
//  채팅방 ID
    private String roomId;
    //보내는 사람
    private String sender;
    //내용

    private String message;

    @Override
    public String toString() {
        return "ChatMessageDto{" +
                "type=" + type +
                ", roomId='" + roomId + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}