package com.food.ordering.system.kafka.config.data;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration // To mark this class as a Spring bean and be loaded and injected when necessary.
@ConfigurationProperties(prefix = "kafka-consumer-config") // To map the configuration values from the application configuration file like from an application.yaml file, to the fields defined in this class. For the prefix = "kafka-consumer-config" it's the base configuration value that will be used to map the config data from. See 28. Implementing Kafka config data generic module lesson at 06:42.
public class KafkaConsumerConfigData {
    private String keyDeserializer;
    private String valueDeserializer;
    private String autoOffsetReset;
    private String specificAvroReaderKey;
    private String specificAvroReader;
    private Boolean batchListener;
    private Boolean autoStartup;
    private Integer concurrencyLevel;
    private Integer sessionTimeoutMs;
    private Integer heartbeatIntervalMs;
    private Integer maxPollIntervalMs;
    private Long pollTimeoutMs;
    private Integer maxPollRecords;
    private Integer maxPartitionFetchBytesDefault;
    private Integer maxPartitionFetchBytesBoostFactor;
}
