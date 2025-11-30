package com.food.ordering.system.domain.event;

// In some saga steps, I will not need to fire an event if it's an ending operation.
// That's why we created this class.
public final class EmptyEvent implements DomainEvent<Void> {
    public static final EmptyEvent INSTANCE = new EmptyEvent();

    private EmptyEvent() {

    }

    @Override
    public void fire() {

    }
}
