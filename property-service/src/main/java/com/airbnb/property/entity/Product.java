package com.airbnb.property.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Product")
public class Product implements Serializable {
    @Id
    private String id;
    private double price;
    private int numberOfRooms;
    private int ownerId;
    private String ownerEmail;
    private Boolean isActive;
    private String picture;
    private String street;
    private String state;
    private String zipcode;
}
