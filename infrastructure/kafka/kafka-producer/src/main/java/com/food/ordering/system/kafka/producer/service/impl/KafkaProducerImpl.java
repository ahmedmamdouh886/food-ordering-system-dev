package com.food.ordering.system.kafka.producer.service.impl;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import com.food.ordering.system.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;

import jakarta.annotation.PreDestroy;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;


// I will keep the generic types as it is so that I will be able to use this class from any service with any model.
@Slf4j
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Why do we use a callback here?
    // Because the send methods on Kafka producer is a non-blocking asynchronous call so it will not return a results immediately.
    // Instead, it requires a callback methods to be called later asynchronously.
    @Override
    public void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback) {

        log.info("Sending message={} to topic={}", message, topicName);

        try {
            CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);

            kafkaResultFuture.whenComplete(callback);
        } catch (KafkaException e) {
            log.error("Error on kafka producer with key: {}, message: {} and exception: {}",
                    key,
                    message,
                    e.getMessage(),
                    e
            );

            throw new KafkaProducerException("Error on kafka producer with key: " + key + " and message: " + message);
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
