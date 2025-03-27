package com.example.Job.service.interfaces;

import com.example.Job.entity.Message;
import com.example.Job.models.dtos.MessageRequest;

import java.util.List;

public interface IMessageService {
    void saveMessage(MessageRequest messageRequest);

    void sendMessage(MessageRequest message);
    List<Message> findMessagesByConversationId(long conversationId);
}
