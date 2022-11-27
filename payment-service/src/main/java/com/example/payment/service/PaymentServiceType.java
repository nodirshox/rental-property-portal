package com.example.payment.service;

import com.example.payment.response.Response;

public interface PaymentServiceType {
    Response pay(Double price);
}
