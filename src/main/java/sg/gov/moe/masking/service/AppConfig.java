package sg.gov.moe.masking.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;

@Configuration
public class AppConfig {

    @Bean
    public AWSSecretsManager secretsManager() {
        return AWSSecretsManagerClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_2)
                .build();
    }
}