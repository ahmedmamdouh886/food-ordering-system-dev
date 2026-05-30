package com.food.ordering.system.customer.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "customer-service") // The value will be from the application.yml customer-service.customer-topic-name.
public class CustomerServiceConfigData {
    private String customerTopicName; // The value will be from the application.yml customer-service.customer-topic-name.
}
