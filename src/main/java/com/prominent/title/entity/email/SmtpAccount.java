package com.prominent.title.entity.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "smtp_accounts")
public class SmtpAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int smtpAccountId;
    @Column(nullable = false, length = 50)
    private String smtpAccountCode;
    @Column(nullable = false)
    private String smtpHost;
    @Column(nullable = false)
    private int smtpPort;
    private Boolean isSslEnable;
    @Column(nullable = false, length = 500)
    private String smtpFrom;
    private String replyTo;
    private String smtpUserName;
    private String smtpPassword;
    private boolean isActive;
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
}
