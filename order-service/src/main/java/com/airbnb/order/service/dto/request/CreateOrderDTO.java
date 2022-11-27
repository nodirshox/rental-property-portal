package com.airbnb.order.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderDTO {
    private Integer UserId;
    private String email;
    private String propertyId;
    private double price;
}
