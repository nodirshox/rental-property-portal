package com.example.payment.dto.request;

import lombok.Data;

@Data
public class UpdateOrderStatusDTO {
    private int orderId;

    public UpdateOrderStatusDTO(int orderId) {
        this.orderId = orderId;
    }
}