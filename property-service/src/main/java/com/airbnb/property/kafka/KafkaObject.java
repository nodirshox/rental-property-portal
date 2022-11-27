package com.airbnb.property.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaObject {
    private Boolean success;
    private String message;
    private Object data;

    public KafkaObject(Object data, boolean success) {
        this.data = data;
        this.success = success;
        this.message = "Request completed";
    }
}
