package com.airbnb.order.service.service.impl;

import com.airbnb.order.service.dto.request.CreateOrderDTO;
import com.airbnb.order.service.dto.request.NewNotificationDTO;
import com.airbnb.order.service.dto.response.Response;
import com.airbnb.order.service.entity.Order;
import com.airbnb.order.service.kafka.KafkaObject;
import com.airbnb.order.service.repository.OrderRepository;
import com.airbnb.order.service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Value("${config.topic.send-email}")
    private String SEND_EMAIL_TOPIC;

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, KafkaObject> kafkaTemplate;

    @Override
    public void create(CreateOrderDTO order) {
        Order newOrder = new Order();
        newOrder.setId((int)System.currentTimeMillis());
        newOrder.setUserId(order.getUserId());
        newOrder.setEmail(order.getEmail());
        newOrder.setPropertyId(order.getPropertyId());
        newOrder.setPrice(order.getPrice());
        newOrder.setIsPaid(false);

        orderRepository.save(newOrder);
    }

    @Override
    public Response findAllByUserId(int id) {
        return new Response(orderRepository.findAllByUserId(id), true);
    }

    @Override
    public Response getAll() {
        return new Response(orderRepository.findAll(), true);
    }

    @Override
    public Response findById(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            log.info("Order with id={} not found", id);
            return new Response("Order with id=" + id + " not found", false);
        }

        return new Response(order, true);
    }

    @Override
    public void updateIsPaidByOrderId(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            log.info("Order with id={} not found", id);
            return;
        }

        order.setIsPaid(true);
        orderRepository.save(order);
        log.info("Status of order with id={} is changed to Paid", id);
        sendNotification(order);
    }

    void sendNotification(Order order) {
        NewNotificationDTO notification = new NewNotificationDTO(order.getEmail(), "Order(" + order.getId() + ") is activated");
        KafkaObject response = new KafkaObject(notification, true);

        Message<KafkaObject> message = MessageBuilder
                .withPayload(response)
                .setHeader(KafkaHeaders.TOPIC, SEND_EMAIL_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }
}
