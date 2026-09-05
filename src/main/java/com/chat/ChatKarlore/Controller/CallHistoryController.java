package com.chat.ChatKarlore.Controller;

import com.chat.ChatKarlore.Entity.Call;
import com.chat.ChatKarlore.Repository.CallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calls")
@RequiredArgsConstructor
public class CallHistoryController {
    private final CallRepository callRepository;
    @GetMapping("/history/{userId}")
    public List<Call> getCallHistory(@PathVariable String userId){
        return callRepository.findByCallerOrReceiverOrderByStartedAtDesc(userId,userId);
    }
}
