package com.example.clinic_manager.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ConversationRepo extends JpaRepository<Conversation, Long> {


    @Query("select c from Conversation c where ?1 in (c.user1Id, c.user2Id) and ?2 in (c.user1Id, c.user2Id)")
    Optional<Conversation> getConversation(Long user1Id, Long user2Id);

    @Query("select count(c) from Conversation c where (c.user1Id=?1 and c.unread1 > 0) or (c.user2Id=?1 and c.unread2 > 0)")
    int getUnreadConvNum(Long userId);

    @Query("select c from Conversation c where ?1 in (c.user1Id, c.user2Id) order by c.latestMessageTime desc")
    List<Conversation> getConversations(Long userId);
}