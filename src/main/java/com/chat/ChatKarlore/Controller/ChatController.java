package com.chat.ChatKarlore.Controller;

import com.chat.ChatKarlore.Dto.ChatMessage;
import com.chat.ChatKarlore.Dto.ReadReceipt;
import com.chat.ChatKarlore.Dto.Type.TypingMessage;
import com.chat.ChatKarlore.Entity.Message;
import com.chat.ChatKarlore.Entity.MessageStatus;
import com.chat.ChatKarlore.Repository.MessageRepository;
import com.chat.ChatKarlore.Service.UserPresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;



@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserPresenceService userPresenceService;
    private final MessageRepository messageRepository;

    @MessageMapping("/private-message")
    public void privateMessage(ChatMessage message, SimpMessageHeaderAccessor accessor) {
        if (message.getReceiver()==null ||message.getReceiver().isBlank()){
            throw new IllegalArgumentException("receiver is required ");
        }
        String sender = String.valueOf( accessor.getSessionAttributes().get("username"));
        Message message1 = Message.builder().sender(sender).receiver(message.getReceiver())
                .content(message.getContent())
                .sentAt(LocalDateTime.now())
                .status(MessageStatus.SENT)
        .build();
        if (userPresenceService.isOnline(message.getReceiver())){
            message1.setStatus(MessageStatus.DELIVERED);} else {message1.setStatus(MessageStatus.SENT);}

        messageRepository.save(message1);
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/message", message);
    }
    @SendTo("/topic/message")
    public ChatMessage send(ChatMessage chatMessage) {

        return chatMessage;
    }
    @MessageMapping("/read")
    public void read(ReadReceipt readReceipt) {
        if (readReceipt.getMessageId()==null){
            throw new IllegalArgumentException("messageId is required");
        }
        Message message =messageRepository.findById(readReceipt.getMessageId()).orElseThrow(()->new IllegalArgumentException("Message not found:"+readReceipt.getMessageId()));
        message.setStatus(MessageStatus.READ);
        messageRepository.save(message);
        messagingTemplate.convertAndSendToUser(message.getSender(), "/queue/read", message);
    }
    @MessageMapping("/typing")
    public void typing(
            TypingMessage typingmessage, Principal principal) {
        String sender =  principal.getName();
        Map<String,Object> payload = new HashMap<>();
    payload.put("sender",sender);
    payload.put("typing",typingmessage.isTyping());
    messagingTemplate.convertAndSendToUser(typingmessage.getReceiver(),"/queue/typing",payload);
    }


}
