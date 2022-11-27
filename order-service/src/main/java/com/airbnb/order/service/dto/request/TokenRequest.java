package com.airbnb.order.service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRequest {
    private String token;
}