package com.example.clinic_manager.message;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class MessageResponse {

    private Long senderId;
    private String date;
    private String time;
    private String content;
}