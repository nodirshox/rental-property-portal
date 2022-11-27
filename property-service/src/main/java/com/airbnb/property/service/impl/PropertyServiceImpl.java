package com.airbnb.property.service.impl;

import com.airbnb.property.entity.Product;
import com.airbnb.property.entity.Property;
import com.airbnb.property.kafka.KafkaObject;
import com.airbnb.property.model.NewNotificationDTO;
import com.airbnb.property.model.UpdatePropertyStatusDTO;
import com.airbnb.property.model.PropertyDTO;
import com.airbnb.property.model.Response;
import com.airbnb.property.repository.ProductRepository;
import com.airbnb.property.repository.PropertyRepository;
import com.airbnb.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    @Value("${config.topic.send-email}")
    private String SEND_EMAIL_TOPIC;
    private final KafkaTemplate<String, KafkaObject> kafkaTemplate;
    private final ProductRepository productRepository;

    @Override
    public Response getAllProperties() {
            List<Property> properties = propertyRepository.findAllByIsActive(true);
            return new Response(properties,true);
    }

    @Override
    public Response getAllPropertiesFromCache() {
        List<Product> products = productRepository.findAll();

        if (products.size() == 0) {
        List<Property> properties = propertyRepository.findAllByIsActive(true);

            for(Property property:properties) {
                Product product = new Product();

                product.setId(property.getId());
                product.setPrice(property.getPrice());
                product.setNumberOfRooms(property.getNumberOfRooms());
                product.setOwnerId(property.getOwnerId());
                product.setOwnerEmail(property.getOwnerEmail());
                product.setIsActive(property.getIsActive());

                product.setState(property.getState());
                product.setStreet(property.getStreet());
                product.setZipcode(property.getZipcode());
                product.setPicture(property.getPicture());

                productRepository.save(product);
            }
            return new Response(properties,true);
        }
        return new Response(products,true, "Request completed from cache");
    }

    @Override
    public Response getById(String id) {
        Property property = propertyRepository.findById(id).orElse(null);

        if (property == null) {
            return new Response("Property with id=" + id + " not found", false);
        }
        return new Response(property, true);
    }

    @Override
    public void saveProperty(PropertyDTO propertyDTO) {
        Property property = new Property(
                propertyDTO.getPrice(),
                propertyDTO.getNumberOfRooms(),
                propertyDTO.getOwnerId(),
                propertyDTO.getOwnerEmail(),
                propertyDTO.getPicture(),
                propertyDTO.getStreet(),
                propertyDTO.getState(),
                propertyDTO.getZipcode());
        propertyRepository.save(property);
        new Response(propertyDTO, true);
    }

    @Override
    public void changePropertyStatus(UpdatePropertyStatusDTO updatePropertyStatusDTO) {
        Property property = propertyRepository.findById(updatePropertyStatusDTO.getPropertyId()).orElse(null);
        if (property == null) {
            log.error("Could not find property: {}", updatePropertyStatusDTO.getPropertyId());
            return;
        }
        property.setIsActive(false);
        propertyRepository.save(property);
        log.info("Successfully changed property status: {}", updatePropertyStatusDTO.getPropertyId());
        sendNotification(property);
    }

    void sendNotification(Property property) {
        NewNotificationDTO notification = new NewNotificationDTO(property.getOwnerEmail(), "Property(" + property.getId() + ") is paid");
        KafkaObject response = new KafkaObject(notification, true);

        Message<KafkaObject> message = MessageBuilder
                .withPayload(response)
                .setHeader(KafkaHeaders.TOPIC, SEND_EMAIL_TOPIC)
                .build();
        kafkaTemplate.send(message);
    }
}
