package com.example.clinic_manager.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.conversation=?1 ORDER BY m.time")
    List<Message> getMessages(Conversation conversation);
}