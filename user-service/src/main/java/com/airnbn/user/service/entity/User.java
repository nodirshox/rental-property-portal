package com.airnbn.user.service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @PrimaryKey
    private Integer id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
}