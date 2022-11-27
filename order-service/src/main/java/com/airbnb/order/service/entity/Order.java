package com.airbnb.order.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @PrimaryKey
    private Integer id;

    private Integer userId;
    private String email;
    private String propertyId;
    private double price;
    private Boolean isPaid;
}
