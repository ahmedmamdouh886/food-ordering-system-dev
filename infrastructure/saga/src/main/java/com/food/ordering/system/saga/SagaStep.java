package com.food.ordering.system.saga;

import com.food.ordering.system.domain.event.DomainEvent;

// This interface will be implemented with each Saga step.
// The process method will handle the standard processing with a transaction.
// The rollback method will handle the compensating transaction in case a failure occurs in the next Saga step,
// If the next saga step fails, previous one should be able to roll back its changes.
public interface SagaStep<T> {
    void process(T data);
    void rollback(T data);
}
