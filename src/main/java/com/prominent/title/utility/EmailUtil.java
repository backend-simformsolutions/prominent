package com.prominent.title.utility;

import com.prominent.title.entity.EmailStatus;
import com.prominent.title.entity.email.EmailQueue;
import com.prominent.title.exception.EmailException;
import com.prominent.title.repository.EmailQueueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.prominent.title.constants.MailConstants.*;

/*
 * This util contains method related to token email generation and url extraction. */

@Slf4j
@Component
public class EmailUtil {

    private final JavaMailSenderImpl mailSender;
    private final RecordCreationUtility recordCreationUtility;
    private final EmailQueueRepository emailQueueRepository;

    @Value("${spring.mail.username}")
    private String smtpUserName;
    @Value("${spring.mail.password}")
    private String smtpPassword;
    @Value("${PASSWORD_TOKEN_EXPIRATION}")
    private long passwordTokenExpiration;

    public EmailUtil(JavaMailSenderImpl mailSender,RecordCreationUtility recordCreationUtility, EmailQueueRepository emailQueueRepository) {
        this.mailSender = mailSender;
        this.recordCreationUtility = recordCreationUtility;
        this.emailQueueRepository = emailQueueRepository;
    }


    /**
     * This method constructs and send the email to recipient email using credentials provided in application properties.
     * Used in Reset Password
     *
     * @param recipientEmail recipientEmail
     * @param link           link
     * @throws MessagingException javax.mail. messaging exception
     * @throws IOException        java.io. i o exception
     */
    public void constructResetTokenEmail(String recipientEmail, String link) throws MessagingException, IOException {
        log.info("Constructing reset password email for {}...", recipientEmail);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(SUPPORT_EMAIL, SUPPORT_NAME);
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Please click on the below link to reset your password:</p>"
                + "<p><a href=\"" + link + "\">Reset Password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        storeEmailQueueDetailsAndSendMailUsingRandomSmtpAccount(mailSender, helper, RESET_USER_PASSWORD, false);
        log.info("Email sent to {}...", recipientEmail);
    }

    /**
     * This method used to store email queue details and send mail using random smtp account.
     *
     * @param mailSender        mailSender
     * @param mimeMessageHelper mimeMessageHelper
     * @param emailType         emailType
     * @param isScheduled       isScheduled
     * @return {@link int}
     * @throws MessagingException javax.mail. messaging exception
     * @throws IOException        java.io. i o exception
     */

    public int storeEmailQueueDetailsAndSendMailUsingRandomSmtpAccount(JavaMailSenderImpl mailSender, MimeMessageHelper mimeMessageHelper, String emailType, boolean isScheduled) throws MessagingException, IOException {
        MimeMessage mimeMessage = mimeMessageHelper.getMimeMessage();
        this.mailSender.setUsername(smtpUserName);
        this.mailSender.setPassword(smtpPassword);
        EmailQueue emailQueue = new EmailQueue();
        emailQueue.setEmailTo(Arrays.toString(mimeMessage.getRecipients(Message.RecipientType.TO)));
        emailQueue.setEmailCc(Arrays.toString(mimeMessage.getRecipients(Message.RecipientType.CC)));
        emailQueue.setEmailBcc(Arrays.toString(mimeMessage.getRecipients(Message.RecipientType.BCC)));
        emailQueue.setEmailType(emailType);
        emailQueue.setSubject(mimeMessage.getSubject());
        emailQueue.setEmailBody(mimeMessage.getContent().toString());
        emailQueue.setHtmlBody(true);
        emailQueue.setEmailStatus(EmailStatus.PENDING);
        BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), emailQueue);
        if (isScheduled) {
            emailQueue.setEmailOption("Schedule");
        } else {
            emailQueue.setEmailOption("Now");
            sendMailAndChangeEmailQueueDetails(mailSender, mimeMessage, emailQueue.getEmailQueueId());
            return emailQueue.getEmailQueueId();
        }
        return 0;
    }

    /**
     * This method used to send mail and change email queue details.
     *
     * @param mailSender  mailSender
     * @param mimeMessage mimeMessage
     * @param id          id
     */
    public void sendMailAndChangeEmailQueueDetails(JavaMailSenderImpl mailSender, MimeMessage mimeMessage, int id) {
        Optional<EmailQueue> optionalEmailQueue = emailQueueRepository.findById(id);
        if (optionalEmailQueue.isPresent()) {
            EmailQueue emailQueue = optionalEmailQueue.get();

            try {
                mailSender.send(mimeMessage);
            } catch (MailException mailException) {
                emailQueueRepository.updateWhenFailureByEmailQueueId(EmailStatus.ERROR, mailException.getLocalizedMessage(), emailQueue.getEmailQueueId());
                throw new EmailException("Could Not Send Email With This Id " + emailQueue.getEmailQueueId());
            }
            emailQueueRepository.updateWhenSuccessByEmailQueueId(EmailStatus.SENT, LocalDateTime.now(), emailQueue.getSentAttemptCount() + 1, emailQueue.getEmailQueueId());
        }
    }
}
