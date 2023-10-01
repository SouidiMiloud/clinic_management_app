package com.example.clinic_manager.user;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserNotifResponse {

    private String role;
    private String username;
    private String appointmentsNotifNum;
    private String messagesNum;
}