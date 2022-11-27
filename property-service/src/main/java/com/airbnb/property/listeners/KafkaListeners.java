package com.airbnb.property.listeners;

import com.airbnb.property.kafka.KafkaObject;
import com.airbnb.property.model.UpdatePropertyStatusDTO;
import com.airbnb.property.model.PropertyDTO;
import com.airbnb.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListeners {
    private final ModelMapper mapper;

    private final PropertyService propertyService;

    @KafkaListener(topics = "${config.topic.create-property}", groupId = "group_one", containerFactory = "kafkaListenerContainerFactory")
    void createOrderListener(KafkaObject kafkaObject) {
        var propertyDTO = mapper.map(kafkaObject.getData(), PropertyDTO.class);
        propertyService.saveProperty(propertyDTO);
        log.info("Received: " + propertyDTO);
    }

    @KafkaListener(topics = "${config.topic.update-property-status}", groupId = "group_one", containerFactory = "kafkaListenerContainerFactory")
    void changePropertyStatus(@Payload KafkaObject kafkaObject) {
        var kafkaPropertyDTO = mapper.map(kafkaObject.getData(), UpdatePropertyStatusDTO.class);
        log.info("Received: " + kafkaPropertyDTO);
        propertyService.changePropertyStatus(kafkaPropertyDTO);
    }
}
