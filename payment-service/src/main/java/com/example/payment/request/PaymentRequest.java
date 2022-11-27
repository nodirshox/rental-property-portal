package com.example.payment.request;

import com.example.payment.entity.Payment;
import lombok.Data;

@Data
public class PaymentRequest {
    private Integer orderId;
    private Integer userId;
    private String propertyId;
    private Double price;
    private Payment.PaymentType paymentType;

    public PaymentRequest(Integer orderId, Integer userId, String propertyId, Double price, Payment.PaymentType paymentType) {
        this.orderId = orderId;
        this.userId = userId;
        this.propertyId = propertyId;
        this.price = price;
        this.paymentType = paymentType;
    }
}
