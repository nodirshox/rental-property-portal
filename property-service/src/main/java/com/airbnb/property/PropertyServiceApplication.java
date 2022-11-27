package com.airbnb.property;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableMongoRepositories
public class PropertyServiceApplication {
	@Value("${aws.s3.access_key}")
	private String accessKey;
	@Value("${aws.s3.secret_key}")
	private String secretKey;
	@Value("${aws.zone}")
	private String zone;

	public static void main(String[] args) {
		SpringApplication.run(PropertyServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public AmazonS3 s3() {

		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder
				.standard()
				.withRegion(zone)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();

	}
}
