package com.airbnb.notification.service.util;

import com.airbnb.notification.service.dto.NewNotificationDTO;
import com.airbnb.notification.service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailUtil {
    private final JavaMailSender sender;
    private final NotificationService notificationService;

    public void sendEmail(String email, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
            message.setTo(email);
            message.setText(text);
            message.setSubject("Confirmation Email");
            message.setFrom("Kamyabali7@gmail.com");
            sender.send(message);
            log.info("New notification send: email={}", email);
        } catch (Exception e) {
            log.error("Error on send email={}, error={}", email, e.getMessage());
            NewNotificationDTO newMessage = new NewNotificationDTO(email, text);
            notificationService.create(newMessage, e.getMessage());
        }
    }
}
