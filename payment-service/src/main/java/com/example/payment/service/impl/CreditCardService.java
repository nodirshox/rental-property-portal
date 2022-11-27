package com.example.payment.service.impl;

import com.example.payment.response.Response;
import com.example.payment.service.PaymentServiceType;
import org.springframework.stereotype.Service;

@Service
public class CreditCardService implements PaymentServiceType {

    @Override
    public Response pay(Double price) {
        return new Response(null, true);
    }
}
