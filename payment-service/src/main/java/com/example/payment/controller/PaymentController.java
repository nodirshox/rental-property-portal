package com.example.payment.controller;


import com.example.payment.request.PaymentRequest;
import com.example.payment.request.TokenRequest;
import com.example.payment.response.Response;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment/payments")
@RequiredArgsConstructor
@CrossOrigin
public class PaymentController {
    private final PaymentService paymentService;


    @PostMapping
    public Response makePayment(@RequestBody PaymentRequest request, @RequestHeader(name="Authorization") String token){

        // TODO: check condition with old token
        if (!paymentService.checkAuthValid(token))
            return new Response(null, false);

        return paymentService.makePayment(request);
    }
}
