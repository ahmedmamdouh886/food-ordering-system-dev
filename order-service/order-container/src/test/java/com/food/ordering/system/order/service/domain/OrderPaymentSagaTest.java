package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.food.ordering.system.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

// This is integration test for Order payment saga.

@Slf4j
// This is the spring boot main class for order service application.
// With this Spring Boot test annotation pointing to the OrderServiceApplication class,
// I aim to start the Spring Boot context in my test like I am starting the application.
@SpringBootTest(classes = OrderServiceApplication.class)
// Please note that the setup SQL uses the default ExecutionPhase which is BEFORE_TEST_METHOD,
// so it will be executed before each test method.
@Sql(value = {"classpath:sql/OrderPaymentSagaTestSetUp.sql"})
// executionPhase = AFTER_TEST_METHOD means that OrderPaymentSagaTestCleanUp will be executed after each test method.
@Sql(value = {"classpath:sql/OrderPaymentSagaTestCleanUp.sql"}, executionPhase = AFTER_TEST_METHOD)
public class OrderPaymentSagaTest {

    @Autowired
    private OrderPaymentSaga orderPaymentSaga;

    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    private final UUID SAGA_ID = UUID.fromString("15a497c1-0f4b-4eff-b9f4-c402c8c07afa");
    private final UUID ORDER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb17");
    private final UUID CUSTOMER_ID = UUID.fromString("d215b5f8-0249-4dc5-89a3-51fd148cfb41");
    private final UUID PAYMENT_ID = UUID.randomUUID();
    private final BigDecimal PRICE = new BigDecimal("100");

//    @Test
//    void testDoublePayment() {
//        // And I will do that two times.
//        // I will call the process method with a PaymentResponse object that has the same saga ID.
//        // It will simulate that the first thread runs the process,
//        // and the second thread only comes
//        // after first thread returns from this process method,
//        // that is, after the transaction is committed.
//        orderPaymentSaga.process(getPaymentResponse());
//        orderPaymentSaga.process(getPaymentResponse());
//    }

//    @Test
//    void testDoublePaymentWithThreads() throws InterruptedException {
//        Thread thread1 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));
//        Thread thread2 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));
//
//        thread1.start();
//        thread2.start();
//
//        thread1.join();
//        thread2.join();
//
//        assertPaymentOutbox();
//    }

    // This test case is the same as the testDoublePaymentWithThreads test case, but implemented with CountDownLatch.
    // CountDownLatch is a synchronization construct
    // that allows one or more threads to wait
    // until the set of operations
    // being performed in other threads completes.
    // And I will create two threads here
    // and run the process method as in the previous test method.
    @Test
    void testDoublePaymentWithLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occurred for thread1");
            } finally {
                latch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            } catch (OptimisticLockingFailureException e) {
                log.error("OptimisticLockingFailureException occurred for thread2");
            } finally {
                latch.countDown();
            }
        });

        thread1.start();
        thread2.start();

        // The await method blocks until the current count reaches to 0 due to invocations of the CountDown method.
        // Since I have count two, this will wait until both threads complete it.
        // So I force the two threads to run almost at the same time again.
        // In other words, only 2 threads will work at the same time.
        latch.await();

        assertPaymentOutbox();
    }

    private void assertPaymentOutbox() {
        Optional<PaymentOutboxEntity> paymentOutboxEntity =
                paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(ORDER_SAGA_NAME, SAGA_ID,
                        List.of(SagaStatus.PROCESSING));

        assertTrue(paymentOutboxEntity.isPresent());
    }

    private PaymentResponse getPaymentResponse() {
        return PaymentResponse.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(SAGA_ID.toString())
                .paymentStatus(com.food.ordering.system.domain.valueobject.PaymentStatus.COMPLETED)
                .paymentId(PAYMENT_ID.toString())
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(Instant.now())
                .failureMessages(new ArrayList<>())
                .build();
    }
}