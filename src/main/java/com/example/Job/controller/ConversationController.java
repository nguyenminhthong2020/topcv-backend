package com.example.Job.controller;

import com.example.Job.models.dtos.*;
import com.example.Job.service.IConversationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation")

public class ConversationController {

    private static final Logger log = LoggerFactory.getLogger(ConversationController.class);
    private IConversationService conversationService;
    private SimpMessagingTemplate simpMessagingTemplate;

    public ConversationController(IConversationService conversationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.conversationService = conversationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    //  /app/chat
    @MessageMapping("/chat")
    public ResponseEntity<ResponseDto> sendToUser(@Payload MessageRequest message, Principal principal){
        // Send to the user's personal queue: /user/{recipient}/queue/messages


        String senderId = principal.getName();

        message.setSenderId(senderId);

        log.info("Message: " + message);

        conversationService.sendAndAddNewMessage(message);

        ResponseDto responseDto = new ResponseDto.Builder()
                .setStatus(HttpStatus.OK)
                .setMessage("Save message successfully")
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<ResponseDto> getAllChatUsers(@RequestParam(name = "userId") long userId){

//        List<AccountDto> allUserWithConversation = messageService.findAllUserWithConversation(userId);

        List<GetConversationResponse> allUserWithConversation = conversationService.findAllUserWithConversation(userId);
        ResponseDto responseDto = new ResponseDto.Builder()
                .setStatus(HttpStatus.OK)
                .setMessage("OK")
                .setData(allUserWithConversation)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<ResponseDto> createNewConversation(@Valid @RequestBody AddConversationRequest conversationRequest){

//        List<AccountDto> allUserWithConversation = messageService.findAllUserWithConversation(userId);

        AddConversationResponse newConversation = conversationService.createNewConversation(conversationRequest);
        ResponseDto responseDto = new ResponseDto.Builder()
                .setStatus(HttpStatus.CREATED)
                .setMessage("OK")
                .setData(newConversation)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }
}
