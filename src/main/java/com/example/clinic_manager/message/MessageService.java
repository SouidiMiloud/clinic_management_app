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
    private UserService userService;


    public ResponseEntity<String> saveMessage(ClinicUser user, MessageRequest request) {

        if(request.getMessage().isEmpty())
            return ResponseEntity.ok().body("empty message");
        ClinicUser participant = clinicUserRepo.findByUsername(request.getUsername()).get();
        Optional<Conversation> conversationOpt = conversationRepo.getConversation(user, participant);
        Conversation conversation = conversationOpt.orElseGet(() ->
                new Conversation(null, null, user, participant, 0, 0));

        updateConversation(conversation, participant, request.getMessage());
        Message message = new Message(user.getId(), participant.getId(), conversation, LocalDateTime.now(), request.getMessage());
        messageRepo.save(message);
        sendConversationNotif(participant);
        return ResponseEntity.ok().body("message saved successfully");
    }
    private void updateConversation(Conversation conversation, ClinicUser receiver, String msgContent){
        conversation.setLatestMessage(msgContent);
        conversation.setLatestMessageTime(LocalDateTime.now());
        if(conversation.getParticipant1().equals(receiver))
            conversation.setUnread1(conversation.getUnread1() + 1);
        else
            conversation.setUnread2(conversation.getUnread2() + 1);
        conversationRepo.save(conversation);
    }
    private void sendConversationNotif(ClinicUser participant){

        int unreadConvNum = conversationRepo.getUnreadConvNum(participant);
        userService.updateNotifications(participant, -1, unreadConvNum);
    }

    public ResponseEntity<MessagesResp> getMessages(Long userId, String participantUsername) {

        ClinicUser participant = clinicUserRepo.findByUsername(participantUsername).get();
        Optional<Conversation> conversation = conversationRepo.getConversation(clinicUserRepo.findById(userId).get(), participant);
        MessagesResp response = new MessagesResp(userId, new UserMessageResponse(participant.getFirstName() +
                ' ' + participant.getLastName(), participant.getProfileImagePath()));
        if(conversation.isEmpty())
            return ResponseEntity.ok().body(response);

        List<Message> messages = messageRepo.getMessages(conversation.get());

        response.setMessages(buildMessageResponses(messages));
        updateUserUnreadMessages(userId, conversation.get());
        return ResponseEntity.ok().body(response);
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
        if(userId.equals(conversation.getParticipant1().getId()))
            conversation.setUnread1(0);
        else
            conversation.setUnread2(0);
        conversationRepo.save(conversation);
        int unreadConvNum = conversationRepo.getUnreadConvNum(user);
        userService.updateNotifications(user, -1, unreadConvNum);
    }

    public ResponseEntity<List<ConversationResponse>> getConversations(ClinicUser user, String search) {

        List<Conversation> conversations = conversationRepo.getConversations(user, search);
        List<ConversationResponse> response = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        for(Conversation c : conversations){
            Long participantId = (c.getParticipant1().equals(user) ? c.getParticipant2().getId() : c.getParticipant1().getId());
            Integer unreadMessages = (c.getParticipant1().equals(user) ? c.getUnread1() : c.getUnread2());
            ClinicUser participant = clinicUserRepo.findById(participantId).get();
            String name = participant.getFirstName() + ' ' + participant.getLastName();
            response.add(new ConversationResponse(participant.getUsername(), name, participant.getProfileImagePath(),
                    unreadMessages, c.getLatestMessageTime().format(formatter), c.getLatestMessage()));
        }
        return ResponseEntity.ok().body(response);
    }
}