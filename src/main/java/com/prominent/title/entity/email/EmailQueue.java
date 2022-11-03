package com.prominent.title.entity.email;

import com.prominent.title.entity.EmailStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_queues")
public class EmailQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emailQueueId;
    @Column(length = 50)
    private String eventCode;
    @Column(length = 50)
    private String smtpAccountCode;
    @Column(nullable = false)
    private String emailType;
    private String emailTo;
    private String emailCc;
    private String emailBcc;
    @Column(nullable = false)
    private String subject;
    @Column(nullable = false, length = 1000)
    private String emailBody;
    @Column(nullable = false)
    private boolean isHtmlBody;
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private EmailStatus emailStatus;
    private LocalDateTime emailSentDatetime;
    private String errorText;
    private int sentAttemptCount;
    @Column(nullable = false)
    private int createUserId;
    private int updateUserId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    @Column(nullable = false, length = 100)
    private String internalKey;
    @Column(nullable = false, length = 10)
    private String concurrencyKey;
    private String emailOption;
    private String sendTo;
}
