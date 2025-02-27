package com.example.Job.repository.Test;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.Job.models.dtos.Test.TestChat;

public interface ITestChatRepository extends MongoRepository<TestChat, String> {

}
