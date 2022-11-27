package com.airbnb.property.model;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
}
