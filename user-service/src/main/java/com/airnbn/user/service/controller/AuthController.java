package com.airnbn.user.service.controller;

import com.airnbn.user.service.model.CreateUserDTO;
import com.airnbn.user.service.model.LoginRequest;
import com.airnbn.user.service.model.Response;
import com.airnbn.user.service.model.TokenRequest;
import com.airnbn.user.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class AuthController {

    private final UserService userService;
    @Value("${config.secret-key}")
    private String SECRET_KEY;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Response response = new Response(userService.login(loginRequest), true);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/validate-token")
    public Response validateToken(@RequestBody TokenRequest tokenRequest,
                                  @RequestHeader(required = false) HttpHeaders headers) {
        log.info("Header values={}, key={}", headers.toString(), headers.get("key"));
        if (headers.get("key") == null || !headers.get("key").contains(SECRET_KEY)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Credentials");
        }

        return userService.validateToken(tokenRequest);
    }

    @GetMapping("/info/{id}")
    public Response getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping("/me")
    public Response userInfo(@RequestHeader (name="Authorization") String token) {
        if (token.length() == 0) {
            return new Response("Authorization header is required", false);
        }
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken(token);
        return userService.validateTokenInfo(tokenRequest);
    }

    @PostMapping("/create")
    public Response create(@RequestBody CreateUserDTO user) {
        return userService.create(user);
    }
}