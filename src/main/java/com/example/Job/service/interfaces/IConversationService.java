package com.example.Job.service.interfaces;

import com.example.Job.entity.Conversation;
import com.example.Job.entity.Message;
import com.example.Job.models.dtos.AddConversationRequest;
import com.example.Job.models.dtos.AddConversationResponse;
import com.example.Job.models.dtos.GetConversationResponse;
import com.example.Job.models.dtos.MessageRequest;

import java.util.List;

public interface IConversationService {


    AddConversationResponse createNewConversation(AddConversationRequest conversationRequest);

    List<GetConversationResponse> findAllUserWithConversation(long userId);
//
//    List<Message> getChatHistory(long userID1, long userID2);
    List<Message> getMessagesByConversationId(long conversationId);

    void sendAndAddNewMessage(MessageRequest messageRequest);
}
