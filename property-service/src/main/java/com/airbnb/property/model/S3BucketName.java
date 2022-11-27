package com.airbnb.property.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum S3BucketName {
    PROPERTY_BUCKET("waa-project");

    private final String s3BucketName;
}

