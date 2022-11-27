package com.airbnb.property.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "property")
public class Property {
    @Id
    private String id;
    private double price;
    private int numberOfRooms;
    private Integer ownerId;
    private String ownerEmail;
    private Boolean isActive;
    private String picture;
    private String street;
    private String state;
    private String zipcode;

    public Property(double price, int numberOfRooms, Integer ownerId, String ownerEmail, String picture, String street, String state, String zipcode) {
        this.isActive = true;
        this.picture = picture;
        this.price = price;
        this.numberOfRooms = numberOfRooms;
        this.ownerId = ownerId;
        this.ownerEmail = ownerEmail;
        this.street = street;
        this.state = state;
        this.zipcode = zipcode;
    }
}
