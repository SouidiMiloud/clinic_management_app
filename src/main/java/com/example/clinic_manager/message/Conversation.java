package com.example.clinic_manager.message;

import com.example.clinic_manager.user.ClinicUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Entity
public class Conversation {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private LocalDateTime latestMessageTime;
    private String latestMessage;
    @ManyToOne
    private ClinicUser participant1;
    @ManyToOne
    private ClinicUser participant2;
    private Integer unread1;
    private Integer unread2;

    public Conversation(LocalDateTime latestMessageTime, String latestMessage, ClinicUser participant1,
                        ClinicUser participant2, Integer unread1, Integer unread2) {
        this.latestMessageTime = latestMessageTime;
        this.latestMessage = latestMessage;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.unread1 = unread1;
        this.unread2 = unread2;
    }
}
