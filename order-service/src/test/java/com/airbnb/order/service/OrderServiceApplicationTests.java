package com.airbnb.order.service;

import com.airbnb.order.service.dto.request.CreateOrderDTO;
import com.airbnb.order.service.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceApplicationTests {
	@Autowired
	private OrderService orderService;

	@Test
	void createOrder() {
		CreateOrderDTO createOrderDTO = new CreateOrderDTO(1, "admin@mail.com", "123", 990);
		orderService.create(createOrderDTO);
	}

	@Test
	void updateOrder() {
		int orderId = 1;
		orderService.updateIsPaidByOrderId(orderId);
	}
}
