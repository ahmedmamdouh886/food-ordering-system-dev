package com.food.ordering.system.order.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// So, this class will lot the configuration data from the order service prefix, from a configuration file, like from the application YAML file.
@Data
@Configuration
@ConfigurationProperties(prefix = "order-service")
public class OrderServiceConfigData {
    /**
     * I will create four string fields:
     * payment request topic name,
     * payment response topic name,
     * restaurant approval request topic name,
     * and restaurant approval response topic name.
     *
     * As you remember, I will use four Kafka topics to communicate between the microservices using domain events.
     *
     *
     * OrderServiceConfigData can be, actually should be in the messaging module. In fact it is only
     * used by the messaging module and only has topic names. I initially put the config data class in application
     * service in case different config properties used in the domain layer. But in this final project
     * OrderServiceConfigData can be renamed and put in messaging module to only include topic names. I will
     * change that in the videos and in source code. Thanks for pointing this.
     */
    private String paymentRequestTopicName;
    private String paymentResponseTopicName;
    private String restaurantApprovalRequestTopicName;
    private String restaurantApprovalResponseTopicName;
}
