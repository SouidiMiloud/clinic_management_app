package com.example.clinic_manager.message;

import com.example.clinic_manager.user.ClinicUser;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;


    @PostMapping("/sendMessage")
    public ResponseEntity<String> sendMessage(@AuthenticationPrincipal ClinicUser user, @RequestBody Map<String, String> message){

        return messageService.saveMessage(user, message);
    }

    @GetMapping("/getConversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(@AuthenticationPrincipal ClinicUser user){

        return messageService.getConversations(user);
    }

    @GetMapping("/getMessages")
    public ResponseEntity<Map<String, Object>> getMessages(@AuthenticationPrincipal ClinicUser user, @RequestParam String participantUsername){

        return messageService.getMessages(user.getId(), participantUsername);
    }
}