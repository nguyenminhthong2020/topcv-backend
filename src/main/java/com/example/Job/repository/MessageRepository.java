package com.example.Job.repository;

import com.example.Job.entity.Account;
import com.example.Job.entity.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

//    @Query("""
//    SELECT m FROM Message m WHERE (m.senderId = :userID1 AND m.receiverId = :userID2 ) OR
//    (m.senderId = :userID2 AND m.receiverId = :userID1 )
//    ORDER BY m.time ASC
//    """  )
//    List<Message> fetchChatHistory(@Param("userID1") long userID1, @Param("userID2") long userID2);

//    @Query("""
//   SELECT a FROM Account a WHERE a.id IN (
//        SELECT m.senderId FROM Message m WHERE m.receiverId = :userId
//        UNION
//        SELECT m.receiverId FROM Message m WHERE m.senderId = :userId
//   )
//   """)
//    List<Account> findAllUserWithConversation(@Param("userId") long userId);

//    @Query("""
//    SELECT a, MAX(m.time) FROM Account a
//    JOIN Message m ON (m.senderId = a.id OR m.receiverId = a.id)
//    WHERE (m.senderId = :userId OR m.receiverId = :userId) AND a.id <> :userId
//    GROUP BY a.id
//    ORDER BY MAX(m.time) DESC
//""")
//@Query("""
//    SELECT a FROM Account a WHERE a.id IN(
//    SELECT a.id
//    FROM Account a
//    JOIN Message m ON (m.senderId = a.id OR m.receiverId = a.id)
//    WHERE (m.senderId = :userId OR m.receiverId = :userId) AND a.id <> :userId
//    GROUP BY a.id
//    ORDER BY MAX(m.time) DESC)
//""")
//    List<Account> findUsersByLatestInteraction(@Param("userId") long userId);


        @Query("""
    SELECT m FROM Message m WHERE m.conversation.id = :conversationId 
    ORDER BY m.time ASC
    """  )
    List<Message> fetchChatHistory(@Param("conversationId") long conversationId);

}
