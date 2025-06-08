package com.food.ordering.system.order.service.dataaccess.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
// It's required, because Spring will need no-args-constructor to create a proxy object from this class.
@AllArgsConstructor // It's required to use the builder pattern with @Builder annotation. That's why we used it here.
@Table(name = "order_address")
@Entity
public class OrderAddressEntity {
    @Id
    private UUID id;

    @OneToOne()
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    private String street;

    private String postalCode;

    private String city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderAddressEntity that = (OrderAddressEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
