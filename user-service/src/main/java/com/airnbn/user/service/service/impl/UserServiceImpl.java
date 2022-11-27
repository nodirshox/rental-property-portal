package com.airnbn.user.service.service.impl;

import com.airnbn.user.service.entity.User;
import com.airnbn.user.service.model.*;
import com.airnbn.user.service.repository.UserRepository;
import com.airnbn.user.service.security.JwtHelper;
import com.airnbn.user.service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            log.error("Incorrect Credentials with email={}", loginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Credentials");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.error("Incorrect Credentials with email={}", loginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Credentials");
        }

        final String accessToken = jwtHelper.generateToken(loginRequest.getEmail());
        var loginResponse = new LoginResponse(accessToken);
        return loginResponse;
    }

    @Override
    public Response validateToken(TokenRequest tokenRequest) {
        boolean isValid = jwtHelper.validateToken(tokenRequest.getToken());
        if (!isValid) {
            log.error("Invalid token while validating the token={}", tokenRequest.getToken());
            return new Response(null,false);
        }

        var email = jwtHelper.getUsernameFromToken(tokenRequest.getToken());

        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User with email={} not found", email);
            return new Response(null,false);
        }
        var response = mapper.map(user, UserDTO.class);

        return new Response(response,true);
    }

    @Override
    public Response validateTokenInfo(TokenRequest tokenRequest) {
        boolean isValid = jwtHelper.validateToken(tokenRequest.getToken());
        if (!isValid) {
            log.error("Invalid token while validating the token={}", tokenRequest.getToken());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        var email = jwtHelper.getUsernameFromToken(tokenRequest.getToken());

        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.error("User with email={} not found", email);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
        var response = mapper.map(user, UserDTO.class);

        return new Response(response,true);
    }

    @Override
    public Response getById(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new Response(null, false);
        }

        var response = mapper.map(user, UserDTO.class);

        return new Response(response, true);
    }

    @Override
    public Response create(CreateUserDTO newUser) {
        if (newUser.getRole().equals("ADMIN")) {
            log.error("Error while creating user. Invalid role={}", newUser.getRole());
            return new Response("Invalid user role", false);
        }

        User existingUser = userRepository.findByEmail(newUser.getEmail());

        if (existingUser != null) {
            log.error("User with email={} is already exists", newUser.getEmail());
            return new Response("User with email=" + newUser.getEmail() + " is already exists", false);
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        var user = mapper.map(newUser, User.class);
        user.setId((int)userRepository.count() + 1);
        User createdUser = userRepository.save(user);
        var response =  mapper.map(createdUser, CreatedUserDTO.class);
        final String accessToken = jwtHelper.generateToken(newUser.getEmail());
        response.setAccessToken(accessToken);
        return new Response(response, true);
    }
}