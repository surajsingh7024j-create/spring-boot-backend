package com.chat.ChatKarlore.Dto;

import lombok.Data;

@Data
public class CallSignal {
    private Long callId;
    private String sender;
    private String receiver;
    private String type;
    private String sdp;
    private String candidate;
    private String sdpMid;
    private Integer sdpMLineIndex;
    private Boolean videoCall;

}
