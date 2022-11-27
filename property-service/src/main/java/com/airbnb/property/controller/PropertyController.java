package com.airbnb.property.controller;

import com.airbnb.property.kafka.KafkaObject;
import com.airbnb.property.model.PropertyDTO;
import com.airbnb.property.model.Response;
import com.airbnb.property.model.TokenRequest;
import com.airbnb.property.model.UserDTO;
import com.airbnb.property.service.PropertyService;
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
@RequestMapping("/property/properties")
@RequiredArgsConstructor
@CrossOrigin
public class PropertyController {

    private final PropertyService propertyService;
    private final KafkaTemplate<String, KafkaObject> kafkaTemplate;

    private final RestTemplate restTemplate;

    private final ModelMapper mapper;

    @Value("${config.topic.create-property}")
    private String CREATE_PROPERTY_TOPIC;

    @Value("${config.user-service.host}")
    private String USER_SERVICE_HOST;

    @Value("${config.user-service.port}")
    private Integer USER_SERVICE_PORT;

    @Value("${config.user-service.key}")
    private String USER_SERVICE_SECRET_KEY;

    enum SERVICES {
        USER_SERVICE
    }

    @GetMapping
    public Response getAllProperties() {
        return propertyService.getAllProperties();
    }

    @GetMapping("/cache")
    public Response getAllPropertiesFromCache() {
        return propertyService.getAllPropertiesFromCache();
    }

    @GetMapping("/{id}")
    public Response getById(@PathVariable String id) {
        return propertyService.getById(id);
    }

    @PostMapping
    public Response saveProperty(@RequestBody PropertyDTO propertyDTO,
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

        propertyDTO.setOwnerId(user.getId());
        propertyDTO.setOwnerEmail(user.getEmail());

        KafkaObject response = new KafkaObject(propertyDTO,true);
        kafkaTemplate.send(CREATE_PROPERTY_TOPIC,response);
        return new Response(propertyDTO,true);
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
