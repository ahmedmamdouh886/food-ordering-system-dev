package com.food.ordering.system.payment.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    /**
     * Remember, I haven't put to spring dependency in core domain layer of payment service,
     * but I still want to inject and use this payment domain service from other modules.
     * So I create a bean configuration here in the container module for this purpose.
     */
    @Bean
    public PaymentDomainService paymentDomainService() {
        return new PaymentDomainServiceImpl();
    }
}
