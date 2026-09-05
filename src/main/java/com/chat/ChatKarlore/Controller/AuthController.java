package com.chat.ChatKarlore.Controller;

import com.chat.ChatKarlore.Dto.AuthResponse;
import com.chat.ChatKarlore.Dto.LoginRequest;
import com.chat.ChatKarlore.Dto.SignUpRequest;
import com.chat.ChatKarlore.Security.AuthService;
import com.chat.ChatKarlore.Security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController         // ye wali class api response degi jo ki json me hoga
@RequestMapping("/api/auth")        // class ke har api ke start me ye path lagega
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    // signup api
    @PostMapping("/signup")         // post like http://localhost:8080/api/auth/signup
    public ResponseEntity<AuthResponse> signup(@RequestBody SignUpRequest signUpRequest){
         authService.Signup(signUpRequest);
        return ResponseEntity.ok(new AuthResponse("Signup success"));
    }
    // login api
    @PostMapping("/login")              // post like http://localhost:8080/api/auth/login
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        System.out.println("LOGIN API HIT");//request body lka matlab jo bhi joson ayega wo object me convert hogaa
        AuthResponse authResponse = authService.Login(loginRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
