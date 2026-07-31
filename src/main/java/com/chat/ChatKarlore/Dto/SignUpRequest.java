package com.chat.ChatKarlore.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    private String name;
    private String number;
    private String username;
    private String email;
    private String password;
    private String confirmedPassword;
}
