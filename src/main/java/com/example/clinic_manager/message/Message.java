package com.example.clinic_manager.message;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long conversationId;
    private LocalDateTime time;
    private String content;


    public Message(Long senderId, Long receiverId, Long conversationId, LocalDateTime time, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.conversationId = conversationId;
        this.time = time;
        this.content = content;
    }
}