package com.chat.ChatKarlore.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String sender;
    public String receiver;
    @Column(columnDefinition = "TEXT",length = 10000)
    public String content;
    private LocalDateTime sentAt;
    @Enumerated(EnumType.STRING)
    private  MessageStatus status;
}
