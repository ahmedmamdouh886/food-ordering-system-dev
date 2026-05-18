package com.food.ordering.system.order.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;


// This event is used to publish a message to the restaurant service on the request-approval-request-topic.
public class OrderPaidEvent extends OrderEvent {

    public OrderPaidEvent(
            Order order,
            ZonedDateTime createdAt
    ) {
        super(order, createdAt);
    }
}
