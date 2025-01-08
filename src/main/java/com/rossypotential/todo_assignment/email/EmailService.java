package com.rossypotential.todo_assignment.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendHtmlMessageForResetPassword(EmailDetails message, String name, String link) throws MessagingException, JsonProcessingException;

    void sendEmailAlert(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);

    public void sendSimpleMailMessage(String name, String to, String token);

    void sendHtmlMessageToVerifyEmail(EmailDetails message, String name, String link) throws MessagingException, JsonProcessingException;
}
