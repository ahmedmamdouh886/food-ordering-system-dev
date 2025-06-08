package com.food.ordering.system.order.service.dataaccess.order.entity;

import java.io.Serializable;
import java.util.Objects;

// So, this ID class annotation is required to use an ID class in an entity with multi column primary key.
// This class is required to create multi-column primary key. See @IdClass(OrderItemEntityId.class) in OrderItemEntity
public class OrderItemEntityId implements Serializable {
    private Long id;
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntityId that = (OrderItemEntityId) o;
        return id.equals(that.id) && order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }
}
