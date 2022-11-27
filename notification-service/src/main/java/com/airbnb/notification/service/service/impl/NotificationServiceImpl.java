package com.airbnb.notification.service.service.impl;

import com.airbnb.notification.service.dto.NewNotificationDTO;
import com.airbnb.notification.service.entity.Notification;
import com.airbnb.notification.service.repository.NotificationRepository;
import com.airbnb.notification.service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public void create(NewNotificationDTO notification, String error) {
        Notification newNotification = new Notification();
        newNotification.setId((int)System.currentTimeMillis());
        newNotification.setEmail(notification.getEmail());
        newNotification.setMessage(notification.getMessage());
        newNotification.setError(error);

        notificationRepository.save(newNotification);
    }
}
