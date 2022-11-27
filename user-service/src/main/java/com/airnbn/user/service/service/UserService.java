package com.airnbn.user.service.service;

import com.airnbn.user.service.model.*;

public interface UserService {
    LoginResponse login(LoginRequest loginRequest);
    Response validateToken(TokenRequest tokenRequest);
    Response validateTokenInfo(TokenRequest tokenRequest);
    Response getById(Integer id);
    Response create(CreateUserDTO user);
}
