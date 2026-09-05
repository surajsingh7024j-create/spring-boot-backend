package com.chat.ChatKarlore.FIrebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;


@Service
public class FcmService {
    public String sendNotification(
            String deviceToken,
            String title,
            String body
    ) throws Exception{
        Notification notification= Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .build();
        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .build();
        return
                FirebaseMessaging.getInstance().send(message);
    }
}
