package com.food.ordering.system.order.service.dataaccess.customer.repository;

import com.food.ordering.system.order.service.dataaccess.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository // To make this interface aa Spring managed JPA Bean.
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

}
