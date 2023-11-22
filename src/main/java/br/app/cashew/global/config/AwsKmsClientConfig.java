package br.app.cashew.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;

import java.net.URI;

@Configuration
public class AwsKmsClientConfig {

    @Value("${aws.endpoint}")
    public String awsEndpoint;

    @Bean
    public KmsClient kmsClient() {
        return KmsClient
                .builder()
                .endpointOverride(URI.create(awsEndpoint))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.SA_EAST_1)
                .build();
    }
}
