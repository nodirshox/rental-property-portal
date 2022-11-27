package com.airnbn.user.service.model;

import lombok.Data;

@Data
public class CreatedUserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String accessToken;
    private String role;
}
