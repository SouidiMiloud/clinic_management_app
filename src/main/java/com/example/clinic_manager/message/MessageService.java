package com.example.clinic_manager.message;

import com.example.clinic_manager.user.ClinicUser;
import com.example.clinic_manager.user.ClinicUserRepo;
import com.example.clinic_manager.user.UserMessageResponse;
import com.example.clinic_manager.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepo messageRepo;
    private ConversationRepo conversationRepo;
    private ClinicUserRepo clinicUserRepo;
    private SimpMessagingTemplate messagingTemplate;
    private UserService userService;


    public ResponseEntity<String> saveMessage(ClinicUser user, Map<String, String> message_req) {

        String content = message_req.get("message");
        if(content.isEmpty())
            return ResponseEntity.ok().body("empty message");
        Conversation conversation;
        LocalDateTime now = LocalDateTime.now();
        ClinicUser participant = clinicUserRepo.findByUsername(message_req.get("username")).get();
        Optional<Conversation> conversationOpt = conversationRepo.getConversation(user.getId(), participant.getId());
        if(conversationOpt.isPresent())
            conversation = conversationOpt.get();
        else
            conversation = new Conversation(null, null, user.getId(), participant.getId(), 0, 0);
        conversation.setLatestMessage(content);
        conversation.setLatestMessageTime(now);
        if(conversation.getUser1Id().equals(participant.getId()))
            conversation.setUnread1(conversation.getUnread1() + 1);
        else conversation.setUnread2(conversation.getUnread2() + 1);
        conversationRepo.save(conversation);
        Message message = new Message(user.getId(), participant.getId(), conversation.getId(), now, content);
        messageRepo.save(message);

        sendConversationNotif(participant);
        return ResponseEntity.ok().body("message saved successfully");
    }
    private void sendConversationNotif(ClinicUser participant){

        int unreadConvNum = conversationRepo.getUnreadConvNum(participant.getId());
        participant.setMessagesNum(unreadConvNum);
        participant.setNotificationsNum(participant.getNotificationsNum() + 1);
        clinicUserRepo.save(participant);
        userService.sendNotifs(participant.getId());
    }

    public ResponseEntity<Map<String, Object>> getMessages(Long userId, String participantUsername) {

        ClinicUser participant = clinicUserRepo.findByUsername(participantUsername).get();
        Optional<Conversation> conversation = conversationRepo.getConversation(userId, participant.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("participant", new UserMessageResponse(participant.getFirstName() + ' ' + participant.getLastName(), participant.getProfileImagePath()));
        if(conversation.isEmpty()) {
            map.put("messages", List.of());
            return ResponseEntity.ok().body(map);
        }
        List<Message> messages = messageRepo.getMessages(conversation.get().getId());

        map.put("messages", buildMessageResponses(messages));
        updateUserUnreadMessages(userId, conversation.get());
        return ResponseEntity.ok().body(map);
    }
    private List<MessageResponse> buildMessageResponses(List<Message> messages){
        List<MessageResponse> response = new ArrayList<>();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        for(Message m : messages)
            response.add(new MessageResponse(m.getSenderId(), m.getTime().format(dateFormat),
                    m.getTime().format(timeFormat), m.getContent()));

        return response;
    }
    private void updateUserUnreadMessages(Long userId, Conversation conversation){

        ClinicUser user = clinicUserRepo.findById(userId).get();
        if(userId.equals(conversation.getUser1Id())) {
            user.setNotificationsNum(user.getNotificationsNum() - conversation.getUnread1());
            conversation.setUnread1(0);
        }
        else {
            user.setNotificationsNum(user.getNotificationsNum() - conversation.getUnread2());
            conversation.setUnread2(0);
        }
        conversationRepo.save(conversation);
        int unreadConvNum = conversationRepo.getUnreadConvNum(userId);
        user.setMessagesNum(unreadConvNum);
        clinicUserRepo.save(user);
        userService.sendNotifs(userId);
    }

    public ResponseEntity<List<ConversationResponse>> getConversations(ClinicUser user) {

        List<Conversation> conversations = conversationRepo.getConversations(user.getId());
        List<ConversationResponse> response = new ArrayList<>();
        ClinicUser participant;
        Long participantId;
        String name;
        Integer unreadMessages;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for(Conversation c : conversations){
            participantId = (c.getUser1Id().equals(user.getId()) ? c.getUser2Id() : c.getUser1Id());
            unreadMessages = (c.getUser1Id().equals(user.getId()) ? c.getUnread1() : c.getUnread2());
            participant = clinicUserRepo.findById(participantId).get();
            name = participant.getFirstName() + ' ' + participant.getLastName();
            response.add(new ConversationResponse(participant.getUsername(), name, participant.getProfileImagePath(),
                    unreadMessages, c.getLatestMessageTime().format(formatter), c.getLatestMessage()));
        }
        return ResponseEntity.ok().body(response);
    }
}