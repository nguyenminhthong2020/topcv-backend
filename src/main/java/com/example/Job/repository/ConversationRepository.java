package com.example.Job.repository;

import com.example.Job.entity.Account;
import com.example.Job.entity.Conversation;
import com.example.Job.models.dtos.GetConversationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
//    @Query("""
//    SELECT a FROM Account a
//    JOIN Conversation c ON (c.user1_ID = a.id OR c.user2_id = a.id)
//    WHERE a.id <> :userId AND ( c.user1_ID = :userId OR c.user2_ID = :userId)
//    ORDER BY c.lastUpdated DESC
//""")
//    List<Account> findUsersByLatestInteraction(@Param("userId") long userId);

//    @Query("""
//    SELECT c,
//        CASE
//            WHEN c.user1.id  = :userId THEN c.user2
//            ELSE c.user1
//        END
//    FROM Conversation c
//    WHERE c.user1.id = :userId OR c.user2.id = :userId
//    ORDER BY c.lastUpdated DESC
//""")
//    List<Conversation> findUsersByLatestInteraction(@Param("userId") long userId);

    @Query("""
    SELECT c
    FROM Conversation c
    WHERE c.user1.id = :userId OR c.user2.id = :userId
    ORDER BY c.lastUpdated DESC 
""")
    List<Conversation> findUsersByLatestInteraction(@Param("userId") long userId);

    @Query("""
   SELECT new com.example.Job.models.dtos.GetConversationResponse(
    c.id, c.lastMessage, c.lastUpdated, new com.example.Job.models.dtos.AccountDto(
    CASE
        WHEN c.user1.id = :userId THEN c.user2.id
        ELSE c.user1.id
    END,
    CASE
        WHEN c.user1.id = :userId THEN c.user2.name
        ELSE c.user1.name
    END,
    CASE
        WHEN c.user1.id = :userId THEN c.user2.role
        ELSE c.user1.role
    END
    )
   )
    FROM Conversation c
    WHERE c.user1.id = :userId OR c.user2.id = :userId
    ORDER BY c.lastUpdated DESC 
    """
    )
    List<GetConversationResponse> findConversationsWithUserId(@Param("userId") long userId);


    @Modifying
    @Query("""
            UPDATE Conversation c 
            SET c.lastMessage = :lastMessage, c.lastUpdated = :lastUpdated 
            WHERE c.id = :conversationId"""
    )
    void updateLastMessage(@Param("conversationId") Long conversationId,
                           @Param("lastMessage") String lastMessage,
                           @Param("lastUpdated") Instant lastUpdated);
}
