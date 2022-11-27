package com.airbnb.property.repository;

import com.airbnb.property.entity.Property;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends MongoRepository<Property, String> {
    List<Property> findAllByIsActive(boolean status);
}
