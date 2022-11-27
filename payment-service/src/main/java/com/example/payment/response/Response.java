package com.example.payment.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Boolean success;
    private String message;
    private Object data;

    public Response(Object data, boolean success){
        this.data = data;
        this.success = success;
        this.message = "Request completed";
    }

}
