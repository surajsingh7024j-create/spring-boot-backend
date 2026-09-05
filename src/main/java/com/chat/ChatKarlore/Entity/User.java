package com.chat.ChatKarlore.Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    private String number;
    private String username;
    @Column(name = "Password_hash", nullable = false)
    private String password;
    @Column(nullable = false)
    private String ConfirmedPassword;
    @Column(name = "fcm_token", length = 1000)
    private String fcmToken;
}
