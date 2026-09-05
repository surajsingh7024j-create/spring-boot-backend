package com.chat.ChatKarlore.FIrebase;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    private final FcmService fcmService;

    public NotificationController(FcmService fcmService) {
        this.fcmService = fcmService;
    }
    @PostMapping("/send")
    public String sendNotification(
            @RequestParam String token,
            @RequestParam String title,
            @RequestParam String body
    )throws Exception{
        return fcmService.sendNotification(token, title, body);
    }
}
