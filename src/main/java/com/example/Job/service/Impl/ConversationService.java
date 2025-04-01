package com.example.Job.service.Impl;

import com.example.Job.entity.Account;
import com.example.Job.entity.Conversation;
import com.example.Job.entity.Message;
import com.example.Job.models.dtos.*;
import com.example.Job.repository.ConversationRepository;
import com.example.Job.service.IAccountService;
import com.example.Job.service.IConversationService;

import com.example.Job.service.IMessageService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ConversationService implements IConversationService {

    private static final Logger log = LoggerFactory.getLogger(ConversationService.class);
    private IMessageService messageService;
    private IAccountService accountService;
    private ModelMapper modelMapper;
    private ConversationRepository conversationRepository;

    public ConversationService(IMessageService messageService, IAccountService accountService, ModelMapper modelMapper, ConversationRepository conversationRepository) {
        this.messageService = messageService;
        this.accountService = accountService;
        this.modelMapper = modelMapper;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public AddConversationResponse createNewConversation(AddConversationRequest conversationRequest) {
        Conversation conversation = new Conversation();

        Account user1 = accountService.getAccountById(conversationRequest.getUser1_ID());

        Account user2 = accountService.getAccountById(conversationRequest.getUser2_ID());
//        conversation.setUser1_ID(conversationRequest.getUser1_ID());
//        conversation.setUser2_ID(conversationRequest.getUser2_ID());

        conversation.setUser1(user1);
        conversation.setUser2(user2);
        conversation.setLastUpdated(Instant.now());

        try {
            Conversation saved = conversationRepository.save(conversation);

            AddConversationResponse response = AddConversationResponse.builder()
                    .id(saved.getId())
                    .lastMessage(saved.getLastMessage())
                    .lastUpdated(saved.getLastUpdated())
                    .user1_name(saved.getUser1().getName())
                    .user2_name(saved.getUser2().getName())
                    .build();

//        return modelMapper.map(saved, ConversationResponse.class);
            return  response;
        }catch (DataAccessException exception){
            log.error(exception.getMessage());
            throw new RuntimeException("Internal Server Error");
        }


    }

    @Override
    public List<GetConversationResponse> findAllUserWithConversation(long userId) {

        // this is get response directly in query: two result: 77ms
        List<GetConversationResponse> responses = conversationRepository.findConversationsWithUserId(userId);

        // this is slower when we modify in java, 2 result:200ms
//        List<Conversation> usersByLatestInteraction = conversationRepository.findUsersByLatestInteraction(userId);
//
//        List<GetConversationResponse> responses = usersByLatestInteraction.stream().map(conversation -> {
//                    Account otherUser = conversation.getUser1().getId() == userId ? conversation.getUser1() : conversation.getUser2();
//
//                    GetConversationResponse conversationRes = GetConversationResponse.builder()
//                            .id(conversation.getId())
//                            .lastUpdated(conversation.getLastUpdated())
//                            .lastMessage(conversation.getLastMessage())
//                            .otherUser(modelMapper.map(otherUser, AccountDto.class))
//                            .build();
//                    return conversationRes;
//                }
//        ).collect(Collectors.toList());


        return responses;
    }

    @Override
    public List<Message> getMessagesByConversationId(long conversationId) {
        return messageService.findMessagesByConversationId(conversationId);
    }

    @Override
    @Transactional
    public void sendAndAddNewMessage(MessageRequest messageRequest) {

        messageService.sendMessage(messageRequest);

        conversationRepository.updateLastMessage(messageRequest.getConversationId(), messageRequest.getContent(), Instant.now());
        messageService.saveMessage(messageRequest);

    }


//    @Override
//    public List<AccountDto> findAllUserWithConversation(long userId) {
//
//        List<Account> allUserWithConversation = messageRepository.findUsersByLatestInteraction(userId);
//
//        List<AccountDto> userDtos = allUserWithConversation.stream().map(user ->
//                modelMapper.map(user, AccountDto.class)
//                ).collect(Collectors.toList());
//
//        return userDtos;
//    }
//
//    @Override
//    public List<Message> getChatHistory(long userID1, long userID2) {
//        List<Message> messages = messageRepository.fetchChatHistory(userID1, userID2);
//
//        return messages;
//    }
}
