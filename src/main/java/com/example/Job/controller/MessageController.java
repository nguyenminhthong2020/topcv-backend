package com.example.Job.controller;

import com.example.Job.entity.Message;
import com.example.Job.models.dtos.ResponseDto;
import com.example.Job.security.JwtUtil;

import com.example.Job.service.IMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private IMessageService messageService;
    private SimpMessagingTemplate simpMessagingTemplate;
    private SimpUserRegistry simpUserRegistry;
    private JwtUtil jwtUtil;
    private final String destinationQueue = "/queue/messages";

    public MessageController(IMessageService messageService, SimpMessagingTemplate simpMessagingTemplate, SimpUserRegistry simpUserRegistry, JwtUtil jwtUtil) {
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.simpUserRegistry = simpUserRegistry;
        this.jwtUtil = jwtUtil;
    }



//    @GetMapping("/api/v1/chat")
//    public ResponseEntity<ResponseDto> getAllChatUsers(@RequestParam(name = "userId") long userId){
//
////        List<AccountDto> allUserWithConversation = messageService.findAllUserWithConversation(userId);
//        ResponseDto responseDto = new ResponseDto.Builder()
//                .setStatus(HttpStatus.OK)
//                .setMessage("OK")
//                .setData(null)
//                .build();
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//
//    }


    @GetMapping("/api/v1/chat/history")
    public ResponseEntity<ResponseDto> getChatHistory(@RequestParam(name = "conversationId") long conversationId){

//        List<Message> messages = messageService.getChatHistory(userID1, userID2);
        List<Message> messages = messageService.findMessagesByConversationId(conversationId);
        ResponseDto responseDto = new ResponseDto.Builder()
                .setStatus(HttpStatus.OK)
                .setMessage("OK")
                .setData(messages)
                .build();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }



}
