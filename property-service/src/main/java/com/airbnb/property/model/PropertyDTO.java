package com.airbnb.property.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO {
    private double price;
    private int numberOfRooms;
    private Integer ownerId;
    private String ownerEmail;
    private String picture;
    private String street;
    private String state;
    private String zipcode;
}
