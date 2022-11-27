package com.airbnb.order.service.dto.request;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
}
