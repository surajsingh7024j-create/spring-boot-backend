package com.chat.ChatKarlore.Security;

import com.chat.ChatKarlore.Dto.AuthResponse;
import com.chat.ChatKarlore.Dto.LoginRequest;
import com.chat.ChatKarlore.Dto.SignUpRequest;
import com.chat.ChatKarlore.Entity.User;
import com.chat.ChatKarlore.Repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;         // eska work database se user find aur save
    private final PasswordEncoder passwordEncoder;          // eska password bcrypt hash login time password verify karna
    private final JwtService jwtService;                // jwt token generate and verify

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {         // constructor injection ya 3 teeno ka constructor bana ke auth service ko dtea ha
        this.userRepository = userRepository;  // eska class ke var me spring se mila userrepository ko save karn a
        this.passwordEncoder = passwordEncoder;         //
        this.jwtService = jwtService;
    }
    // signUp keliye
    public String Signup(SignUpRequest signUpRequest){                          // signup request milegi
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {               // kya email pahle se user table me hai ya nahi
            throw new UsernameNotFoundException("Email Already Exists");
        }
        User user= new User();
        user.setName(signUpRequest.getName());// esse ek empaty user object banta ha // user object ke username me save
        user.setEmail(signUpRequest.getEmail());        // esme data set karte ha
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));// password ko encrypt karta ha
        user.setConfirmedPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);                  //my sql me save hota haa
        return "user registered successfully";
    }
    // login
    public AuthResponse Login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new RuntimeException("user Not Found"));
        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        if (!passwordMatches) {         // pasword galat ho
            throw new RuntimeException("Password Do Not Match");        // tab ye bhejega
        }
        String token = jwtService.generateToken(user.getEmail(),  user.getPassword());      // user ka email jwt subject me jayega
        return new AuthResponse(token);
    }
}
