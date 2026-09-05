package com.chat.ChatKarlore.Service;

import com.chat.ChatKarlore.Entity.Message;
import com.chat.ChatKarlore.Entity.MessageStatus;
import com.chat.ChatKarlore.Entity.User;
import com.chat.ChatKarlore.FIrebase.FcmService;
import com.chat.ChatKarlore.Repository.MessageRepository;
import com.chat.ChatKarlore.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;
    public Message save(Message message) throws Exception {
        message.setSentAt(LocalDateTime.now());
        message.setStatus(MessageStatus.SENT);
        if(message.getReceiver()==null||message.getReceiver().isBlank()){throw new IllegalArgumentException("receiver is required");}
        Long receiverId;
        try{receiverId= Long.parseLong(message.getReceiver());} catch (NumberFormatException e) {
            throw new IllegalArgumentException("receiver must be a valid user id : "+message.getReceiver());
        }
        Message savedMessage=  messageRepository.save(message);
        User receiver = userRepository.findById(receiverId).orElseThrow(()->new RuntimeException("receiver not found "+receiverId));
        if (receiver.getFcmToken()!=null && ! receiver.getFcmToken().isBlank()){
            fcmService.sendNotification(receiver.getFcmToken(),"new message ","you have received a new message ");
        }
        return savedMessage;
    }
}
