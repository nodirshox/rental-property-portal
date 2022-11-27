package com.airbnb.order.service.listeners;

import com.airbnb.order.service.dto.request.CreateOrderDTO;
import com.airbnb.order.service.dto.request.UpdateOrderStatusDTO;
import com.airbnb.order.service.kafka.KafkaObject;
import com.airbnb.order.service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Payload;

@Component
@RequiredArgsConstructor
public class KafkaListeners {
    private final ModelMapper mapper;
    private final OrderService orderService;

    @KafkaListener(topics = "${config.topic.create-order}", groupId = "group_one", containerFactory = "kafkaListenerContainerFactory")
    void createOrderListener(@Payload KafkaObject kafkaObject) {
        var order = mapper.map(kafkaObject.getData(), CreateOrderDTO.class);
        orderService.create(order);
    }

    @KafkaListener(topics = "${config.topic.update-order-status}", groupId = "group_one", containerFactory = "kafkaListenerContainerFactory")
    public void consumeUserMessage(@Payload KafkaObject kafkaObject) {
        var update = mapper.map(kafkaObject.getData(), UpdateOrderStatusDTO.class);
        orderService.updateIsPaidByOrderId(update.getOrderId());
    }
}
