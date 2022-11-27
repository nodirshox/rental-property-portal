package com.example.payment.request;

import lombok.Data;

@Data
public class TokenRequest {
    private String token;

    public TokenRequest(String token) {
        this.token = token;
    }
}
