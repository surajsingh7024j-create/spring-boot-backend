package com.chat.ChatKarlore.Controller;

import com.chat.ChatKarlore.Dto.UserDto;
import com.chat.ChatKarlore.Entity.User;
import com.chat.ChatKarlore.Repository.UserRepository;
import com.chat.ChatKarlore.Security.AuthService;
import com.chat.ChatKarlore.Service.UserPresenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserPresenceService userPresenceService;
    private final UserRepository userRepository;

    public UserController(UserPresenceService userPresenceService, UserRepository userRepository, AuthService authService) {
        this.userPresenceService = userPresenceService;
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> profile(Authentication authentication) {
        System.out.println("PROFILE API HIT");
        String email = authentication.getName();
        Map<String, String> response = new HashMap<>();
        response.put("email", email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        System.out.println("ALL USERS  API HIT");
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail())).toList();
    }

    @GetMapping("/{username}/online")
    public boolean isOnline(@PathVariable String username) {
        return userPresenceService.isOnline(username);
    }


    @PostMapping("/update-fcm")
    public ResponseEntity<?> updateFcmToken(@RequestBody Map<String,String>body,Authentication authentication) {
        String token = body.get("token");
        if (token == null || token.isBlank()) {return ResponseEntity.badRequest().body("FCM token is required");
        }
        String email =authentication.getName();
        User user= userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found "));
        user.setFcmToken(token);
        userRepository.save(user);
        return ResponseEntity.ok("FCM token saved");
    }
}