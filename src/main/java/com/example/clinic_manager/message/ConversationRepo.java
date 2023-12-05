package com.example.clinic_manager.message;

import com.example.clinic_manager.user.ClinicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ConversationRepo extends JpaRepository<Conversation, Long> {


    @Query("SELECT c FROM Conversation c" +
            " WHERE ?1 IN (c.participant1, c.participant2)" +
            "AND ?2 IN (c.participant1, c.participant2)")
    Optional<Conversation> getConversation(ClinicUser user1, ClinicUser user2);

    @Query("SELECT COUNT(c) FROM Conversation c" +
            " WHERE (c.participant1=?1 AND c.unread1 > 0)" +
            "OR (c.participant2=?1 AND c.unread2 > 0)")
    int getUnreadConvNum(ClinicUser user);

    @Query("SELECT c FROM Conversation c " +
            "WHERE (c.participant1 = ?1 AND (c.participant2.firstName LIKE CONCAT('%', ?2, '%')" +
                "OR c.participant2.lastName LIKE CONCAT('%', ?2, '%')))" +
            " OR (c.participant2 = ?1 AND (c.participant1.firstName LIKE CONCAT('%', ?2, '%')" +
                "OR c.participant1.lastName LIKE CONCAT('%', ?2, '%')))" +
            " ORDER BY c.latestMessageTime DESC")
    List<Conversation> getConversations(ClinicUser user, String search);

}