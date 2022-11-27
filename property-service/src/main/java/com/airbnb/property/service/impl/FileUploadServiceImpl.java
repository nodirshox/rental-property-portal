package com.airbnb.property.service.impl;

import com.airbnb.property.model.FileUploadDTO;
import com.airbnb.property.model.FileUploadResponseDTO;
import com.airbnb.property.model.S3BucketName;
import com.airbnb.property.service.FileUploadService;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final AmazonS3 amazonS3Client;
    private String AWS_URL = String.format("https://%s.s3.amazonaws.com/",
            S3BucketName.PROPERTY_BUCKET.getS3BucketName());
    @Override
    public FileUploadResponseDTO upload(FileUploadDTO fileUploadDTO) {
        String url = uploadFileToAWSAndGetUrl(fileUploadDTO.getFile());
        return new FileUploadResponseDTO(url);
    }

    private String uploadFileToAWSAndGetUrl(MultipartFile multipartFile) {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        String keyName = new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");

        try {
            amazonS3Client
                    .putObject(S3BucketName.PROPERTY_BUCKET.getS3BucketName(), keyName,
                            multipartFile.getInputStream(),
                            metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AWS_URL.concat(keyName);
    }
}
