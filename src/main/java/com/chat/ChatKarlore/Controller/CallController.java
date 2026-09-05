package com.chat.ChatKarlore.Controller;

import com.chat.ChatKarlore.Dto.CallSignal;
import com.chat.ChatKarlore.Entity.Call;
import com.chat.ChatKarlore.Repository.CallRepository;
import com.chat.ChatKarlore.Service.CallService;
import com.chat.ChatKarlore.Service.UserPresenceService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class CallController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final CallService callService;
    private final CallRepository callRepository;
    private final UserPresenceService userPresenceService;

    public CallController(SimpMessagingTemplate simpMessagingTemplate, CallService callService, CallRepository callRepository, UserPresenceService userPresenceService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.callService = callService;
        this.callRepository = callRepository;
        this.userPresenceService = userPresenceService;
    }
    @MessageMapping("/call.offer")
    public void offer(CallSignal callSignal, Principal principal) {
        if (principal==null){System.out.println("ERROR: principal is null in Offer ");return;}
        if (callSignal==null){System.out.println("ERROR: callSignal is null in Offer ");return;}
        String receiver=callSignal.getReceiver();
        if (receiver==null){System.out.println("ERROR: receiver  is null/blank in Offer ");return;}
        String sender= principal.getName();
        callSignal.setSender(sender);
       Long callId = callService.createCall(callSignal.getSender(),receiver,Boolean.TRUE.equals(callSignal.getVideoCall()));
       if (callId==null){System.out.println("ERROR: callId  is null after create call ");return;}
        callSignal.setCallId(callId);
        simpMessagingTemplate.convertAndSendToUser(receiver,
                "/queue/call", callSignal);
    }
    @MessageMapping("/call.answer")
    public void answer(CallSignal callSignal, Principal principal) {
        if (principal==null){System.out.println("ERROR: principal is null in ANSWER ");return;}
        if (callSignal==null){System.out.println("ERROR: callSignal is null in ANSWER ");return;}
        String receiver= callSignal.getReceiver();
        if (receiver==null||receiver.isBlank()){System.out.println("ERROR: receiver is null in ANSWER ");return;}
        Long callId =callSignal.getCallId();
        System.out.println("ANSWER callId="+callId);
        System.out.println("ANSWER sender="+principal.getName());
        System.out.println("ANSWER receiver="+receiver);
        if (callId==null){System.out.println("ERROR: callID is null in ANSWER ");return;}
        callSignal.setSender(principal.getName());
        boolean accepted=
        callService.markAccepted(callId);
        if(!accepted){        System.out.println("ERROR: call was not accepted, id="+callId);return;
        }
        simpMessagingTemplate.convertAndSendToUser(receiver, "/queue/call", callSignal);
    }
    @MessageMapping("/call.ice")
    public void ice(CallSignal callSignal, Principal principal) {
        if (principal==null){System.out.println("ERROR:principal is null in ICE signal");return;}
        if (callSignal==null){System.out.println("ERROR:callSignal is null in ICE signal");return;}
        String receiver=callSignal.getReceiver();
        if (receiver==null||receiver.isBlank()){System.out.println("ERROR:receiver is null in ICE signal");return;}
        callSignal.setSender(principal.getName());
        simpMessagingTemplate.convertAndSendToUser(receiver, "/queue/call", callSignal);
    }
    @MessageMapping("/call.reject")
    public void reject(CallSignal callSignal, Principal principal) {
        callSignal.setSender(principal.getName());
        callService.markRejected(callSignal.getCallId());
        if (callSignal.getReceiver()==null||callSignal.getReceiver().isBlank()){System.out.println("ERROR: receiver is null ");return;}
        simpMessagingTemplate.convertAndSendToUser(callSignal.getReceiver(), "/queue/call", callSignal);
    }
    @MessageMapping("/call.end")
    public void end(CallSignal callSignal,Principal principal) {
        if(principal==null){
                   System.out.println("ERROR: principal is null in end call");return;
            }
        if(callSignal==null){
            System.out.println("ERROR: callSignal is null in end call");return;
        }
        Long callId =callSignal.getCallId();
        if(callId==null){
            System.out.println("ERROR: callId is null in end call");return;
        }
        System.out.println("END callId="+callId);
        System.out.println("END sender="+principal.getName());
        Call call= callRepository.findById(callId).orElse(null);
        if(call==null){System.out.println("ERROR:call not found  with id ="+callId);return;}
        String sender= principal.getName();
        String receiver;
        if (sender.equals(call.getCaller())){receiver=call.getReceiver();}else{receiver=call.getCaller();}
        if(receiver==null||receiver.isBlank()){System.out.println("ERROR: receiver is null in end ");return;}
        callSignal.setSender(sender);
        callSignal.setReceiver(receiver);
        callService.endCall(callId);
        System.out.println("end sending to ="+receiver);
        simpMessagingTemplate.convertAndSendToUser(callSignal.getReceiver(), "/queue/call", callSignal);}
    }

