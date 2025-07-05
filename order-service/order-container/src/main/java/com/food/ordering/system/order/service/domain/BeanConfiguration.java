package com.food.ordering.system.order.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// I will mark this class with configuration annotation so this will be a spring-managed configuration Bean.
@Configuration
public class BeanConfiguration {

    /**
     * I will add order domain service method with return type order domain service, and here return a new order domain service impl object.
     *
     * I will put Bean annotation on these methods.
     *
     * So what does these methods do?
     *
     * Remember, in the domain core, I haven't added any spring dependency.
     *
     * So the order domain service, is not marked as a spring Bean in the domain core module.
     *
     * But I still want to use the order domain service as a spring bean and injected to the order application service module.
     *
     * To use this order domain service as a spring bean, I need to register it as a Bean.
     *
     * And I'll do this here in this Be Configuration class.
     *
     * So when this Spring Boot application starts, it'll register the order domain service as a spring bean, although I don't have a dependency to the spring on the order domain core.
     */
    @Bean
    public OrderDomainService OrderDomainService() {
        return new OrderDomainServiceImpl();
    }
}
