package com.example.payment.service;

import com.example.payment.dto.UserDTO;
import com.example.payment.request.PaymentRequest;
import com.example.payment.response.Response;

public interface PaymentService {

    Response makePayment(PaymentRequest paymentRequest);

    Boolean checkAuthValid(String token);

    void changeOrderStatus(int orderId);

    void sendEmailToUser(UserDTO user, int orderId, double price);

    void changePropertyStatus(String propertyId);
}
