package com.chat.ChatKarlore.Dto.Type;

import lombok.Data;

@Data
public class TypingMessage {
    private String receiver;
    private String sender;
    private boolean typing;
}
