package com.example.payment;

import com.example.payment.entity.Payment;
import com.example.payment.request.PaymentRequest;
import com.example.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentApplicationTests {

	@Autowired
	PaymentService payment;

	@Test
    public void makePayment(){
		PaymentRequest paymentRequest = new PaymentRequest(1,1,"1",100.00, Payment.PaymentType.CC);
		assertNotNull(payment.makePayment(paymentRequest));
    }

	@Test
	public void checkAuth() {
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtci5ub2RpcmJlazc3QGdtYWlsLmNvbSIsImlhdCI6MTY2NjkwNzM5OSwiZXhwIjoxNjY2OTI4OTk5fQ.bqrn0AUyD1XSv1KyJxb9kSYW6LBaFShsoa9A8rnmb-AWzlMHot3uax0bVqgPnrd_kTZ5gNQTj7paAERZUzyYWA";

		assertNotNull(payment.checkAuthValid(token));
	}

}
