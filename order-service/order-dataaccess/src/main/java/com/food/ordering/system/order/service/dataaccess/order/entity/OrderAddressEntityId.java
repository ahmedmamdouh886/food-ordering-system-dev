package com.food.ordering.system.order.service.dataaccess.order.entity;

import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddressEntityId {
    private UUID id;
    private OrderEntity order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAddressEntityId that = (OrderAddressEntityId) o;
        return Objects.equals(id, that.id) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }
}
