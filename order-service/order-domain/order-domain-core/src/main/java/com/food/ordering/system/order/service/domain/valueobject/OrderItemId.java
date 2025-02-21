package com.food.ordering.system.order.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

/*
The uniqueness of order item is only importance in the aggregates,
so I don't need a UUID field here.
Having a long value starting from one will be enough.
 */
public class OrderItemId extends BaseId<Long> {
    public OrderItemId(Long value) {
        super(value);
    }
}
