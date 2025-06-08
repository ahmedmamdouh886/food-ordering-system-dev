package com.food.ordering.system.order.service.dataaccess.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
// It's required, because Spring will need no-args-constructor to create a proxy object from this class.
@AllArgsConstructor // It's required to use the builder pattern with @Builder annotation. That's why we used it here.
@IdClass(OrderItemEntityId.class) // So, this ID class annotation is required to use an ID class in an entity with multi column primary key.
@Table(name = "orders")
@Entity
public class OrderItemEntity {
    @Id
    private Long id;

    @Id // We used @Id here and @Id on "private Long id" above to create multi-column primary key. So the long id field will only provide a unique item in a specific order.
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    private UUID productId;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subTotal;

    // We use equals and hashCode function with id and order, because the primary key here consists of two fields is and order_id as mentioned above.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemEntity that = (OrderItemEntity) o;
        return id.equals(that.id) && order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order);
    }
}
