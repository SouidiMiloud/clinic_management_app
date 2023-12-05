package com.example.clinic_manager.message;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;
    private LocalDateTime time;
    private String content;

    public Message(Long senderId, Long receiverId, Conversation conversation, LocalDateTime time, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.conversation = conversation;
        this.time = time;
        this.content = content;
    }
}