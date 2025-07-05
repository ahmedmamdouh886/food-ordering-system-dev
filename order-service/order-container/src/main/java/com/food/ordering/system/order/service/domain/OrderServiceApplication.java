package com.food.ordering.system.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// This one is required for a multi-module application and here I set a strict package name, so only the entities, that sits on com.food.ordering.system.order.service.dataaccess package will be scanned as JPA Repositories.
@EnableJpaRepositories(basePackages = {"com.food.ordering.system.order.service.dataaccess", "com.food.ordering.system.dataaccess"})
// This one is required for a multi-module application and here I set a strict package name, so only the entities, that sits on com.food.ordering.system.order.service.dataaccess package will be scanned as JPA entities.
@EntityScan(basePackages = {"com.food.ordering.system.order.service.dataaccess", "com.food.ordering.system.dataaccess"})
// @SpringBootApplication This annotation will mark this class as the Spring Boot application.
// The scanBasePackages property is important when working with multiple modules. With this property, any package on the other modules will be scanned as long as it starts with com.food.ordering.system as the package name.
@SpringBootApplication(scanBasePackages = "com.food.ordering.system")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
