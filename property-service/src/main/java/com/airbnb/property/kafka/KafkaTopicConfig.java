package com.airbnb.property.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Value("${config.topic.create-property}")
    private String CREATE_ORDER_TOPIC;
    @Bean
    public NewTopic myTopic() {
        return TopicBuilder.name(CREATE_ORDER_TOPIC).build();
    }
}
