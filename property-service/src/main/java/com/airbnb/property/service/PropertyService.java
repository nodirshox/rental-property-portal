package com.airbnb.property.service;

import com.airbnb.property.model.UpdatePropertyStatusDTO;
import com.airbnb.property.model.PropertyDTO;
import com.airbnb.property.model.Response;

public interface PropertyService {
    Response getAllProperties();
    Response getAllPropertiesFromCache();

    Response getById(String id);

    void saveProperty(PropertyDTO propertyDTO);

    void changePropertyStatus(UpdatePropertyStatusDTO updatePropertyStatusDTO);
}
