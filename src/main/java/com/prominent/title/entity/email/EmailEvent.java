package com.prominent.title.entity.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_events")
public class EmailEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emailEventId;
    @Column(nullable = false)
    private int displayOrder;
    @Column(nullable = false, length = 50)
    private String eventCode;
    @Column(nullable = false, length = 500)
    private String emailSubject;
    @Column(nullable = false)
    private String emailTemplate;
    private String defaultEmailTo;
    private String defaultEmailCc;
    private String defaultEmailBcc;
    @Column(nullable = false)
    private int createUserId;
    private int updateUserId;
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime createDate;
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP(6)")
    private LocalDateTime updateDate;
    @Column(nullable = false, length = 100)
    private String internalKey;
    @Column(nullable = false, length = 10)
    private String concurrencyKey;

    @OneToOne
    @JoinColumn(referencedColumnName = "smtpAccountCode", name = "smtpAccountCode")
    private SmtpAccount smtpAccount;

    @OneToMany
    @JoinColumn(referencedColumnName = "emailEventId", name = "emailEventId")
    private List<EmailQueue> emailQueues;
}
