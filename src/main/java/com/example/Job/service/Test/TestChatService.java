package com.example.Job.service.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Job.models.dtos.Test.TestChat;
import com.example.Job.repository.Test.ITestChatRepository;

@Service
public class TestChatService {
    private ITestChatRepository _chatRepository;

    @Autowired
    public TestChatService(ITestChatRepository chatRepository) {
        this._chatRepository = chatRepository;
    }

    public TestChat createChat(TestChat testChat) {
        return _chatRepository.save(testChat);
    }
}
