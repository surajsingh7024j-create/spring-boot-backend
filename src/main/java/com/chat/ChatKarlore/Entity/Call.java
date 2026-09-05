package com.chat.ChatKarlore.Entity;

import com.chat.ChatKarlore.Dto.Type.CallStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="calls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Call {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "caller_id")
    private String caller;
    private String receiver;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CallStatus status;
    private boolean videoCall;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
