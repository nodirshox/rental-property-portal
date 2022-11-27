package com.example.payment.service.impl;

import com.example.payment.dto.UserDTO;
import com.example.payment.dto.request.NewNotificationDTO;
import com.example.payment.dto.request.UpdateOrderStatusDTO;
import com.example.payment.dto.request.UpdatePropertyStatusDTO;
import com.example.payment.entity.Payment;
import com.example.payment.kafka.KafkaObject;
import com.example.payment.repository.PaymentRepository;
import com.example.payment.request.PaymentRequest;
import com.example.payment.request.TokenRequest;
import com.example.payment.response.Response;
import com.example.payment.service.PaymentService;
import com.example.payment.service.PaymentServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${config.topic.update-order-status}")
    private String UPDATE_ORDER_TOPIC;

    @Value("${config.topic.update-property-status}")
    private String CHANGE_PROPERTY_STATUS_TOPIC;

    @Value("${config.topic.send-email}")
    private String SEND_EMAIL_TOPIC;

    @Value("${config.user-service.host}")
    private String USER_SERVICE_HOST;

    @Value("${config.user-service.port}")
    private Integer USER_SERVICE_PORT;

    @Value("${config.user-service.key}")
    private String USER_SERVICE_SECRET_KEY;

    private final PaymentServiceType paymentServiceType;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;
    private final KafkaTemplate<String, KafkaObject> kafkaTemplate;
    private UserDTO user;

    private final PaymentRepository paymentRepository;
    enum SERVICES {
        USER_SERVICE
    }

    @Override
    public Response makePayment(PaymentRequest paymentRequest) {
        log.info("New payment request orderId={}, propertyId={}", paymentRequest.getOrderId(), paymentRequest.getPropertyId());
        Response response = paymentServiceType.pay(paymentRequest.getPrice());
        paymentRequest.setUserId(this.user.getId());

        if (!response.getSuccess()) {
            return new Response(paymentRequest, false);
        }

        // save to mongo
        Payment payment = new Payment();
        payment.setOrderId(paymentRequest.getOrderId());
        payment.setPrice(paymentRequest.getPrice());
        payment.setStatus(Payment.PaymentStatus.PAID);
        payment.setPaymentType(paymentRequest.getPaymentType());
        payment.setResponse("Completed");
        paymentRepository.save(payment);

        this.changeOrderStatus(paymentRequest.getOrderId());
        this.changePropertyStatus(paymentRequest.getPropertyId());
        this.sendEmailToUser(this.user, paymentRequest.getOrderId(), paymentRequest.getPrice());

        return new Response(paymentRequest, true);
    }

    @Override
    public Boolean checkAuthValid(String token) {
        String USER_SERVICE = "http://" + USER_SERVICE_HOST + ":" + USER_SERVICE_PORT;
        TokenRequest tokenRequest = new TokenRequest(token);

        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(tokenRequest, getHeader(SERVICES.USER_SERVICE));
        Response userInfo = restTemplate.postForObject(USER_SERVICE + "/user/validate-token", httpEntity, Response.class);
        assert userInfo != null;

        this.user = mapper.map(userInfo.getData(), UserDTO.class);
        return userInfo.getSuccess();
    }

    @Override
    public void changeOrderStatus(int orderId) {
        KafkaObject response = new KafkaObject(new UpdateOrderStatusDTO(orderId), true);

        Message<KafkaObject> message = MessageBuilder
                .withPayload(response)
                .setHeader(KafkaHeaders.TOPIC, UPDATE_ORDER_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    @Override
    public void sendEmailToUser(UserDTO user, int orderId, double price) {
        NewNotificationDTO notification = new NewNotificationDTO(user.getEmail(), "Payment with orderId(" + orderId + "), price $" + price + " is successfully paid.");
        KafkaObject response = new KafkaObject(notification, true);

        Message<KafkaObject> message = MessageBuilder
                .withPayload(response)
                .setHeader(KafkaHeaders.TOPIC, SEND_EMAIL_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    @Override
    public void changePropertyStatus(String propertyId) {
        KafkaObject response = new KafkaObject(new UpdatePropertyStatusDTO(propertyId), true);

        Message<KafkaObject> message = MessageBuilder
                .withPayload(response)
                .setHeader(KafkaHeaders.TOPIC, CHANGE_PROPERTY_STATUS_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }

    private HttpHeaders getHeader(SERVICES type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        if (type == SERVICES.USER_SERVICE) {
            headers.set("key", USER_SERVICE_SECRET_KEY);
        }

        return headers;
    }
}
