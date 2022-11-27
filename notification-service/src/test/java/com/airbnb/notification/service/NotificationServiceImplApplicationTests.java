package com.airbnb.notification.service;

import com.airbnb.notification.service.util.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NotificationServiceImplApplicationTests {
	@Autowired
	private MailUtil util;
	@Test
	void createNotification() {
		String email = "kamyabali7@gmail.com";
		String message = "hello";

		util.sendEmail(email, message);
	}
}
