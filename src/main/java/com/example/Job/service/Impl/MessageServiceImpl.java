package com.example.Job.service.Impl;

import com.example.Job.constant.MessageStatusEnum;
import com.example.Job.entity.Conversation;
import com.example.Job.entity.Message;
import com.example.Job.models.dtos.MessageRequest;
import com.example.Job.repository.MessageRepository;
import com.example.Job.service.IMessageService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements IMessageService {
    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);
    private MessageRepository messageRepository;
    private ModelMapper modelMapper;
    private SimpMessagingTemplate simpMessagingTemplate;

    public MessageServiceImpl(MessageRepository messageRepository, ModelMapper modelMapper, SimpMessagingTemplate simpMessagingTemplate) {
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public void saveMessage(MessageRequest messageRequest) {

        Conversation conversation = new Conversation();
        conversation.setId(messageRequest.getConversationId());
//        conversation.setLastMessage(messageRequest.getContent());
//        conversation.setLastUpdated(Instant.now());
//
        Message message = new Message();

        message.setContent(messageRequest.getContent());
        message.setSenderId(Long.parseLong(messageRequest.getSenderId()));
//        message.setReceiverId(Long.parseLong(messageRequest.getRecipientId()));
        message.setTime(messageRequest.getTimeStamp());
        message.setConversation(conversation);
        message.setMessageStatus(MessageStatusEnum.UNREAD);

        messageRepository.save(message);
//        try{
//            messageRepository.save(message);
//        } catch (RuntimeException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException("Can not save message");
//        }

    }

    @Override
    public void sendMessage(MessageRequest message) {


        simpMessagingTemplate.convertAndSendToUser(
                message.getRecipientId().toString(),
                "/queue/messages",
                Map.of("content", message.getContent(),
                        "senderId", message.getSenderId(),
                        "senderName", message.getSenderName(),
                        "senderRole", message.getSenderRole(),
                        "timeStamp", message.getTimeStamp() != null ? message.getTimeStamp() : Instant.now(),
                        "conversationId", message.getConversationId()
                )

        );
    }

    @Override
    public List<Message> findMessagesByConversationId(long conversationId) {
        List<Message> messages = messageRepository.fetchChatHistory(conversationId);

        return  messages;
    }

}
