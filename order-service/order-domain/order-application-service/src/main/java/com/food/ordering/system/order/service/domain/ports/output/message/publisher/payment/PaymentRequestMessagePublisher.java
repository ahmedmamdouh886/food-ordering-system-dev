package com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment;

import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.outbox.OutboxStatus;

import java.util.function.BiConsumer;

/**
 * In this interface, I will add a single method, publish, and use OrderPaymentOutboxMessage as the first parameter.
 *
 * As a second parameter, I will pass a BitConsumer interface, which is a functional interface, comes with Java 8.
 *
 * It basically accepts two parameters and doesn't return anything.
 *
 * It accepts the parameters as generic types.
 *
 * I will use OrderPaymentOutboxMessage and outboxStatus for the two parameters for this BiConsumer interface.
 *
 * In the implementation, I will have a methods that takes these two parameters and return void, and then I will simply pass that methods definition and let it be called in the published method.
 *
 * So why do I need that?
 *
 * Because I want to update OutboxStatus as completed or failed. depending on the result of these published operation.
 *
 * This published methods will be implemented in the order messaging module with an adapter.
 *
 * It will use Kafka Producer.
 *
 * And only in that adapter I will know that if Kafka Producer send methods successfully sent the data or not.
 *
 * Remember that Kafka Producer send method is an asynchronous methods and it uses a callback methods to be called later.
 *
 * When I send this BiConsumer as parameter to the publish methods, I will be able to call it in the Kafka Producer callback methods.
 *
 * Remember that Kafka Producer callback methods handles failure and success cases.
 *
 * I will simply call the accept methods of BiConsumer in those failure and success methods and update the outboxStatus in the local database.
 *
 * Stay tuned for the implementation.
 */
public interface PaymentRequestMessagePublisher {
    void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);
}
