package com.airbnb.property.service;


import com.airbnb.property.model.FileUploadDTO;
import com.airbnb.property.model.FileUploadResponseDTO;

public interface FileUploadService {
    FileUploadResponseDTO upload(FileUploadDTO fileUploadDTO);
}
