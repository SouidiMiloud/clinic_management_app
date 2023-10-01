package com.example.clinic_manager.message;

import lombok.AllArgsConstructor;
import lombok.Data;



@Data
@AllArgsConstructor
public class ConversationResponse {

    private String participantUsername;
    private String participantName;
    private String participantImage;
    private Integer unreadMsgsNum;
    private String lastMsgTime;
    private String lastMsgContent;
}