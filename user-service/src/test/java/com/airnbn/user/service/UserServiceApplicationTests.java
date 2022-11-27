package com.airnbn.user.service;

import com.airnbn.user.service.model.LoginRequest;
import com.airnbn.user.service.model.TokenRequest;
import com.airnbn.user.service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceApplicationTests {
	@Autowired
	private UserService userService;

	@Test
	void login() {
		LoginRequest loginRequest = new LoginRequest("admin@mail.com", "123");
		assertNotNull(userService.login(loginRequest));
	}

	@Test
	void validateToken() {
		TokenRequest tokenRequest = new TokenRequest("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsImlhdCI6MTY2NjkwNzc5MSwiZXhwIjoxNjY2OTI5MzkxfQ.kb2fPlLcHWrcWGnQIEY3vL-o62ERrcIS6yM8kHm7PQ2AkVxsdaaKSufuyPm9SDPqMjhvoMQids5wtm_4yjBbRw");
		assertNotNull(userService.validateToken(tokenRequest));
	}
}
