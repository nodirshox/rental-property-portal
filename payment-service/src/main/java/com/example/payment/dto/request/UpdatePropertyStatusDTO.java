package com.example.payment.dto.request;

import lombok.Data;

@Data
public class UpdatePropertyStatusDTO {
    private String propertyId;

    public UpdatePropertyStatusDTO(String propertyId) {
        this.propertyId = propertyId;
    }
}
