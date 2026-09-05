package com.chat.ChatKarlore.Dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class ChatMessage {
    private String message;
    @JsonAlias({"senderId","sender"})
    private String sender;
    @JsonAlias({"receiverId","receiver"})
    private String receiver;
    private String date;
    private String time;
    private String type;
    private String content;
}
