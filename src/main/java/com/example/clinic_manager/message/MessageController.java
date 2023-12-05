package com.example.clinic_manager.message;

import com.example.clinic_manager.user.ClinicUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;


    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@AuthenticationPrincipal ClinicUser user, @RequestBody MessageRequest request){

        return messageService.saveMessage(user, request);
    }

    @GetMapping("/getConversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(@AuthenticationPrincipal ClinicUser user, @RequestParam String usernameSearch){

        return messageService.getConversations(user, usernameSearch);
    }

    @GetMapping("/getMessages")
    public ResponseEntity<MessagesResp> getMessages(@AuthenticationPrincipal ClinicUser user, @RequestParam String participantUsername){

        return messageService.getMessages(user.getId(), participantUsername);
    }
}