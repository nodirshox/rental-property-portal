package com.airbnb.notification.service.service;

import com.airbnb.notification.service.dto.NewNotificationDTO;

public interface NotificationService {
    void create(NewNotificationDTO notification, String error);
}
