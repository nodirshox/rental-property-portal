package com.airnbn.user.service.model;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
}
