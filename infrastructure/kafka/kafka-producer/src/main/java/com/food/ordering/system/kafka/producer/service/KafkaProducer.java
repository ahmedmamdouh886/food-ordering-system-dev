package com.food.ordering.system.kafka.producer.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;
import java.util.function.BiConsumer;

// In Java, Serializable is a marker interface that indicates that objects of a class can be serialized. Serialization is the process of converting an object's state into a byte stream, which can then be saved to a file, sent over a network, or stored in a database. Deserialization is the reverse process of converting a byte stream back into an object.
public interface KafkaProducer<K extends Serializable, V extends SpecificRecordBase> {
    void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback);
}
