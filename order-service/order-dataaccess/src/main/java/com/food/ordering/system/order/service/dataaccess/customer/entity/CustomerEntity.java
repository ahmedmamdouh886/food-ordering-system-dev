package com.food.ordering.system.order.service.dataaccess.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * In order service domain logic we need some data about restaurant and customer. To reach that data I used
 * materialized view for restaurant data or a table that is a projected copy of the customer data which is
 * populated using event store. We would go ask for that data through api calls but that creates a more
 * problematic coupling between the services. Therefore, I prefer to use this approach for data required from
 * other domains.
 */
@Table(name = "order_customer_m_view", schema = "customers") // As mentioned previously for the early examples, I will use a materialized view from the customer database schema in the order service.
@Entity
public class CustomerEntity {

    @Id
    private UUID id;
}
