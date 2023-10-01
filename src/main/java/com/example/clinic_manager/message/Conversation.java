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
public class Conversation {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDateTime latestMessageTime;
    private String latestMessage;
    private Long user1Id;
    private Long user2Id;
    private Integer unread1;
    private Integer unread2;

    public Conversation(LocalDateTime latestMessageTime, String latestMessage, Long user1Id, Long user2Id, Integer unread1, Integer unread2) {
        this.latestMessageTime = latestMessageTime;
        this.latestMessage = latestMessage;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.unread1 = unread1;
        this.unread2 = unread2;
    }
}
