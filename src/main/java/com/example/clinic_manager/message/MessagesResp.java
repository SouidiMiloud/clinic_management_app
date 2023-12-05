package com.example.clinic_manager.message;

import com.example.clinic_manager.user.UserMessageResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class MessagesResp {
    private Long userId;
    private UserMessageResponse participant;
    private List<MessageResponse> messages;

    public MessagesResp(Long userId, UserMessageResponse participant){
        this.userId = userId;
        this.participant = participant;
        messages = new ArrayList<>();
    }
}