package com.example.payment.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaObject {
    private Boolean success;
    private String message;
    private Object data;

    public KafkaObject(Object data, Boolean success) {
        this.success = success;
        this.data = data;
        this.message = "Request completed";
    }
}
