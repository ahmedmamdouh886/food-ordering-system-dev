package com.food.ordering.system.kafka.config.data;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration // To mark this class as a Spring bean and be loaded and injected when necessary.
@ConfigurationProperties(prefix = "kafka-producer-config") // To map the configuration values from the application configuration file like from an application.yaml file, to the fields defined in this class. For the prefix = "kafka-producer-config" it's the base configuration value that will be used to map the config data from. See 28. Implementing Kafka config data generic module lesson at 06:42.
public class KafkaProducerConfigData {
    private String keySerializerClass;
    private String valueSerializerClass;
    private String compressionType;
    private String acks;
    private Integer batchSize;
    private Integer batchSizeBoostFactor;
    private Integer lingerMs;
    private Integer requestTimeoutMs;
    private Integer retryCount;
}
