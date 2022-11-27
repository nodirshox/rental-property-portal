package com.airbnb.notification.service.listeners;

import com.airbnb.notification.service.dto.NewNotificationDTO;
import com.airbnb.notification.service.kafka.KafkaObject;
import com.airbnb.notification.service.util.MailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Payload;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners {
    private final ModelMapper mapper;

    private final MailUtil util;

    @KafkaListener(topics = "${config.topic.send-email}", groupId = "group_one", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUserMessage(@Payload KafkaObject kafkaObject) {
        var notification = mapper.map(kafkaObject.getData(), NewNotificationDTO.class);
        log.info("New notification received: email={}, message={}", notification.getEmail(), notification.getMessage());

        util.sendEmail(notification.getEmail(), buildMessage(notification.getEmail(), notification.getMessage()));
    }

    private String buildMessage(String email, String message) {
        return "Dear customer!\n\n" + message + "\n\n"
                + email + "\n"
                + "This is automatic email please do not reply." + "\n" + "Thank you for using our service.";
    }
}