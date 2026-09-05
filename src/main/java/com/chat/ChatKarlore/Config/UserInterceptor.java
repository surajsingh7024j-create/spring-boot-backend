package com.chat.ChatKarlore.Config;

import com.chat.ChatKarlore.Security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    @Override
    public Message<?> preSend(Message<?>message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("======== STOMP ");
        System.out.println("COMMAND ="+accessor.getCommand());
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            System.out.println("AUTH HEADER ="+authHeader);
            if (authHeader == null || authHeader.startsWith("bearer")){
                System.out.println("AUTH HEADER MISSING ");
                throw new IllegalArgumentException("missing Authorization header");
            }
            String token = authHeader.trim().substring(7);
            System.out.println("TOKEN RECEIVED ="+token.substring(0,15)+"...");
           String username = jwtService.extractUsername(token);
           System.out.println("USERNAME ="+username);
                   if (username == null ||! jwtService.isTokenValid(token,username)){
                       System.out.println("JWT INVALID");
                       throw new IllegalArgumentException("invalid jwt token");
                   }
                   System.out.println("JWT VALID");
                   accessor.setUser(new StompPrincipal(username));
                if( accessor.getSessionAttributes()!=null){accessor.getSessionAttributes().put("username",username);}

        System.out.println("STOMP USER SET ="+ username);}
        return message;
    }
}
