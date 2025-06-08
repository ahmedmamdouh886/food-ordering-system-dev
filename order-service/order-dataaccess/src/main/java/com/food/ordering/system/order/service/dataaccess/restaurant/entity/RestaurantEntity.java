package com.food.ordering.system.order.service.dataaccess.restaurant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
// It's required, because Spring will need no-args-constructor to create a proxy object from this class.
@AllArgsConstructor // It's required to use the builder pattern with @Builder annotation. That's why we used it here.
@IdClass(RestaurantEntityId.class) // This way I will set the restaurantEntityID class as the primary key of the restaurantEntity using this ID class annotation.
/**
 * In order service domain logic we need some data about restaurant and customer. To reach that data I used
 * materialized view for restaurant data or a table that is a projected copy of the customer data which is
 * populated using event store. We would go ask for that data through api calls but that creates a more
 * problematic coupling between the services. Therefore, I prefer to use this approach for data required from
 * other domains.
 */
@Table(name = "order_restaurant_m_view") // So as in the customer entity, I will use a materialized view from the restaurant database to query restaurant information in the order domain layer.
@Entity
public class RestaurantEntity {

    @Id
    private UUID restaurantId;

    @Id
    private UUID productId;

    private String restaurantName;

    private Boolean restaurantActive;

    private String productName;

    private BigDecimal productPrice;
}
