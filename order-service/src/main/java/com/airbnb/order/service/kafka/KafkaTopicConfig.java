package com.airbnb.order.service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${config.topic.create-order}")
    private String CREATE_ORDER_TOPIC;

    @Value("${config.topic.update-order-status}")
    private String UPDATE_ORDER_STATUS_TOPIC;

    @Bean
    public NewTopic createOrderTopic() {
        return TopicBuilder.name(CREATE_ORDER_TOPIC).build();
    }

    @Bean
    public NewTopic updateOrderStatusTopic() {
        return TopicBuilder.name(UPDATE_ORDER_STATUS_TOPIC).build();
    }
}
