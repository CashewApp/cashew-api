package br.app.cashew.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;

@Configuration
public class AwsKmsClientConfig {


    @Bean
    public KmsClient kmsClient() {
        return KmsClient
                .builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.SA_EAST_1)
                .build();
    }
}
