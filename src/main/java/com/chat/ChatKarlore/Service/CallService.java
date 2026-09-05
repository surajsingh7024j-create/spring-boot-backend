package com.chat.ChatKarlore.Service;

import com.chat.ChatKarlore.Dto.Type.CallStatus;
import com.chat.ChatKarlore.Entity.Call;
import com.chat.ChatKarlore.Repository.CallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class CallService {
    private final CallRepository callRepository;
    public Long createCall(String caller, String receiver, boolean videoCall){
       if (caller==null||caller.isBlank()){System.out.println("ERROR:caller is blank");return null;}
        if (receiver==null||receiver.isBlank()){System.out.println("ERROR: receiver is blank");return null;}
            Call call=
        callRepository.save(Call.builder()
                .caller(caller)
                .receiver(receiver)
                .videoCall(videoCall)
                .status(CallStatus.RINGING)
                .startedAt(LocalDateTime.now())
                .build());
        if (call == null ||call.getId()==null){System.out.println("ERROR:call Id was not generated");return null;}
        return call.getId();
    }
    public boolean markAccepted(Long id) {
        if (id==null){System.out.println("ERROR:callId is null in markAccepted");return false;}
        Call call = callRepository.findById(id).orElse(null);
        if (call==null){System.out.println("ERROR: call not found with Id="+id);return false;}
        call.setStatus(CallStatus.ACCEPTED);
        callRepository.save(call);
        return true;
    }

    public void endCall(Long id){
        if (id==null){        System.out.println("ERROR: call is null in end call");return;
        }
        Call call = callRepository.findById(id).orElseThrow(()->new RuntimeException("call not found with id="+id));
        call.setEndedAt(LocalDateTime.now());
        call.setStatus(CallStatus.ENDED);
        callRepository.save(call);
    }
    public void markRejected(Long id){

        if (id==null){        System.out.println("ERROR: call is null in end call");return;
        }
        Call call = callRepository.findById(id).orElseThrow(()->new RuntimeException("call not found with id="+id));
        call.setStatus(CallStatus.REJECTED);
        callRepository.save(call);
    }

}
