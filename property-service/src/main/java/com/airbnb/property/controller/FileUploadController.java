package com.airbnb.property.controller;

import com.airbnb.property.model.FileUploadDTO;
import com.airbnb.property.model.FileUploadResponseDTO;
import com.airbnb.property.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/property/files")
public class FileUploadController {
    private final FileUploadService fileUploadService;

    @PostMapping
    public FileUploadResponseDTO upload(@ModelAttribute FileUploadDTO fileUploadDTO) {
        return fileUploadService.upload(fileUploadDTO);
    }
}
