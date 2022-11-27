package com.example.payment.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${config.topic.send-email}")
    private String SEND_EMAIL_TOPIC;
    @Value("${config.topic.update-order-status}")
    private String UPDATE_ORDER_STATUS;

    @Value("${config.topic.update-property-status}")
    private String UPDATE_PROPERTY_STATUS;

    @Bean
    public NewTopic sendEmail(){
        return TopicBuilder.name(SEND_EMAIL_TOPIC).build();
    }

    @Bean
    public NewTopic changeOrderStatus(){
        return TopicBuilder.name(UPDATE_ORDER_STATUS).build();
    }

    @Bean
    public NewTopic updatePropertyStatus(){
        return TopicBuilder.name(UPDATE_PROPERTY_STATUS).build();
    }
}
