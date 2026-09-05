package com.chat.ChatKarlore.Service;

import com.chat.ChatKarlore.Dto.Type.CallStatus;
import com.chat.ChatKarlore.Entity.Call;
import com.chat.ChatKarlore.Repository.CallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MissedCallScheduler {
    private final CallRepository callRepository;
    @Scheduled(fixedRate = 100000)
    public void checkMissedCalls(){
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(60);
        List<Call> ringingCalls = callRepository.findByStatusAndStartedAtBefore(CallStatus.MISSED, cutoff);
        for(Call call:ringingCalls){
            call.setStatus(CallStatus.MISSED);
            call.setEndedAt(LocalDateTime.now());
            callRepository.save(call);
        }
    }
}
