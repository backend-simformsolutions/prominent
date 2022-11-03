package com.prominent.title.repository;

import com.prominent.title.entity.EmailStatus;
import com.prominent.title.entity.email.EmailQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface EmailQueueRepository extends JpaRepository<EmailQueue, Integer> {
    @Modifying
    @Transactional
    @Query("update EmailQueue e set e.emailStatus = ?1, e.emailSentDatetime = ?2, e.sentAttemptCount = ?3 " +
            "where e.emailQueueId = ?4")
    void updateWhenSuccessByEmailQueueId(EmailStatus emailStatus, LocalDateTime emailSentDatetime, int sentAttemptCount, int emailQueueId);

    @Modifying
    @Transactional
    @Query("update EmailQueue e set e.emailStatus = ?1, e.errorText = ?2 where e.emailQueueId = ?3")
    void updateWhenFailureByEmailQueueId(EmailStatus emailStatus, String errorText, int emailQueueId);

}
