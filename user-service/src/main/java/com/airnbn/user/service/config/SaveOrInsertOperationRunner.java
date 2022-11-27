package com.airnbn.user.service.config;

import com.airnbn.user.service.entity.User;
import com.airnbn.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveOrInsertOperationRunner implements CommandLineRunner {
    private final UserRepository repo;

    @Override
    public void run(String... args) {
        repo.saveAll(List.of(
                new User(1, "Admin", "Admin", "admin@mail.com", "$2a$12$IKEQb00u5QpZMx4v5zMweu.3wrq0pS7XLCHO4yHZ.BW/yvWu1feo2", "ADMIN"),
                new User(2, "Owner", "Owner", "owner@mail.com", "$2a$12$IKEQb00u5QpZMx4v5zMweu.3wrq0pS7XLCHO4yHZ.BW/yvWu1feo2", "OWNER"),
                new User(3, "Customer", "Customer", "customer@mail.com", "$2a$12$IKEQb00u5QpZMx4v5zMweu.3wrq0pS7XLCHO4yHZ.BW/yvWu1feo2", "CUSTOMER")
            )
        );
    }
}
