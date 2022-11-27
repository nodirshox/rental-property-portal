package com.airbnb.notification.service.repository;

import com.airbnb.notification.service.entity.Notification;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends CassandraRepository<Notification, Integer> {
}
