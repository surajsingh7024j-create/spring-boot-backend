package com.chat.ChatKarlore.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
@Component
@RequiredArgsConstructor
public class PresenceListener {
    private final UserPresenceService userPresenceService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void connect(SessionConnectedEvent event){
        StompHeaderAccessor accessor=StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();
        String sessionId = accessor.getSessionId();
        if (principal == null||sessionId==null){return; }
        String username= principal.getName();
            userPresenceService.online(username,sessionId);
            Boolean online=userPresenceService.isOnline(username);
            Map<String,Object> presence =new HashMap<>();
            presence.put("username",username);
            presence.put("online",online);
        Message<Map<String,Object>>message= MessageBuilder.withPayload(presence).build();
            messagingTemplate.send("/topic/presence", message);
            System.out.println("PRESENCE SENT :"+username+"online "+online);
        }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();
        String sessionId = accessor.getSessionId();
        if (principal == null || sessionId == null) {
            return;
        }
        String username= principal.getName();
        userPresenceService.offline(username,sessionId);
        Boolean online=userPresenceService.isOnline(username);
        Map<String, Object> presence =new HashMap<>();
        presence.put("username",username);
        presence.put("online",online);
        Message<Map<String,Object>>message=MessageBuilder.withPayload(presence).build();
        messagingTemplate.send( "/topic/presence", message);
        System.out.println("PRESENCE SENT: "+username+"Offline");

        }
    }

