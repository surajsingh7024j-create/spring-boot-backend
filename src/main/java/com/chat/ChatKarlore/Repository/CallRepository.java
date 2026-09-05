package com.chat.ChatKarlore.Repository;

import com.chat.ChatKarlore.Dto.Type.CallStatus;
import com.chat.ChatKarlore.Entity.Call;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {
    List<Call> findByStatusAndStartedAtBefore(CallStatus callStatus, LocalDateTime time);
    List<Call> findByCallerOrReceiverOrderByStartedAtDesc(String Caller,String receiver);
}
