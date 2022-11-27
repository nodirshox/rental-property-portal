package com.airbnb.order.service.service;

import com.airbnb.order.service.dto.request.CreateOrderDTO;
import com.airbnb.order.service.dto.response.Response;

public interface OrderService {
    void create(CreateOrderDTO order);
    Response findAllByUserId(int id);
    Response getAll();
    Response findById(int id);
    void updateIsPaidByOrderId(int id);
}
