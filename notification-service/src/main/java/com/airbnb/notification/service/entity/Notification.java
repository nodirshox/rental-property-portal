package com.airbnb.notification.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @PrimaryKey
    private Integer id;

    private String email;
    private String message;
    private String error;
}
