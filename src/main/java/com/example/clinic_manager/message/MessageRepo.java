package com.example.clinic_manager.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MessageRepo extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.conversationId=?1 order by m.time asc ")
    List<Message> getMessages(Long conversationId);
}