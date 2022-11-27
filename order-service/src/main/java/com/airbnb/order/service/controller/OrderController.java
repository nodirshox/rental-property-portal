package com.airbnb.order.service.controller;

import com.airbnb.order.service.dto.request.CreateOrderDTO;
import com.airbnb.order.service.dto.request.TokenRequest;
import com.airbnb.order.service.dto.request.UserDTO;
import com.airbnb.order.service.dto.response.Response;
import com.airbnb.order.service.kafka.KafkaObject;
import com.airbnb.order.service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequestMapping("/order/orders")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {
    private final KafkaTemplate<String, KafkaObject> kafkaTemplate;
    private final OrderService orderService;
    private final RestTemplate restTemplate;

    private final ModelMapper mapper;
    enum SERVICES {
        USER_SERVICE
    }

    @Value("${config.topic.create-order}")
    private String CREATE_ORDER_TOPIC;

    @Value("${config.topic.update-order-status}")
    private String UPDATE_ORDER_STATUS_TOPIC;

    @Value("${config.user-service.host}")
    private String USER_SERVICE_HOST;

    @Value("${config.user-service.port}")
    private Integer USER_SERVICE_PORT;

    @Value("${config.user-service.key}")
    private String USER_SERVICE_SECRET_KEY;

    @PostMapping
    public Response create(@RequestBody CreateOrderDTO order,
                           @RequestHeader (name="Authorization") String token) {
        String USER_SERVICE = "http://"+ USER_SERVICE_HOST +":" + USER_SERVICE_PORT;
        TokenRequest tokenRequest = new TokenRequest(token);
        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(tokenRequest, getHeader(SERVICES.USER_SERVICE));

        Response userInfo = restTemplate.postForObject(USER_SERVICE + "/user/validate-token", httpEntity, Response.class);
        assert userInfo != null;

        if (!userInfo.getSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Credentials");
        }

        var user = mapper.map(userInfo.getData(), UserDTO.class);
        order.setUserId(user.getId());
        order.setEmail(user.getEmail());

        KafkaObject response = new KafkaObject(order, true);
        kafkaTemplate.send(CREATE_ORDER_TOPIC, response);
        return new Response("Order received", true);
    }

    @GetMapping("/user")
    public Response getOrdersByUserId(@RequestHeader (name="Authorization") String token) {
        String USER_SERVICE = "http://"+ USER_SERVICE_HOST +":" + USER_SERVICE_PORT;
        TokenRequest tokenRequest = new TokenRequest(token);
        HttpEntity<TokenRequest> httpEntity = new HttpEntity<>(tokenRequest, getHeader(SERVICES.USER_SERVICE));

        Response userInfo = restTemplate.postForObject(USER_SERVICE + "/user/validate-token", httpEntity, Response.class);
        assert userInfo != null;

        if (!userInfo.getSuccess()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Credentials");
        }

        var user = mapper.map(userInfo.getData(), UserDTO.class);

        return orderService.findAllByUserId(user.getId());
    }

    @GetMapping
    public Response getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable Integer id) {
        return orderService.findById(id);
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
