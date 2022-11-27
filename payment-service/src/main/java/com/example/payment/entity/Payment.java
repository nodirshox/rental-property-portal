package com.example.payment.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "Payment")
public class Payment {

    @Id
    private String id;

    private Integer orderId;
    private Double price;
    private PaymentStatus status;
    private PaymentType paymentType;
    private String response;

    public Payment(Integer orderId, Double price, PaymentType paymentType, String response) {
        this.orderId = orderId;
        this.price = price;
        this.paymentType = paymentType;
        this.response = response;
        this.status = PaymentStatus.NOT_PAID;
    }

    public enum PaymentStatus {
        PAID, NOT_PAID
    }
    public enum PaymentType {
        CC, BANK, PAYPAL
    }
}
